package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;

public class TurretConstants {
    public enum Alliance{RED,BLUE
    }
    public static boolean ENCODER_REVERSED = false;
    public static final Pose RED_HIVE = new Pose (72,60);
    public static final Pose BLUE_HIVE = new Pose (72,84);
    public static Pose getHive(Alliance alliance){
        return alliance == Alliance.RED ? RED_HIVE : BLUE_HIVE;
    }
    public static final boolean LIMIT_TRAVEL = false;
    public static final double MIN_ANGLE_RAD = Math.toRadians(-170);
    public static final double MAX_ANGLE_RAD = Math.toRadians(170);
    public static final double D_FILTER = 0.7;
    public static final double ON_TARGET_TOL_RAD = Math.toRadians(1.5);
    public static final double kP = 1.2;
    public static final double kD = 0.05;
    public static final double kS = 0.04;
    public static final double kV_HEADING = 0;
    public static final double PRELOAD = 0.06;
}
