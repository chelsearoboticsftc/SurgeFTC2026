package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.D_FILTER;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.ENCODER_REVERSED;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.LIMIT_TRAVEL;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.MAX_ANGLE_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.MIN_ANGLE_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.OFFSET_X;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.OFFSET_Y;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.ON_TARGET_TOL_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.PRELOAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.getHive;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kP;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kS;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kV_HEADING;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Turret{
    CRServo TurretA;
    CRServo TurretB;
    DcMotorEx TurretEncoder;
    private final ElapsedTime timer = new ElapsedTime();
    private double targetRad = 0;
    private double lastError = 0;
    private double lastError_abs =  Math.PI;;
    private double dFiltered = 0;



    public Turret(HardwareMap hardwareMap){
        this.TurretA = hardwareMap.get(CRServo.class,"TurretA");
        this.TurretB = hardwareMap.get(CRServo.class,"TurretB");
        this.TurretEncoder = hardwareMap.get(DcMotorEx.class,"TurretEncoder");
        TurretEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TurretEncoder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        timer.reset();
    }
    public int getEncoderPose(){
        return TurretEncoder.getCurrentPosition();
    }
    public double TurretRadians(){
       double Radians = ((double) getEncoderPose() / 8192) * 2 * Math.PI * (1 / 2.75);
        return ENCODER_REVERSED ? -Radians : Radians;
    }
    public double getAngleRadians(){
        return wrap(0+TurretRadians());
    }
    public static double[] TurretFieldXY(Pose pose){
        double h = pose.heading();
        double x = pose.x() + OFFSET_X * Math.cos(h) - OFFSET_Y * Math.sin(h);
        double y = pose.y() + OFFSET_X * Math.sin(h) + OFFSET_Y * Math.cos(h);
        return new double[]{x, y};
    }
    public static double angleToTarget(Pose robot, Pose target){
        double[]t = TurretFieldXY(robot);
        double fieldAngle = Math.atan2(target.y() - t[1], target.x() - t[0]);
        return wrap(fieldAngle - robot.heading());
    }
    public static double distanceToTarget(Pose robot, Pose target){
        double[]t = TurretFieldXY(robot);
        return Math.hypot(target.x() - t[0], target.y() - t[1] );
    }



    public void aim(Pose robot, Pose target, double headingRateRadPerSec){
        targetRad = angleToTarget(robot, target);
        run(headingRateRadPerSec);
    }

    public void aimAtHive(Pose robot, double headingRateRadPerSec, TurretConstants.Alliance alliance){
        aim(robot, getHive(alliance), headingRateRadPerSec);
    }
    public void holdAngle(double robotRelativeRadians){
        targetRad = wrap(robotRelativeRadians);
        run(0);
    }

    public static double wrap(double rad){
        while(rad >=  Math.PI) rad -= 2* Math.PI;
        while(rad <- Math.PI) rad += 2* Math.PI;
        return rad;
    }
    private void run(double headingRate){
        double dt = Math.max(timer.seconds(), 1e-3);
        timer.reset();

        double angle = getAngleRadians();
        double target = LIMIT_TRAVEL ? clamp(targetRad, MIN_ANGLE_RAD, MAX_ANGLE_RAD): targetRad;
        double error = LIMIT_TRAVEL ? (target - angle) : wrap(target - angle);

        double DRaw = (error - lastError) / dt;
        lastError = error;
        dFiltered = D_FILTER * dFiltered + (1 - D_FILTER) * DRaw;
        lastError_abs = Math.abs(error);

        double u = 0;
        if(Math.abs(error) > ON_TARGET_TOL_RAD){
            u = kP * error + kD * dFiltered + kS * Math.signum(error) - kV_HEADING * headingRate;
        }
        u = clamp(u, -(1 - PRELOAD), 1.0 - PRELOAD);
        TurretA.setPower(u + PRELOAD);
        TurretB.setPower(u - PRELOAD);
    }
    public static double clamp(double v, double low, double high){
        return Math.max(low, Math.min(high, v));
    }
    public boolean isOnTarget(){
        return lastError_abs < ON_TARGET_TOL_RAD;
    }
    public double getTargetRad(){
        return targetRad;
    }
    public void stop(){
        TurretA.setPower(0);
        TurretB. setPower(0);
    }

}
