package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.hardware.limelightvision.*;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;


import java.util.ArrayList;
import java.util.List;
public class Limelight {
   Limelight3A limelight;
   private final Telemetry telemetry;
    public static final int RED_OPPOSITE_MIN = 30, RED_OPPOSITE_MAX = 33;
    public static final int RED_AUDIENCE_MIN = 34, RED_AUDIENCE_MAX = 37;
    public static final int BLUE_OPPOSITE_MIN = 38, BLUE_OPPOSITE_MAX = 41;
    public static final int BLUE_AUDIENCE_MIN = 42, BLUE_AUDIENCE_MAX = 45;

    public Limelight(HardwareMap hardwareMap, Telemetry telemetry){
        this.limelight = hardwareMap.get(Limelight3A.class,"limelight");
        this.telemetry = telemetry;
        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
    }

    public void start(){
        limelight.start();

    }
    public void stop(){
        limelight.stop();

    }


    public void update() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            telemetry.addData("tx", result.getTx());
            telemetry.addData("ty", result.getTy());
        } else {
            telemetry.addData("Limelight", "no valid target");
        }
    }


}

