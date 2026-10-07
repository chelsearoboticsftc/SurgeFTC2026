package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.teamcode.Subsystems.shooterConstants.GateDown;
import static org.firstinspires.ftc.teamcode.Subsystems.shooterConstants.GateToggleVar;
import static org.firstinspires.ftc.teamcode.Subsystems.shooterConstants.GateUp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class shooter {
    DcMotorEx shooterMotor;
    Servo gateServo;

    private int MotorSetVelocity = 1;

    private double MotorSetPower = 1;
    public shooter(HardwareMap hardwareMap){

        this.shooterMotor = hardwareMap.get(DcMotorEx.class,"shooterMotor");
        this.gateServo = hardwareMap.get(Servo.class,"gateServo");

        shooterMotor.setZeroPowerBehavior(SampleSubsystemConstants.MOTOR_NAME_ZERO_POWER_BEHAVIOR);

        shooterMotor.setDirection(SampleSubsystemConstants.MOTOR_NAME_DIRECTION);

        shooterMotor.setVelocityPIDFCoefficients(

                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_P,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_I,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_D,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_F);
    }

    public static void GateToggleVar() {
    }

    public void init(){

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
