package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import static com.pedropathing.api.Paths.*;
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
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import java.util.ArrayList;
import java.util.List;

public abstract class RedFarFinal extends OpMode {
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private List<Pose> points;
    private shooter shooter;
    private Intake intake;
    private Flower flower;
    private Pose startPose;
    private Pose lastPose;
    private int stepCount;
    private Command routine;
    protected enum HeadingStyle {
        LINEAR,
        CONSTANT,
        TANGENT
    }
    protected static final double DEFAULT_SHOOTER_VELOCITY = 1500;
    protected static final long SPIN_UP_TIMEOUT_MS = 1500;
    protected static final long FEED_MS = 5000;
    protected static final double INTAKE_DOWN_POSITION = 0.55;
    protected static final long INTAKE_SETTLE_MS = 300;
    public static final double FIELD_CENTER = 70.75;
    protected abstract Pose startPos();
    protected abstract List<Command> steps();
    protected boolean isRed() {return false;}
    protected double shooterVelocity() {return DEFAULT_SHOOTER_VELOCITY;}

    protected Pose pose(double x, double y, double headingDegrees) {
        PoseFactory factory = PoseFactory.degrees();
        if (isRed()) {
            factory = factory.mirrorAroundPoint(FIELD_CENTER, FIELD_CENTER);
        }
        return factory.of(x, y, headingDegrees);
    }

    protected Command driveTo(double x, double y, double headingDegrees, BaseAuto.HeadingStyle style) {
        Pose from = lastPose;
        Pose to = pose(x, y, headingDegrees);
        lastPose = to;
        Path leg = line(from, to);
        switch (style) {
            case CONSTANT:
                leg = leg.constant(to);
                break;


}

    }


