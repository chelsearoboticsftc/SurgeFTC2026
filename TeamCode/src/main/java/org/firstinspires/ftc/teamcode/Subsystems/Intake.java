package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Intake {

    //Declare HW objects here

    //Example declare a DcMotorEx object as part of this class called 'motorName'
    DcMotorEx intake;
    DcMotorEx index;

    private int intakeSetPosition = 0;
    private double intakePower = 0;

    public Intake(HardwareMap hardwareMap) {
        this.intake = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        intake.setZeroPowerBehavior(IntakeConstants.INTAKE_ZERO_POWER_BEHAVIOR);
        intake.setDirection(IntakeConstants.INTAKE_DIRECTION);
        this.index = hardwareMap.get(DcMotorEx.class,"indexMotor");
        index.setZeroPowerBehavior(IntakeConstants.INDEX_ZERO_POWER_BEHAVIOR);
        index.setDirection(IntakeConstants.INDEX_DIRECTION);
    }

    public void setMotorPower(double power) {
        intake.setPower(power);
    }
    public void setIndexPower(double power) {
        index.setPower(power);
    }
}
