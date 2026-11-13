package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeConstants {
    //Add subsystem constants here.  Use this to avoid magic numbers
    public static final DcMotor.ZeroPowerBehavior INTAKE_ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;
    public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static final DcMotor.ZeroPowerBehavior INDEX_ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;
    public static final DcMotorSimple.Direction INDEX_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final double intakeUp = 0.5;
    public static final double intakeDown = 0.7;
    public static final double intakeUp2 = 0.9;
    public static final double intakeDown2 = 0.15;
    public static boolean intakeToggleVar = false;
}
