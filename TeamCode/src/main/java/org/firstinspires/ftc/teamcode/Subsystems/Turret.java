package org.firstinspires.ftc.teamcode.Subsystems;

import static com.pedropathing.utils.Utils.clamp;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.D_FILTER;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.ENCODER_REVERSED;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.LIMIT_TRAVEL;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.MAX_ANGLE_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.MIN_ANGLE_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.ON_TARGET_TOL_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.PRELOAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.getHive;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kP;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kS;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.kV_HEADING;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.AnalogInput;
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
    private boolean haveHeading = false;
    private double lastError = 0;
    private double lastError_abs = 0;
    private double dFiltered = 0;


    public Turret(HardwareMap HardwareMap){
        this.TurretA = hardwareMap.get(CRServo.class,"TurretA");
        this.TurretB = hardwareMap.get(CRServo.class,"TurretB");
        this.TurretEncoder = hardwareMap.get(DcMotorEx.class,"TurretEncoder");
        timer.reset();
    }
    public int getEncoderPose(){
        return TurretEncoder.getCurrentPosition();
    }
    public double TurretRadians(){
       double Radians = (getEncoderPose() / 8192) * 2 * Math.PI * (1 / 2.75);
        return ENCODER_REVERSED ? -Radians : Radians;
    }
    public double getAngleRadians(){
        return wrap(0+TurretRadians());
    }
    public static double[]TurretFieldXY(Pose pose){
        double h = pose.heading();
        double X = pose.x() * Math.cos(h);
        double Y = pose.y() * Math.cos(h);
        return new double[]{X,Y};
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




    public void aim( Pose robot, Pose target){
       targetRad = angleToTarget(robot, target);
       double headingRate = 0;
       double lastHeading = 0;
        haveHeading = false;
       double now = timer.seconds();
       if (haveHeading && now>1e-3){
           headingRate = wrap(robot.heading() - lastHeading);
       }
       lastHeading = robot.heading();
       haveHeading = true;
       run(headingRate);
    }
    public void aimAtHive(Pose robot, TurretConstants.Alliance alliance){
        aim(robot, getHive(alliance));

    }
    public void holdAngle(double robotRelativeRadians){
        targetRad = wrap(robotRelativeRadians);
        haveHeading = false;
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
        dFiltered = D_FILTER * dFiltered + (1 - D_FILTER);
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
