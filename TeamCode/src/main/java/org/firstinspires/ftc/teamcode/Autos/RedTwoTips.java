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
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class RedTwoTips extends OpMode {
    private Follower follower;
    private shooter shooter;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose path2 = poseFactory.of(63, 9, 90);
    private final Pose path1 = poseFactory.of(63.0627, 26.3504, 90);
    private final Pose point2 = poseFactory.of(33, 9, 180);
    private final Pose point3 = poseFactory.of(12, 9, 180);
    private final Pose point4 = poseFactory.of(17.5363, 26.4305, 180);
    private final Pose point5 = poseFactory.of(18, 114, 270);
    private final Pose point6 = poseFactory.of(60, 114, 90);
    private final Pose point7 = poseFactory.of(60, 129, 180);
    private final Pose point8 = poseFactory.of(45, 129, 180);
    private final Pose point9 = poseFactory.of(54, 123, 270);
    private final Pose point10 = poseFactory.of(9, 108, 270);

    public Path path1() {
        return Paths.line(path2, path1).linear(path2, path1);
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

    public Path path5() {
        return Paths.line(point4, point5).linear(point4, point5);
    }

    public Path path6() {
        return Paths.line(point5, point6).linear(point5, point6);
    }

    public Path path7() {
        return Paths.line(point6, point7).linear(point6, point7);
    }

    public Path path8() {
        return Paths.line(point7, point8).linear(point7, point8);
    }

    public Path path9() {
        return Paths.line(point8, point9).linear(point8, point9);
    }

    public Path path10() {
        return Paths.line(point9, point10).linear(point9, point10);
    }    private Command autoRoutine() {
        double milliseconds;
        return sequential(
                instant(()->shooter.GateToggleDown()),
                waitMs(100),
                instant(()->shooter.SetShooterVelocity(1000)),
                waitMs(1000),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower,path1()),
                follow(follower,path2()),
                follow(follower,path3()),
                follow(follower,path4()),
                follow(follower,path5()),
                follow(follower,path6()),
                follow(follower,path7()),
                instant(()->shooter.GateToggleDown()),
                waitMs(100),
                instant(()->shooter.SetShooterVelocity(1000)),
                waitMs(1000),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower,path8()),
                follow(follower,path9()),
                instant(()->shooter.GateToggleDown()),
                waitMs(100),
                instant(()->shooter.SetShooterVelocity(1000)),
                waitMs(1000),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower,path10())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(path2);//sets the starting point of your Robot
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
