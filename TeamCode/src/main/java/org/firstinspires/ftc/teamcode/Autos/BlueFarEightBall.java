package org.firstinspires.ftc.teamcode.Autos;

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

    private final Pose start = poseFactory.of(87, 133, 90);
    private final Pose path1 = poseFactory.of(132, 133, 0);
    private final Pose point2 = poseFactory.of(87, 133, 90);
    private final Pose point3 = poseFactory.of(118, 133, 90);
    private final Pose point4 = poseFactory.of(118, 46, 90);
    private final Pose point5 = poseFactory.of(131, 46, 90);

    public Path path1() {
        return line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return line(path1, point2).linear(path1, point2);
    }

    public Path path3() {
        return line(point2, point3).linear(point2, point3);
    }

    public Path path4() {
        return line(point3, point4).linear(point3, point4);
    }

    public Path path5() {
        return line(point4, point5).linear(point4, point5);
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
                follow(follower,path3()),
                follow(follower,path4()),
                follow(follower, path5())
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
