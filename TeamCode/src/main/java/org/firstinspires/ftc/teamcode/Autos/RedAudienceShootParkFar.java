package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import com.pedropathing.follower.Follower;

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

@Autonomous
public class RedAudienceShootParkFar extends OpMode {
    private Follower follower;
    private shooter shooter;
    private Intake intake;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private static final double SHOOT_VELOCITY = 1600;
    private static final long SPIN_UP_TIMEOUT_MS = 1500;
    private static final long FEED_MS = 5000;

    private final Pose start = poseFactory.of(55.75, 7.75, 90);
    private final Pose path1 = poseFactory.of(55.75, 19.75, 90);
    private final Pose point2Start = poseFactory.of(55.75, 19.75, 90);
    private final Pose point2 = poseFactory.of(31.75, 19.75, 90);
    private final Pose point3Start = poseFactory.of(31.75, 19.75, 90);
    private final Pose point3 = poseFactory.of(31.75, 118.75, 90);
    private final Pose point4Start = poseFactory.of(31.75, 118.75, 90);
    private final Pose point4 = poseFactory.of(10.75, 118.75, 90);

    public Path path1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path path4() {
        return Paths.line(point4Start, point4).linear(point4Start, point4);
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
                follow(follower,path1()),
                follow(follower,path2()),
                follow(follower,path3()),
                follow(follower,path4())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);//sets the starting point of your Robot
        follower.update();
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
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }



}
