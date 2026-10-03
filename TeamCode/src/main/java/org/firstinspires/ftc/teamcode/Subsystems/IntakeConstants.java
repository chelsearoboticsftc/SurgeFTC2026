package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class IntakeConstants {
    //Add subsystem constants here.  Use this to avoid magic numbers
    public static final DcMotor.ZeroPowerBehavior INTAKE_ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;
    public static final DcMotorSimple.Direction INTAKE_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final DcMotor.ZeroPowerBehavior INDEX_ZERO_POWER_BEHAVIOR = DcMotor.ZeroPowerBehavior.BRAKE;
    public static final DcMotorSimple.Direction INDEX_DIRECTION = DcMotorSimple.Direction.FORWARD;
}
