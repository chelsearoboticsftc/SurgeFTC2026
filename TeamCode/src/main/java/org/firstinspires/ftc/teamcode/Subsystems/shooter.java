package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class shooter {
    DcMotorEx shooterMotor;
    Servo gateServo;

    private double GateUp = shooterConstants.GateUp;
    private int GateDown = shooterConstants.GateDown;
    private double MotorSetPower = 1;
    public shooter(HardwareMap hardwareMap) {

        this.shooterMotor = hardwareMap.get(DcMotorEx.class, "shooterMotor");
        this.gateServo = hardwareMap.get(Servo.class,"gateServo");

        shooterMotor.setZeroPowerBehavior(shooterConstants.SHOOTER_MOTOR_ZERO_POWER_BEHAVIOR);

        shooterMotor.setDirection(shooterConstants.SHOOTER_MOTOR_DIRECTION);

        shooterMotor.setVelocityPIDFCoefficients(

                shooterConstants.SHOOTER_MOTOR_VELOCITY_P,
                shooterConstants.SHOOTER_MOTOR_VELOCITY_I,
                shooterConstants.SHOOTER_MOTOR_VELOCITY_D,
                shooterConstants.SHOOTER_MOTOR_VELOCITY_F);
    }
        public void SetShooterVelocity(double velocity) {
            shooterMotor.setVelocity(velocity);
        }

       public void init(){

    }
    public void GateToggleDown() {
        gateServo.setPosition(GateDown);
    }
    public void GateToggleUp() {
        gateServo.setPosition(GateUp);
    }
}
