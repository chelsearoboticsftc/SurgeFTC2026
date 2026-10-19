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

@TeleOp(name = "Field Centric TeleOp")
public class TeleOpFieldCentric extends OpMode {
    private Intake intake;
    private shooter shooter;
    private Limelight limelight;
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
        if (gamepad1.left_trigger > 0){
            telemetry.addLine("intake working");
            intake.setMotorPower(1);
        }
        if (gamepad1.right_trigger > 0){
            telemetry.addLine("intake working");
            intake.setMotorPower(-1);
        }
        if (gamepad1.right_trigger == 0 && gamepad1.left_trigger == 0){
            intake.setMotorPower(0);}

        if (gamepad2.left_trigger > 0) {


            intake.setIndexPower(1);
        }
        if (gamepad2.left_trigger == 0){
            intake.setIndexPower(0);
        }
        if (gamepad2.left_bumper) {
            intake.setIndexPower(-1);
        }
       // else {
         //   intake.setIndexPower(0);
        //}
        if (gamepad2.right_trigger > 0){
            shooter.SetShooterVelocity(1500);
        }
        else {
            shooter.SetShooterVelocity(0);
        }


        if (gamepad2.yWasPressed()) {

            intake.intakeToggle();
        }
        if (gamepad2.b) {
            shooter.GateToggleUp();
        }
        else {
            shooter.GateToggleDown();
        }
        if (gamepad1.aWasPressed()) {
            flower.flowerToggleR();
            intake.intakeToggle();
        }
        //if (gamepad1.xWasPressed()) {
          //  flower.flowerToggleL();
       // }
//        if (gamepad2.dpad_up) {
//            flower.resetFlowerR();
//        }
//        if (gamepad2.dpad_right) {
//            flower.resetFlowerL();
//        }
//        if (gamepad2.dpad_down) {
//            intake.intakeServoReset();
//        }

        limelight.update();
        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.update();
    }

}
