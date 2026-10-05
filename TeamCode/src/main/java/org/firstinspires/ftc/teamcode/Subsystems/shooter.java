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
    private double setmotorpower = 1;
    public shooter(HardwareMap HardwareMap){

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
