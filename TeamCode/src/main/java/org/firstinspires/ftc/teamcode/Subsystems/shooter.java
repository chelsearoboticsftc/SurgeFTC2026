package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class shooter {
    DcMotorEx shooterMotor;

    private int MotorSetVelocity = 1;
    private double setmotorpower = 1;
    public shooter(HardwareMap HardwareMap){

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
    public void update(){

    }
    public void Setmotorpower (){
        setmotorpower = 1;
    }



}