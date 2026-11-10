package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;

public class TurretConstants {
    public enum Alliance{RED,BLUE
    }
    public static boolean ENCODER_REVERSED = true;
    private static final double RED_GOAL_X = 60*0.9826 - 3;
    public static final Pose RED_GOAL_A  = new Pose(RED_GOAL_X, 55*0.9826);
    public static final Pose RED_GOAL_B  = new Pose(RED_GOAL_X, 85*0.9826);
    public static final Pose BLUE_GOAL_A = new Pose(141.5 - RED_GOAL_X, 55*0.9826);
    public static final Pose BLUE_GOAL_B = new Pose(141.5 - RED_GOAL_X, 85*0.9826);

    public static Pose[] getGoals(Alliance alliance){
        return alliance == Alliance.RED
                ? new Pose[]{RED_GOAL_A, RED_GOAL_B}
                : new Pose[]{BLUE_GOAL_A, BLUE_GOAL_B};
    }

    public static final double GOAL_SWITCH_Y = 68.78;
    public static final double GOAL_SWITCH_MARGIN = 4;
    public static final boolean LIMIT_TRAVEL = false;
    public static final double MIN_ANGLE_RAD = Math.toRadians(-170);
    public static final double MAX_ANGLE_RAD = Math.toRadians(170);
    public static final double D_FILTER = 0.7;
    public static final double ON_TARGET_TOL_RAD = Math.toRadians(2);
    public static final double kP = .8;
    public static final double kD = 0.1;
    public static final double kS = .09;
    public static final double kV_HEADING = 0.15 ;
    public static final double PRELOAD = 0.03;
    public static final double OFFSET_X = 0;
    public static final double OFFSET_Y = 0;
    public static final double CABLE_LIMIT_RAD = Math.toRadians(270);
}
