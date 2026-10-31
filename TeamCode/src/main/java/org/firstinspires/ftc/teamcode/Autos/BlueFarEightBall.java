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
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class BlueFarEightBall extends OpMode {
    //shoot once
    private Follower follower;
    private shooter shooter;
    private Intake intake;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(87.75, 135.75, 90);
    private final Pose path1 = poseFactory.of(87.75, 124.75, 0);
    private final Pose point2Start = poseFactory.of(87.75, 124.75, 0);
    private final Pose point2 = poseFactory.of(97.75, 132.75, 0);
    private final Pose point3Start = poseFactory.of(97.75, 132.75, 0);
    private final Pose point3 = poseFactory.of(130.75, 132.75, 0);
    private final Pose point4Start = poseFactory.of(130.75, 132.75, 270);
    private final Pose point4 = poseFactory.of(82.75, 106.75, 270);
    private final Pose point5Start = poseFactory.of(82.75, 106.75, 270);
    private final Pose point5 = poseFactory.of(82.75, 11.75, 270);
    private final Pose point6Start = poseFactory.of(82.75, 11.75, 270);
    private final Pose point6 = poseFactory.of(82.75, 22.75, 270);
    private final Pose point7Start = poseFactory.of(82.75, 22.75, 270);
    private final Pose point7 = poseFactory.of(131.75, 22.75, 270);
    private final Pose point8 = poseFactory.of(131.75, 22.75, 270);

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

    public Path path5() {
        return Paths.line(point5Start, point5).linear(point5Start, point5);
    }

    public Path path6() {
        return Paths.line(point6Start, point6).linear(point6Start, point6);
    }

    public Path path7() {
        return Paths.line(point7Start, point7).linear(point7Start, point7);
    }

    public Path path8() {
        return Paths.line(point7, point8).tangent();
    }
    private Command autoRoutine() {
        return sequential(
                instant(()->shooter.SetShooterVelocity(1750)),
                waitMs(1000),
                instant(()->intake.setIndexPower(1)),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower,path1()),
                follow(follower,path2()),
                follow(follower,path3()),
                follow(follower,path4()),
                follow(follower, path5()),
                instant(()->shooter.SetShooterVelocity(1750)),
                waitMs(1000),
                instant(()->intake.setIndexPower(1)),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.GateToggleUp()),
                waitMs(750),
                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower, path6()),
                follow(follower, path7()),
                follow(follower, path8())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);//sets the starting point of your Robot
        follower.update();
        shooter = new shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        OpModeStorage.autonomousEndPose = point5;
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
