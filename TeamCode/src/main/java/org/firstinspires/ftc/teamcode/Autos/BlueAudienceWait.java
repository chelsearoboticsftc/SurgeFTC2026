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
public class BlueAudienceWait extends OpMode {
    //shoot once
    private Follower follower;
    private shooter shooter;
    private Intake intake;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(80, 8, 270);
    private final Pose path1 = poseFactory.of(80, 35, 270);
    private final Pose endpoint = poseFactory.of(130, 25, 270);
//    private final Pose point3 = poseFactory.of(116, 123, 90);
//waits for exactly 10000 ms (10s) then shoots 3 pollen and parks,
    public Path path1() {
        return line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return line(path1, endpoint).linear(path1, endpoint);
    }

    private Command autoRoutine() {
        return sequential(
                waitMs(10000),
                instant(()->shooter.SetShooterVelocity(1800)),
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
                follow(follower, path1()),
                follow(follower, path2())
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