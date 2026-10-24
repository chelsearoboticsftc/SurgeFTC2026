package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
public class RedFar extends OpMode {
    //shoot once
    private Follower follower;
    private shooter Shooter;
    private Intake Intake;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(27, 132, 90);
    private final Pose path1 = poseFactory.of(27, 18, 90);
    private final Pose point2 = poseFactory.of(60, 18, 270);
    private final Pose point3 = poseFactory.of(18, 18, 270);
    private final Pose point4 = poseFactory.of(18, 99, 270);
    public final Pose endpoint = poseFactory.of(9, 85, 270);


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

    public Path path5() {
        return Paths.line(point4, endpoint).linear(point4, endpoint);
    }    private Command autoRoutine() {
        return sequential(
                waitMs(6000),
                follow(follower,path1()),
                waitMs(100),
                follow(follower,path2()),
                instant(()->Shooter.GateToggleDown()),
                waitMs(100),
                instant(()->Intake.setIndexPower(1)),
                instant(()->Shooter.SetShooterVelocity(1700)),
                waitMs(1000),
                instant(()->Shooter.SetShooterVelocity(0)),
                instant(()->Intake.setIndexPower(0)),
                instant(()->Shooter.GateToggleUp()),
                follow(follower,path3()),
                waitMs(100),
                follow(follower,path4()),
                waitMs(100),
                follow(follower,path5())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);//sets the starting point of your Robot
        follower.update();
        Shooter = new shooter(hardwareMap);
        Intake = new Intake(hardwareMap);
        OpModeStorage.autonomousEndPose = endpoint;
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