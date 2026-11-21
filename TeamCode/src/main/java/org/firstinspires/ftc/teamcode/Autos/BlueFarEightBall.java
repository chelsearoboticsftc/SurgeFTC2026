package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.api.Paths;
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

import java.util.Arrays;
import java.util.List;

/*  private final Pose start = poseFactory.of(85.75, 133.75, 90);
    private final Pose path1 = poseFactory.of(85.75, 112.75, 90);
    private final Pose point2 = poseFactory.of(133.75, 112.75, 90);
    private final Pose point3 = poseFactory.of(133.75, 133.75, 90);
    private final Pose point4 = poseFactory.of(133.75, 112.75, 90);
    private final Pose point5 = poseFactory.of(115.75, 112.75, 90);
    private final Pose point6 = poseFactory.of(115.75, 19.75, 270);
    private final Pose point7 = poseFactory.of(82.75, 19.75, 270);
    private final Pose point8 = poseFactory.of(82.75, 7.75, 270);
    private final Pose point9 = poseFactory.of(82.75, 19.75, 270);
    private final Pose point10 = poseFactory.of(115.75, 19.75, 270);
    private final Pose point11 = poseFactory.of(127.75, 22.75, 270); */
@Autonomous
public class BlueFarEightBall extends BaseAuto {
    @Override
    protected Pose startPos() {
        return pose(85.75, 133.75, 90);
    }

    @Override
    protected List<Command> steps() {
        return Arrays.asList(
                spinUp(),
                shoot(),
                driveTo(85.75, 112.75, 90),
                driveTo(133.75, 112.75, 90),
                intakeOn(),
                driveTo(133.75, 133.75, 90),
                pause(2000),
                intakeOff(),
                driveTo(133.75, 112.75, 90),
                driveTo(115.75, 112.75, 90),
                driveTo(115.75, 19.75, 270),
                driveTo(82.75, 19.75, 270),
                driveTo(82.75, 7.75, 270),
                spinUp(),
                shoot(),
                driveTo(82.75, 19.75, 270),
                driveTo(115.75, 19.75, 270),
                driveTo(127.75, 22.75, 270)
        );
    }
}