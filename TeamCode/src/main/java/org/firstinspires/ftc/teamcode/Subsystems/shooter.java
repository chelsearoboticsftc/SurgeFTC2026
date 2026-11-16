package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class shooter {
    DcMotorEx shooterMotor;
    DcMotorEx gate;
    // The speed we last asked for, so other code can compare it with the real speed.
    private double targetVelocity = 0;

    public shooter(HardwareMap hardwareMap) {

        this.shooterMotor = hardwareMap.get(DcMotorEx.class, "shooterMotor");
        this.gate = hardwareMap.get(DcMotorEx.class,"gateMotor");

        shooterMotor.setZeroPowerBehavior(shooterConstants.SHOOTER_MOTOR_ZERO_POWER_BEHAVIOR);

        shooterMotor.setDirection(shooterConstants.SHOOTER_MOTOR_DIRECTION);

        shooterMotor.setVelocityPIDFCoefficients(

                shooterConstants.SHOOTER_MOTOR_VELOCITY_P,
                shooterConstants.SHOOTER_MOTOR_VELOCITY_I,
                shooterConstants.SHOOTER_MOTOR_VELOCITY_D,
                shooterConstants.SHOOTER_MOTOR_VELOCITY_F);
    }

    public void SetShooterVelocity(double velocity) {
        targetVelocity = velocity;
        shooterMotor.setVelocity(velocity);
    }

    /** The shooter's real speed right now, in encoder ticks per second. */
    public double getVelocity() {
        return shooterMotor.getVelocity();
    }

    /** The speed we asked for with SetShooterVelocity. */
    public double getTargetVelocity() {
        return targetVelocity;
    }

    public boolean isAtSpeed() {
        return getTargetVelocity() > 0
        && Math.abs(getVelocity() - getTargetVelocity()) <= shooterConstants.SHOOTER_MOTOR_VELOCITY_TOLERANCE;

    }

    /** Change the velocity PIDF values while running (for tuning in Shooter Value Test). */
    public void setPIDF(double p, double i, double d, double f) {
        shooterMotor.setVelocityPIDFCoefficients(p, i, d, f);
    }

    public void init() {

    }
    public double getShooterVelocity() {
        return shooterMotor.getVelocity();
    }
    public void gateSetPower(double power) {
        gate.setPower(power);
    }

}
