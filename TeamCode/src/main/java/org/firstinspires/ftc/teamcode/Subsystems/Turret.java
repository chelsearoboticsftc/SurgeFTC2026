package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.CABLE_LIMIT_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.D_FILTER;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.ENCODER_REVERSED;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.LIMIT_TRAVEL;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.MAX_ANGLE_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.MIN_ANGLE_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.OFFSET_X;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.OFFSET_Y;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.ON_TARGET_TOL_RAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.PRELOAD;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.getGoals;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.GOAL_SWITCH_Y;
import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.GOAL_SWITCH_MARGIN;
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
    private double lastError_abs =  Math.PI;
    private double dFiltered = 0;
    private int currentGoal = -1;
    private double lastTargetRad = 0, targetRateFiltered = 0, lastU = 0;
    public double getLastU(){ return lastU; }
    public void turnTurret(double power) {
        TurretA.setPower(power);
        TurretB.setPower(power);
    }

    public double getTurretEncoder(){
        return TurretEncoder.getCurrentPosition();
    }


    public Turret(HardwareMap hardwareMap){ this(hardwareMap, true); }   // auto: zero at init

    public Turret(HardwareMap hardwareMap, boolean resetEncoder){
        this.TurretA = hardwareMap.get(CRServo.class,"TurretA");
        this.TurretB = hardwareMap.get(CRServo.class,"TurretB");
        this.TurretEncoder = hardwareMap.get(DcMotorEx.class,"intakeMotor");
        if (resetEncoder){
            TurretEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
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

    public Pose pickGoal(Pose robot, TurretConstants.Alliance alliance){
        Pose[] g = getGoals(alliance);
        double y = TurretFieldXY(robot)[1];

        if (currentGoal < 0){
            currentGoal = y < GOAL_SWITCH_Y ? 0 : 1;          // 0 = lower, 1 = upper
        } else if (currentGoal == 0 && y > GOAL_SWITCH_Y + GOAL_SWITCH_MARGIN){
            currentGoal = 1;
        } else if (currentGoal == 1 && y < GOAL_SWITCH_Y - GOAL_SWITCH_MARGIN){
            currentGoal = 0;
        }
        return g[currentGoal];
    }

    public void aimAtGoal(Pose robot, double headingRateRadPerSec, TurretConstants.Alliance alliance){
        aim(robot, pickGoal(robot, alliance), headingRateRadPerSec);
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
        double tgtDelta = wrap(targetRad - lastTargetRad);
        lastTargetRad = targetRad;
        double rawRate = Math.abs(tgtDelta) > Math.toRadians(30) ? 0 : tgtDelta / dt;
        targetRateFiltered = 0.8 * targetRateFiltered + 0.2 * rawRate;

        double angle = TurretRadians();
        double target = chooseTarget(targetRad, angle);
        double error = target - angle;

        double DRaw = (error - lastError) / dt;
        lastError = error;
        dFiltered = D_FILTER * dFiltered + (1 - D_FILTER) * DRaw;
        lastError_abs = Math.abs(error);

        boolean onTarget = Math.abs(error) <= ON_TARGET_TOL_RAD;
        boolean moving = Math.abs(targetRateFiltered) > Math.toRadians(5);
        boolean hold = onTarget && !moving;
        double u = 0;
        double preload = 0;

        if (!hold) {
            double stiction = onTarget ? 0 : kS * Math.signum(error);   // no kS kick inside the band
            u = kP * error + kD * dFiltered + stiction + kV_HEADING * targetRateFiltered;
            u = clamp(u, -1.0, 1.0);
        } else {
            preload = PRELOAD;
        }
        lastU = u;

        TurretA.setPower(u + preload);
        TurretB.setPower(u - preload);
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
    private static double chooseTarget(double wrappedTarget, double angle){
        double best = Double.NaN, bestDist = Double.MAX_VALUE;
        for (int k = -1; k <= 1; k++){
            double cand = wrappedTarget + k * 2 * Math.PI;
            if (Math.abs(cand) > CABLE_LIMIT_RAD) continue;
            double d = Math.abs(cand - angle);
            if (d < bestDist){ bestDist = d; best = cand; }
        }
        return Double.isNaN(best) ? clamp(wrappedTarget, -CABLE_LIMIT_RAD, CABLE_LIMIT_RAD) : best;
    }

}
