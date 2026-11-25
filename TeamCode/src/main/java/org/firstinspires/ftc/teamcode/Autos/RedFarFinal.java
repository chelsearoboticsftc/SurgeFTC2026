package org.firstinspires.ftc.teamcode.Autos;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autos.BaseAuto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Same route as the old ShortBlue (now in Autos/legacy): pull off the wall, wait, park.
@Autonomous
public class RedFarFinal extends BaseAuto {
    @Override
    protected Pose startPos() {
        return pose(81.75, 8.75, 270);
    }

    @Override
    protected List<Command> steps() {
        return Arrays.asList(
                pause(1000),
                driveTo(81.75, 23.75, 270),
                shoot(),
                driveTo(95.203, 23.75, 270),
                flowerDown(),
                intakeOn(),
                driveTo(94.986, 11.832, 270),
                pause(2000),
                driveTo(81.601, 18.721, 270),
                flowerUp(),
                intakeOff(),
                driveTo(81.921, 8.763, 270),
                shoot(),
                driveTo(95.194, 23.598, 270),
                driveTo(132.392, 24.088, 270)
        );
    }
   // pose(81.75, 8.75, 270)
    //pose(81.75, 23.75, 270)
   // pose(81.75, 23.75, 270)
    //pose(95.203, 23.75, 270)
    // pose(95.203, 23.75, 270)
//    pose(94.986, 11.832, 270)
//    pose(94.986, 11.832, 270)
//    pose(81.601, 18.721, 270)
//    pose(81.601, 18.721, 270)
//    pose(81.921, 8.763, 270)
//    pose(81.921, 8.763, 270)
//    pose(95.194, 23.598, 270)
//    pose(95.194, 23.598, 270)
// pose(132.392, 24.088, 270)
}