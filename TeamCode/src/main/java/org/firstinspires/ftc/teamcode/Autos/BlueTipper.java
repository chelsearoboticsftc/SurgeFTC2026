package org.firstinspires.ftc.teamcode.Autos;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name="Blue Tipper", group="Blue")
public class BlueTipper extends OpMode {
    //shoot once
    private Follower follower;
    private shooter shooter;
    private Intake intake;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    // FILL THESE IN AFTER TESTING
    private static final double SHOOT_VELOCITY = 1500;
    private static final long SPIN_UP_TIMEOUT_MS = 1500;
    private static final long FEED_MS = 5000;

    private static final double START_X = 80;
    private static final double PARK_X = 131;

    private final Pose start = poseFactory.of(START_X, 8, 270);
    private final Pose clear = poseFactory.of(START_X, 33, 270);
    private final Pose park = poseFactory.of(PARK_X, 33, 270);
//    private final Pose point3 = poseFactory.of(116, 123, 90);
//waits for exactly 10000 ms (10s) then shoots 3 pollen and parks,
    public Path path1() {
        return line(start, clear).linear(start, clear);
    }

    public Path path2() {
        return line(clear, park).linear(clear, park);
    }

    private Command autoRoutine() {
        return sequential(
                instant(() -> shooter.SetShooterVelocity(SHOOT_VELOCITY)),
                waitUntil(shooter::isAtSpeed).raceWith(waitMs(SPIN_UP_TIMEOUT_MS)),
                instant(()->shooter.gateSetPower(1)),
                instant(()->intake.setIndexPower(1)),
                instant(()->intake.setMotorPower(1)),
                instant(()->shooter.gateSetPower(1)),
                waitMs(FEED_MS),
                instant(()->shooter.gateSetPower(0)),
                instant(()->intake.setIndexPower(0)),
                instant(()->intake.setMotorPower(0)),
                instant(()->shooter.SetShooterVelocity(0)),
                follow(follower, path1()),
                follow(follower, path2())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        shooter = new shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        follower.setPose(start);//sets the starting point of your Robot
        follower.update();
        OpModeStorage.autonomousEndPose = park;
    }

    @Override
    public void init_loop() {
        telemetry.addData("START", "x=%.0f y=%.0f facing %.0f°", start.x(), start.y(), Math.toDegrees((start.heading())));
        telemetry.update();
    }
    @Override
    public void start() {
        schedule(autoRoutine());
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