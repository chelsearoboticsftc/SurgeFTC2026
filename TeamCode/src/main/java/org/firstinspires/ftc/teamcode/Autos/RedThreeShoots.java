package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous
public class  RedThreeShoots extends OpMode {
    private Follower follower;
    private shooter shooter;
    private Intake intake;
    private Flower flower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose path2 = poseFactory.of(63, 9, 90);
    private final Pose path1Start = poseFactory.of(63, 9, 270);
    private final Pose path1 = poseFactory.of(63.0627, 26.3504, 270);
    private final Pose point2 = poseFactory.of(41.8587, 9.0957, 180);
    private final Pose point3 = poseFactory.of(12, 9, 180);
    private final Pose point4 = poseFactory.of(17.6707, 112.8355, 90);
    private final Pose point5 = poseFactory.of(60, 114, 90);
    private final Pose point6 = poseFactory.of(60, 129, 180);
    private final Pose point7 = poseFactory.of(45, 129, 180);
    private final Pose point8 = poseFactory.of(54, 123, 90);
    private final Pose endpoint = poseFactory.of(9, 108, 90);

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
        return Paths.line(point8, endpoint).linear(point8, endpoint);
    }

    private Command autoRoutine() {
        double milliseconds;
        return sequential(
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.SetShooterVelocity(1700)),
                instant(()->intake.setIndexPower(1)),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower,path1()),
                follow(follower,path2()),
                instant(()-> intake.setMotorPower(1)),
                follow(follower,path3()),
                instant(()-> intake.setMotorPower(0)),
                follow(follower,path4()),
                follow(follower,path5()),
                follow(follower,path6()),
                instant(()->shooter.GateToggleDown()),
                waitMs(100),
                instant(()->shooter.SetShooterVelocity(1700)),
                instant(()->intake.setIndexPower(1)),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
                instant(()->shooter.GateToggleUp()),
                instant(()->flower.flowerToggleR()),
                instant(()->intake.setMotorPower(1)),
                follow(follower,path7()),
                instant(()->flower.flowerToggleR()),
                instant(()->intake.setMotorPower(0)),
                follow(follower,path8()),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.SetShooterVelocity(1700)),
                instant(()->intake.setIndexPower(1)),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.GateToggleUp()),
                waitMs(500),
                instant(()->shooter.GateToggleDown()),
                waitMs(500),
                instant(()->shooter.SetShooterVelocity(0)),
                instant(()->intake.setIndexPower(0)),
                instant(()->shooter.GateToggleUp()),
                follow(follower,path9())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(path2);//sets the starting point of your Robot
        intake = new Intake(hardwareMap);
        shooter = new shooter(hardwareMap);
        flower = new Flower(hardwareMap);
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
