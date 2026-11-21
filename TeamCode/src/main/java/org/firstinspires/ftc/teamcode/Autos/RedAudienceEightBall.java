package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class RedAudienceEightBall extends BlueFarEightBall {
    @Override
    protected boolean isRed() {
        return true;
    }
}
