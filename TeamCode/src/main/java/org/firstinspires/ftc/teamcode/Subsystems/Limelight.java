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




    // how many degrees back is your limelight rotated from perfectly vertical?
    double limelightMountAngleDegrees = 25.0;

    // distance from the center of the Limelight lens to the floor
    double limelightLensHeightInches = 20.0;

    // distance from the target to the floor
    double goalHeightInches = 60.0;



    //calculate distance
    //double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);

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
            telemetry.addData("distance", result.getBotposeAvgDist());
        } else {
            telemetry.addData("Limelight", "no valid target");
        }
    }


}

