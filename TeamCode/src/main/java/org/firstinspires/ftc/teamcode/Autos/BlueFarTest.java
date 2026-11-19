package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//private final Pose start = poseFactory.of(85.75, 133.75, 270);
//private final Pose path1 = poseFactory.of(85.75, 109.75, 270);
@Autonomous
public class BlueFarTest extends BaseAuto {
    @Override
    protected Pose startPos() {
        return pose(85.75, 133.75, 270);
    }
    @Override
    protected Pose park() {
        return pose(85.75, 109.75, 270);
    }
}
