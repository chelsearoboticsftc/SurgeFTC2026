package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;
import org.firstinspires.ftc.teamcode.OpModeStorage;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static java.util.concurrent.locks.LockSupport.park;

@Autonomous
public class ShortRed extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(9, 63, 0);
    private final Pose path1Start = poseFactory.of(9, 63, 0);
    private final Pose path1 = poseFactory.of(21, 63, 0);
    public final Pose endpoint = poseFactory.of(16, 96, 0);

    public Path path1() {
        return Paths.line(path1Start, path1).linear(path1Start, path1);
    }

    public Path path2() {
        return Paths.line(path1, endpoint).linear(path1, endpoint);
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower,path1()),
                follow(follower,path2())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);//sets the starting point of your Robot
        follower.update();
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
