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

public abstract class BaseAuto extends OpMode {
    private Follower follower;
    Pose startPose;
    Pose targetPose;
    public static final double FIELD_CENTER = 70.75;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    protected abstract Pose startPos();
    protected abstract Pose park();
    protected boolean isRed() {return false;}
    protected Pose pose(double x, double y, double headingDegrees) {
        PoseFactory factory = PoseFactory.degrees();
        if (isRed()) {
            factory = factory.mirrorAroundPoint(FIELD_CENTER, FIELD_CENTER);
        }
        return factory.of(x,y,headingDegrees);
    }
    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        startPose = startPos();
        targetPose = park();
        follower.setPose(startPose);//sets the starting point of your Robot
        follower.update();
    }
    @Override
    public void init_loop() {
        telemetry.addData("START", "x=%.0f y=%.0f facing %.0f°", startPose.x(), startPose.y(), Math.toDegrees((startPose.heading())));
        telemetry.update();
    }
    @Override
    public void start() {
        Path startToPark = Paths.line(startPose, targetPose).linear(startPose, targetPose);
        schedule(follow(follower, startToPark));
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
