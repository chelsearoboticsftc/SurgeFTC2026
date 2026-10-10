package org.firstinspires.ftc.teamcode.TeleOps;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Flower;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

@TeleOp(name = "Field Cenric TeleOp")
public class TeleOpFieldCentric extends OpMode {
    private Intake intake;
    private shooter shooter;
    private Flower flower;

    private Follower follower;
    @Override
    public void start(){
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();
    }
    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new shooter(hardwareMap);
        flower = new Flower(hardwareMap);
    }
    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );
        if (gamepad1.left_trigger > 0){
            intake.setMotorPower(1);
        }
        else {
            intake.setMotorPower(0);
        }
        if (gamepad1.right_bumper) {
            intake.setIndexPower(1);
        }
        else {
            intake.setIndexPower(0);
        }
        if (gamepad1.right_trigger > 0){
            shooter.SetShooterVelocity(1000);
        }
        else {
            shooter.SetShooterVelocity(0);
        }


        if (gamepad1.yWasPressed()) {
            intake.intakeToggle();
        }
        if (gamepad1.b) {
            shooter.GateToggleDown();
        }
        else {
            shooter.GateToggleUp();
        }
        if (gamepad1.aWasPressed()) {
            flower.flowerToggle();
        }

        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }

}
