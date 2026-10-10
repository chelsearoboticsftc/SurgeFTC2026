package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.execute;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class RedClose extends OpMode {
    private Follower follower;
    private shooter Shooter;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(57, 9, 90);
    private final Pose path1 = poseFactory.of(57, 24, 90);
    private final Pose point2 = poseFactory.of(27, 24, 90);
    private final Pose point3 = poseFactory.of(27, 99, 90);
    private final Pose point4 = poseFactory.of(9, 99, 90);

    public Path path1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return Paths.line(path1, point2).linear(path1, point2);
    }

    public Path path3() {
        return Paths.line(point2, point3).linear(point2, point3);
    }

    public Path path4() {
        return Paths.line(point3, point4).linear(point3, point4);
    }

    private Command autoRoutine() {
        return sequential(
                instant(() ->Shooter.GateToggleDown()),
                waitMs(100),
                instant(() ->Shooter.SetShooterVelocity(1000)),
                waitMs(1000),
                instant(() ->Shooter.SetShooterVelocity(0)),
                instant(() ->Shooter.GateToggleUp()),
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
        follower.setPose(start);
        follower.update();
        Shooter = new shooter(hardwareMap);
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