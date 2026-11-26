package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class shooterConstants {
    //Add subsystem constants here.  Use this to avoid magic numbers
    public static final int SHOOTER_MOTOR_THRESHOLD = 5;
    // How close (ticks/s) the real speed must be to the target to count as "at speed".
    // isAtSpeed() uses this one (not SHOOTER_MOTOR_THRESHOLD above, which isn't used anywhere).
    // Starting guess (~3% of 1325). Set it from the wobble you measure in Shooter Value Test.
    public static final double SHOOTER_MOTOR_VELOCITY_TOLERANCE = 40;
    public static final DcMotor.ZeroPowerBehavior SHOOTER_MOTOR_ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;
    public static final DcMotorSimple.Direction SHOOTER_MOTOR_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double SHOOTER_MOTOR_VELOCITY_P = 1.0;
    public static final double SHOOTER_MOTOR_VELOCITY_I = 0.0;
    public static final double SHOOTER_MOTOR_VELOCITY_D = 0.0;
    public static final double SHOOTER_MOTOR_VELOCITY_F = 14.5;
    public static final int SHOOTER_MOTOR_POSITION_TOLERANCE = 5;
    public static final double SHOOTER_MOTOR_VELOCITY_TICKS_PER_S = 2200;
    public static final int SHOOTER_MOTOR_A_POSITION = 500;
    public static final int SHOOTER_MOTOR_B_POSITION = 1000;
    public static double SHOOTER_MOTOR_POSITION_P = 5.0;
}
