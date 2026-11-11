
package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;

public class TurretConstants {

    public enum Alliance {
        RED,
        BLUE
    }

    // =====================================
    // HARDWARE NAMES
    // =====================================

    public static final String SERVO_A = "TurretA";
    public static final String SERVO_B = "TurretB";
    public static final String ENCODER_NAME = "intakeMotor";

    // Set true if servo B needs opposite power.
    public static final boolean SERVO_B_REVERSED = false;

    // =====================================
    // ENCODER
    // =====================================

    public static final double ENCODER_CPR = 8192.0;

    // Encoder rotates 2.75 times per turret rotation.
    public static final double GEAR_RATIO = 2.75;

    public static final boolean ENCODER_REVERSED = true;

    // =====================================
    // PID
    // =====================================

    public static final double kP = 1.0;
    public static final double kD = 0.10;
    public static final double kS = 0.06;
    public static final double kV_HEADING = 0.15;

    // =====================================
    // FILTERING
    // =====================================

    public static final double D_FILTER = 0.75;

    // =====================================
    // POWER LIMITS
    // =====================================

    public static final double MAX_POWER = 0.9;
    public static final double MAX_ACCEL = 4.0;


    // =====================================
    // ANGLE LIMITS
    // =====================================

    public static final double CABLE_LIMIT_RAD =
            Math.toRadians(270);

    public static final double HOLD_TOL_RAD =
            Math.toRadians(0.5);

    public static final double ON_TARGET_TOL_RAD =
            Math.toRadians(2);

    // =====================================
    // TURRET OFFSET
    // =====================================

    // Relative to the robot localization center.
    public static final double OFFSET_X = 0;
    public static final double OFFSET_Y = 0;

    // =====================================
    // GOAL LOCATIONS
    // =====================================

    private static final double RED_GOAL_X =
            60 * 0.9826 - 3;

    public static final Pose RED_GOAL_A =
            new Pose(RED_GOAL_X, 55 * 0.9826);

    public static final Pose RED_GOAL_B =
            new Pose(RED_GOAL_X, 85 * 0.9826);

    public static final Pose BLUE_GOAL_A =
            new Pose(141.5 - RED_GOAL_X, 55 * 0.9826);

    public static final Pose BLUE_GOAL_B =
            new Pose(141.5 - RED_GOAL_X, 85 * 0.9826);

    public static Pose[] getGoals(Alliance alliance) {

        return alliance == Alliance.RED
                ? new Pose[]{RED_GOAL_A, RED_GOAL_B}
                : new Pose[]{BLUE_GOAL_A, BLUE_GOAL_B};
    }

    // =====================================
    // AUTOMATIC GOAL SWITCHING
    // =====================================

    public static final double GOAL_SWITCH_Y = 68.78;

    public static final double GOAL_SWITCH_MARGIN = 4;

}
