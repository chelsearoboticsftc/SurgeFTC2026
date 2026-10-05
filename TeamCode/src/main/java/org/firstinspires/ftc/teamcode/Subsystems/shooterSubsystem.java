package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class shooterSubsystem {
    DcMotorEx shooterMotor;

    private int MotorSetVelocity = 1;
    private double setmotorpower = 1;
    public shooterSubsystem (HardwareMap HardwareMap){
        SampleSubsystemConstants.MOTOR_NAME_VELOCITY_P, 
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_I,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_D,
                SampleSubsystemConstants.MOTOR_NAME_VELOCITY_F);
    }
}