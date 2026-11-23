package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.Arrays;
import java.util.List;

// Same route as the old ShortBlue (now in Autos/legacy): pull off the wall, wait, park.
@Autonomous
public class ShortBlue extends BaseAuto {

    @Override
    protected Pose startPos() {
        return pose(132, 72, 180);
    }

    @Override
    protected List<Command> steps() {
        return Arrays.asList(
                driveTo(117, 72, 180),   // pull off the wall
                pause(500),
                driveTo(129, 25, 180)    // park in the Blue loading zone
        );
    }
}
