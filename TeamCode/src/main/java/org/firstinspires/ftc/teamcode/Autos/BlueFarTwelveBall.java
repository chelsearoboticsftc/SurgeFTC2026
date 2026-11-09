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
public class BlueFarTwelveBall extends OpMode {
    //shoot once
    private Follower follower;
    private shooter shooter;
    private Intake intake;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(82.8904, 132.9103, 90);
    private final Pose point1 = poseFactory.of(83, 120, 90);
    private final Pose point2 = poseFactory.of(97, 133, 0);
    private final Pose point3Start = poseFactory.of(97, 133, 0);
    private final Pose point3 = poseFactory.of(130, 133, 0);
    private final Pose point4Start = poseFactory.of(130, 133, 0);
    private final Pose point4 = poseFactory.of(83, 100, 0);
    private final Pose point5 = poseFactory.of(83, 8, 270);
    private final Pose point6Start = poseFactory.of(83, 8, 270);
    private final Pose point6 = poseFactory.of(95, 25, 270);
    private final Pose point7 = poseFactory.of(95, 13, 90);
    private final Pose point8 = poseFactory.of(83.75, 13, 270);
    private final Pose point9 = poseFactory.of(132, 13, 0);
    private final Pose point10Start = poseFactory.of(132, 13, 0);
    private final Pose point10 = poseFactory.of(132, 23, 0);

    public Path path1() {
        return Paths.line(start, point1).linear(start, point1);
    }

    public Path path2() {
        return Paths.line(point1, point2).constant(point2);
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path path4() {
        return Paths.line(point4Start, point4).linear(point4Start, point4);
    }

    public Path path5() {
        return Paths.line(point4, point5).constant(point5);
    }

    public Path path6() {
        return Paths.line(point6Start, point6).linear(point6Start, point6);
    }

    public Path path7() {
        return Paths.line(point6, point7).constant(point7);
    }

    public Path path8() {
        return Paths.line(point7, point8).constant(point8);
    }

    public Path path9() {
        return Paths.line(point8, point9).tangent();
    }

    public Path path10() {
        return Paths.line(point10Start, point10).linear(point10Start, point10);
    }
    private Command autoRoutine() {
        return sequential(
                instant(()->shooter.SetShooterVelocity(1750)),
                waitMs(1000),
                instant(()->intake.setIndexPower(1)),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
//                instant(()->shooter.GateToggleUp()),
                follow(follower,path1()),
                follow(follower,path2()),
                follow(follower,path3()),
                follow(follower,path4()),
                follow(follower, path5()),
                instant(()->shooter.SetShooterVelocity(1750)),
                waitMs(1000),
                instant(()->intake.setIndexPower(1)),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
//                instant(()->shooter.GateToggleUp()),
                waitMs(750),
//                instant(()->shooter.GateToggleDown()),
                waitMs(750),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
//                instant(()->shooter.GateToggleUp()),
                follow(follower, path6()),
                follow(follower, path7()),
                follow(follower, path8()),
                follow(follower, path9()),
                follow(follower, path10())
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
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.update();
    }

    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }



}
