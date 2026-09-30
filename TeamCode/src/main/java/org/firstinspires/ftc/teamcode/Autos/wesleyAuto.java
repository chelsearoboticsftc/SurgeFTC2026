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
public class wesleyAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose startered = poseFactory.of(57, 9, 90);
    private final Pose path1 = poseFactory.of(9, 9, 180);
    private final Pose point2 = poseFactory.of(12, 120, 88.4518);
    private final Pose point3 = poseFactory.of(57, 120, 0);
    private final Pose point4 = poseFactory.of(57, 120, 0);
    private final Pose point5 = poseFactory.of(9.9906, 52.7524, -124.9554);
    private final Pose point6 = poseFactory.of(57, 120, 55.0446);
    private final Pose point7 = poseFactory.of(57, 102, -90);
    private final Pose point8 = poseFactory.of(6, 102, 180);

    public Path path1() {
        return Paths.line(startered, path1).linear(startered, path1);
    }

    public Path path2() {
        return Paths.line(path1, point2).tangent();
    }

    public Path path3() {
        return Paths.line(point2, point3).tangent();
    }

    public Path path4() {
        return Paths.line(point3, point4).tangent();
    }

    public Path path5() {
        return Paths.line(point4, point5).tangent();
    }

    public Path path6() {
        return Paths.line(point5, point6).tangent();
    }

    public Path path7() {
        return Paths.line(point6, point7).tangent();
    }

    public Path path8() {
        return Paths.line(point7, point8).tangent();
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                // Add mechanism commands here.
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4()),
                follow(follower, path5()),
                follow(follower, path6()),
                follow(follower, path7()),
                follow(follower, path8())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startered);//sets the starting point of your Robot
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
