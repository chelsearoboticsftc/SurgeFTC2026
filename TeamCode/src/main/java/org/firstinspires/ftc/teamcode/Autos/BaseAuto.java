package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Subsystems.Flower;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private List<Pose> points;
    private shooter shooter;
    private Intake intake;
    private Flower flower;
    private Pose startPose;
    private Pose lastPose;
    private int stepCount;
    private Command routine;
    protected enum HeadingStyle {
        LINEAR,
        CONSTANT,
        TANGENT
    }
    protected static final double DEFAULT_SHOOTER_VELOCITY = 1500;
    protected static final long SPIN_UP_TIMEOUT_MS = 1500;
    protected static final long FEED_MS = 5000;
    protected static final double INTAKE_DOWN_POSITION = 0.55;
    protected static final long INTAKE_SETTLE_MS = 300;
    public static final double FIELD_CENTER = 70.75;
    protected abstract Pose startPos();
    protected abstract List<Command> steps();
    protected boolean isRed() {return false;}
    protected double shooterVelocity() {return DEFAULT_SHOOTER_VELOCITY;}

    protected Pose pose(double x, double y, double headingDegrees) {
        PoseFactory factory = PoseFactory.degrees();
        if (isRed()) {
            factory = factory.mirrorAroundPoint(FIELD_CENTER, FIELD_CENTER);
        }
        return factory.of(x,y,headingDegrees);
    }
    protected Command intakeOn() {
        return instant(()-> {
                intake.setIntakeServoL(INTAKE_DOWN_POSITION);
                intake.setMotorPower(1);
                intake.setIndexPower(1);
                shooter.gateSetPower(0);
            }
        );
    }
    protected Command intakeOff() {
        return instant(()-> {
            intake.setMotorPower(0);
            intake.setIndexPower(0);
        });
    }
    protected Command driveTo(double x, double y, double headingDegrees) {
        return driveTo(x, y, headingDegrees, HeadingStyle.LINEAR);
    }
    protected Command driveTo(double x, double y, double headingDegrees, HeadingStyle style) {
        Pose from = lastPose;
        Pose to = pose(x, y, headingDegrees);
        lastPose = to;
        Path leg = line(from, to);
        switch (style) {
            case CONSTANT:
                leg = leg.constant(to);
                break;
            case TANGENT:
                // TODO: lastPose is wrong after a TANGENT leg.
                // tangent() ignores headingDegrees: the robot ends the leg facing the way it
                // drove (from -> to), not the heading we passed in. But lastPose (set above)
                // still remembers headingDegrees, so the NEXT leg starts from a heading the
                // robot doesn't really have. A LINEAR leg after this one would turn from the
                // wrong angle. Fix: in this case, set lastPose's heading to the direction of
                // travel from 'from' to 'to'. (It has to work for Red too.)
                leg = leg.tangent();
                break;
            case LINEAR:
            default:
                leg = leg.linear(from, to);
                break;
        }
        return follow(follower, leg);
    }
    protected Command driveAndIntake(double x, double y, double headingDegrees) {
        return sequential(
            intakeOn(),
            driveTo(x, y, headingDegrees),
            pause(INTAKE_SETTLE_MS)
        );
    }
    protected Command spinUp() {
        return spinUp(shooterVelocity());
    }
    protected Command spinUp(double velocity) {
        return instant(()-> shooter.SetShooterVelocity(velocity));
    }
    protected Command shoot() {
        return shoot(shooterVelocity());
    }
    protected Command shoot(double velocity) {
        return sequential(
            spinUp(velocity),
            fire()
        );
    }
    protected Command pause(long milliseconds) {
        return waitMs(milliseconds);
    }
    private Command fire() {
        return sequential(
                waitUntil(shooter::isAtSpeed).raceWith(waitMs(SPIN_UP_TIMEOUT_MS)),
                feed(1),
                waitMs(FEED_MS),
                feed(0),
                instant(()->shooter.SetShooterVelocity(0))
        );
    }
    private Command feed(double power) {
        return instant(() -> {
                shooter.gateSetPower(power);
                intake.setMotorPower(power);
                intake.setIndexPower(power);
            }
        );
    }
    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        shooter = new shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        startPose = startPos();
        lastPose = startPose;   // the first driveTo starts from the start
        List<Command> plan = steps();
        stepCount = plan.size();
        routine = sequential(plan.toArray(new Command[0]));
        follower.setPose(startPose);
        follower.update();
    }
    @Override
    public void init_loop() {
        telemetry.addData(
            "START",
            "x=%.0f y=%.0f facing %.0f°",
            startPose.x(),
            startPose.y(),
            Math.toDegrees(
                (
                    startPose.heading()
                )
            )
        );
        telemetry.update();
    }
    @Override
    public void start() {
        schedule(routine);
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }

    @Override
    public void stop() {
        follower.update();
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}
