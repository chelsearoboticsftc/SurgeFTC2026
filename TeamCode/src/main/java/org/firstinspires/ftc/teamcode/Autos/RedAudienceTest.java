package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class RedAudienceTest extends BlueFarTest{
    @Override
    protected boolean isRed() {
        return true;
    }
}
