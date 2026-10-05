package org.firstinspires.ftc.teamcode.Autos;

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
public class jackAuto extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose thetrashpathisZachspath = poseFactory.of(56, 8, 90);
    private final Pose path1 = poseFactory.of(56, 36, 180);
    private final Pose point2 = poseFactory.of(11.9212, 43.3117, 170.5816);
    private final Pose point3 = poseFactory.of(7.2564, 99.808, 94.7201);
    private final Pose point4 = poseFactory.of(43.5385, 136.6084, 45.4063);
    private final Pose point5 = poseFactory.of(58.0513, 115.8758, -55.008);
    private final Pose point6 = poseFactory.of(131.652, 94.1066, -16.4769);
    private final Pose point7 = poseFactory.of(95.8883, 11.1762, -113.3281);
    private final Pose point8 = poseFactory.of(50.7949, 18.4326, 170.8584);
    private final Pose point9 = poseFactory.of(8.293, 3.9198, -161.1468);
    private final Pose point10 = poseFactory.of(57.0147, 31.9088, 29.876);
    private final Pose point11 = poseFactory.of(86.5586, 108.1011, 68.8059);

    public Path path1() {
        return line(thetrashpathisZachspath, path1).linear(thetrashpathisZachspath, path1);
    }

    public Path path2() {
        return line(path1, point2).tangent();
    }

    public Path path3() {
        return line(point2, point3).tangent();
    }

    public Path path4() {
        return line(point3, point4).tangent();
    }

    public Path path5() {
        return line(point4, point5).tangent();
    }

    public Path path6() {
        return line(point5, point6).tangent();
    }

    public Path path7() {
        return line(point6, point7).tangent();
    }

    public Path path8() {
        return line(point7, point8).tangent();
    }

    public Path path9() {
        return line(point8, point9).tangent();
    }

    public Path path10() {
        return line(point9, point10).tangent();
    }

    public Path path11() {
        return line(point10, point11).tangent();
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
                follow(follower, path8()),
                follow(follower, path9()),
                follow(follower, path10()),
                follow(follower, path11())

        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(thetrashpathisZachspath);//sets the starting point of your Robot
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
