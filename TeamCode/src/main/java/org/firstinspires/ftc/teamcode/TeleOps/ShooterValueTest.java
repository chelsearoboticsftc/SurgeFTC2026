package org.firstinspires.ftc.teamcode.TeleOps;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Flower;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

@TeleOp(name = "Shooter Value Test")
public class ShooterValueTest extends OpMode {
    private Intake intake;
    private shooter shooter;
    private Limelight limelight;
    private Flower flower;
    private double rightFlowerPos = 0;
    private double leftFlowerPos = 0;

    private Follower follower;
    private int velocityVar = 0;
    @Override
    public void start(){
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();
        flower.resetFlowerR();
        flower.resetFlowerL();
    }
    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new shooter(hardwareMap);
        limelight = new Limelight(hardwareMap, telemetry);
        flower = new Flower(hardwareMap);
    }
    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                - gamepad1.left_stick_x,
                -
                        gamepad1.right_stick_x,
                follower.pose().heading()
        );
        if (gamepad1.aWasPressed()) {
            shooter.SetShooterVelocity(velocityVar - 100);
            velocityVar = velocityVar - 100;
        }
        if (gamepad1.yWasPressed()) {
            shooter.SetShooterVelocity(velocityVar + 100);
            velocityVar = velocityVar + 100;
        }
        if (gamepad1.bWasPressed()) {
            shooter.SetShooterVelocity(velocityVar + 10);
            velocityVar = velocityVar + 10;
        }
        if (gamepad1.xWasPressed()) {
            shooter.SetShooterVelocity(velocityVar - 10);
            velocityVar = velocityVar - 10;
        }
        if (gamepad1.leftBumperWasPressed()) {
            flower.leftFlowerPosSwap(leftFlowerPos +.1);
            leftFlowerPos = leftFlowerPos + 0.1;
        }
        if (gamepad1.left_trigger > 0) {
            flower.leftFlowerPosSwap(leftFlowerPos - 0.1);
            leftFlowerPos = leftFlowerPos - 0.1;
        }
        if (gamepad1.right_trigger > 0) {
            flower.rightFlowerPosSwap(rightFlowerPos - 0.1);
            rightFlowerPos = rightFlowerPos - 0.1;
        }
        if (gamepad1.rightBumperWasPressed()) {
            flower.rightFlowerPosSwap(rightFlowerPos + 0.1);
            rightFlowerPos = rightFlowerPos + 0.1;
        }

        limelight.update();
        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Left Flower Servo Position", flower.getRightFlowerPos());
        telemetry.addData("Right Flower Servo Position",flower.getLeftFlowerPos());
        telemetry.addData("Shooter Velocity",velocityVar);
        telemetry.update();
    }

}
