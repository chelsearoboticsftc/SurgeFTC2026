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
public class  BlueAudienceSevenBall extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(82.75, 7.75, 270);
    private final Pose point1 = poseFactory.of(82.75, 13.75, 270);
    private final Pose point2Start = poseFactory.of(82.75, 13.75, 0);
    private final Pose point2 = poseFactory.of(106.75, 13.75, 0);
    private final Pose point3Start = poseFactory.of(106.75, 13.75, 0);
    private final Pose point3 = poseFactory.of(82.75, 19.75, 0);
    private final Pose point4Start = poseFactory.of(82.75, 19.75, 0);
    private final Pose point4 = poseFactory.of(82.75, 13.75, 0);
    private final Pose point5Start = poseFactory.of(82.75, 13.75, 0);
    private final Pose point5 = poseFactory.of(106.75, 13.75, 0);
    private final Pose point6Start = poseFactory.of(106.75, 13.75, 0);
    private final Pose point6 = poseFactory.of(82.75, 19.75, 0);
    private final Pose point7Start = poseFactory.of(82.75, 19.75, 0);
    private final Pose point7 = poseFactory.of(82.75, 13.75, 0);
    private final Pose point8Start = poseFactory.of(82.75, 13.75, 0);
    private final Pose point8 = poseFactory.of(106.75, 13.75, 0);
    private final Pose point9Start = poseFactory.of(106.75, 13.75, 0);
    private final Pose point9 = poseFactory.of(106.75, 7.75, 0);
    private final Pose point10Start = poseFactory.of(106.75, 7.75, 0);
    private final Pose point10 = poseFactory.of(130.75, 7.75, 0);
    private final Pose point11Start = poseFactory.of(130.75, 7.75, 0);
    private final Pose point11 = poseFactory.of(82.75, 16.75, 0);
    private final Pose point12Start = poseFactory.of(82.75, 16.75, 0);
    private final Pose point12 = poseFactory.of(82.75, 19.75, 270);
    private final Pose point13Start = poseFactory.of(82.75, 19.75, 270);
    private final Pose point13 = poseFactory.of(130.75, 31.75, 270);

    public Path path1() {
        return Paths.line(start, point1).linear(start, point1);
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }

    public Path path3() {
        return Paths.line(point3Start, point3).linear(point3Start, point3);
    }

    public Path path4() {
        return Paths.line(point4Start, point4).linear(point4Start, point4);
    }

    public Path path5() {
        return Paths.line(point5Start, point5).linear(point5Start, point5);
    }

    public Path path6() {
        return Paths.line(point6Start, point6).linear(point6Start, point6);
    }

    public Path path7() {
        return Paths.line(point7Start, point7).linear(point7Start, point7);
    }

    public Path path8() {
        return Paths.line(point8Start, point8).linear(point8Start, point8);
    }

    public Path path9() {
        return Paths.line(point9Start, point9).linear(point9Start, point9);
    }

    public Path path10() {
        return Paths.line(point10Start, point10).linear(point10Start, point10);
    }

    public Path path11() {
        return Paths.line(point11Start, point11).linear(point11Start, point11);
    }

    public Path path12() {
        return Paths.line(point12Start, point12).linear(point12Start, point12);
    }

    public Path path13() {
        return Paths.line(point13Start, point13).linear(point13Start, point13);
    }
    private Command autoRoutine() {
        return sequential(
                follow(follower , path1()),
                follow(follower , path2()),
                follow(follower , path3()),
                follow(follower , path4()),
                follow(follower , path5()),
                follow(follower , path6()),
                follow(follower , path7()),
                follow(follower , path8()),
                follow(follower , path9()),
                follow(follower , path10()),
                follow(follower , path11()),
                follow(follower , path12()),
                follow(follower , path13())




        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);//sets the starting point of your Robot
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
