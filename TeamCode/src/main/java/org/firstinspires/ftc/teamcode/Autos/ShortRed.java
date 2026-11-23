package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

// ShortBlue turned 180° around the field center. Starts at (9.5, 69.5) facing 0°,
// which is NOT where the old ShortRed started (9, 63): place the robot there.
@Autonomous
public class ShortRed extends ShortBlue {
    @Override
    protected boolean isRed() {
        return true;
    }
}
