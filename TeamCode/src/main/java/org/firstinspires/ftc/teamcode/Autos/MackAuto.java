

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

@Autonomous
public class MackAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startPose = poseFactory.of(21.1113, 6.8887, 90);
    private final Pose path1 = poseFactory.of(55, 7, 180);
    private final Pose point2 = poseFactory.of(55, 24, -90);
    private final Pose point3 = poseFactory.of(9, 24, 0);
    private final Pose point4 = poseFactory.of(9, 108, -90);

    public Path path1() {
        return Paths.line(startPose, path1).linear(startPose, path1);
    }

    public Path path2() {
        return Paths.line(path1, point2).reverseTangent();
    }

    public Path path3() {
        return Paths.line(point2, point3).reverseTangent();
    }

    public Path path4() {
        return Paths.line(point3, point4).reverseTangent();
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                // Add mechanism commands here.
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);//sets the starting point of your Robot
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

