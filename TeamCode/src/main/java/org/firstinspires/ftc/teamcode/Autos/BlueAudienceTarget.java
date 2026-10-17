package org.firstinspires.ftc.teamcode.Autos;

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
public class BlueAudienceTarget extends OpMode {
    private Follower follower;
    private shooter shooter;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(80, 8, 270);
    private final Pose path1 = poseFactory.of(80, 35, 270);
    private final Pose point2 = poseFactory.of(135, 35, 270);
//    private final Pose point3 = poseFactory.of(116, 123, 90);
//waits for exactly 15000 ms (15s) then shoots,
    public Path path1() {
        return line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return line(path1, point2).linear(path1, point2);
    }

    private Command autoRoutine() {
        return sequential(
                waitMs(15000),
                instant(()->shooter.GateToggleDown()),
                waitMs(100),
                instant(()->shooter.SetShooterVelocity(1000)),
                waitMs(1000),
                instant(()->shooter.SetShooterVelocity(0)),
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