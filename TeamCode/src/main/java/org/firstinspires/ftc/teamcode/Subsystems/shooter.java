package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class shooter {
    DcMotorEx shooterMotor;
    Servo gateServo;

    private int MotorSetVelocity = 1;
    private int GateUp = 90;
    private int GateDown = 0;
    private boolean GateToggleVar = false;
    private double MotorSetPower = 1;
    public shooter(HardwareMap hardwareMap){

        this.shooterMotor = hardwareMap.get(DcMotorEx.class,"shooterMotor");

        shooterMotor.setZeroPowerBehavior(SampleSubsystemConstants.MOTOR_NAME_ZERO_POWER_BEHAVIOR);

        shooterMotor.setDirection(SampleSubsystemConstants.MOTOR_NAME_DIRECTION);

        shooterMotor.setVelocityPIDFCoefficients(

                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_P,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_I,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_D,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_F);
    }    public void init(){

    }
    public void GateToggle() {
        if (!GateToggleVar) {
            gateServo.setPosition(GateDown);
            GateToggleVar = true;
        }
        else {
            gateServo.setPosition(GateUp);
            GateToggleVar = false;
        }
    }
}
