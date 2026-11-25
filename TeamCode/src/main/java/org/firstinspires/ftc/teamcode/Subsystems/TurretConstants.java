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

    // Flip a servo's position (1 - pos) if it moves the wrong way.
    // Both servos should push the turret the same direction.
    public static final boolean SERVO_A_REVERSED = false;
    public static final boolean SERVO_B_REVERSED = false;

    // Set true if increasing turret angle (CCW) needs LOWER servo position.
    public static final boolean TURRET_REVERSED = false;

    // =====================================
    // SERVO RANGE
    // =====================================

    // REV Smart Robot Servo in servo mode: 500-2500 us = 270 degrees.
    public static final int SERVO_PWM_MIN_US = 500;
    public static final int SERVO_PWM_MAX_US = 2500;

    // Total turret rotation for the full 0..1 servo range.
    // (270 deg servo * 4 / 2.75 = ~392.7 deg)
    public static final double TURRET_TRAVEL_RAD =
            Math.toRadians(393);

    // Turret angle when the servos are at position 0.5.
    // Adjust if the turret doesn't face forward at center.
    public static final double ZERO_OFFSET_RAD = 0;

    // Stay slightly off the physical end stops.
    public static final double MAX_TURRET_ANGLE_RAD =
            TURRET_TRAVEL_RAD / 2.0 - Math.toRadians(2);

    // =====================================
    // TRACKING
    // =====================================

    // Aim ahead by this many seconds of target motion (0 = off).
    public static final double LEAD_TIME_SEC = 0.0;

    // Estimated turret speed, used only for isOnTarget().
    // Placeholder, so measure it on the robot and update.
    public static final double SERVO_SPEED_RAD_PER_SEC = 8.0;

    // Manual jog speed at full stick.
    public static final double JOG_SPEED_RAD_PER_SEC =
            Math.toRadians(120);

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