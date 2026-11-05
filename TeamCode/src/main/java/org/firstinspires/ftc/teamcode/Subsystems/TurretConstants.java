package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;

public class TurretConstants {
    public enum Alliance{RED,BLUE
    }
    public static boolean ENCODER_REVERSED = false;
    public static final Pose RED_GOAL_A  = new Pose(60, 55);   // red lower
    public static final Pose RED_GOAL_B  = new Pose(60, 85);   // red upper
    public static final Pose BLUE_GOAL_A = new Pose(86, 55);   // blue lower
    public static final Pose BLUE_GOAL_B = new Pose(86, 85);   // blue upper

    public static Pose[] getGoals(Alliance alliance){
        return alliance == Alliance.RED
                ? new Pose[]{RED_GOAL_A, RED_GOAL_B}
                : new Pose[]{BLUE_GOAL_A, BLUE_GOAL_B};
    }

    public static final double GOAL_SWITCH_Y = 72;
    public static final double GOAL_SWITCH_MARGIN = 4;
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
    public static final double OFFSET_X = 0;
    public static final double OFFSET_Y = 0;
}
