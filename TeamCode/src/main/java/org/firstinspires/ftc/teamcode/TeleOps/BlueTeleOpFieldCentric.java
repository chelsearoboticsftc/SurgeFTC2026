package org.firstinspires.ftc.teamcode.TeleOps;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Flower;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.Subsystems.TurretConstants;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

@TeleOp(name = "Field Centric TeleOp (Blue)")
public class BlueTeleOpFieldCentric extends OpMode {
    private Intake intake;
    private shooter shooter;
    private Limelight limelight;
    private Flower flower;
    private Turret turret;
    private boolean intakeToggleVar = true;

    private Follower follower;
    @Override
    public void start(){
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.setHeading(Math.toRadians(180));
        follower.update();
        intake.setIntakeServoL(0.55);
    }
    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new shooter(hardwareMap);
        limelight = new Limelight(hardwareMap, telemetry);
        flower = new Flower(hardwareMap);
        turret = new Turret(hardwareMap, true);
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

        //Intake controls
        if (gamepad1.left_trigger > 0){
            telemetry.addLine("intake working");
            intake.setMotorPower(1);
            intake.setIndexPower(1);
        }
        else if (gamepad1.right_trigger > 0){
            telemetry.addLine("intake working");
            intake.setMotorPower(-1);
        }
        if (gamepad1.left_trigger <= 0 && gamepad2.left_trigger <= 0 && gamepad1.right_trigger <= 0) {
            intake.setMotorPower(0);
        }

        //Index and gate controls
        if (gamepad2.left_trigger > 0) {
            shooter.gateSetPower(1);
            intake.setIndexPower(1);
            intake.setMotorPower(1);
        }
        else if (gamepad2.b) {
            shooter.gateSetPower(-1);
        }
        else {
            shooter.gateSetPower(0);
        }
        if (gamepad2.left_bumper) {
            intake.setIndexPower(-1);
        }
        if (gamepad2.left_trigger <= 0 && gamepad1.left_trigger <= 0 && !gamepad2.left_bumper) {
            intake.setIndexPower(0);
        }
        if(gamepad1.dpadUpWasPressed()){
            follower.setPose(new Pose(8,8,0));
        }

        //Shooter controls
        if (gamepad2.right_trigger > 0){
            shooter.SetShooterVelocity(1325);
        }
        else if (gamepad2.a) {
            shooter.SetShooterVelocity(-900);
        }
        else {
            shooter.SetShooterVelocity(0);
        }
        if(gamepad1.backWasPressed()){
            intake.intakeToggle();
        }
//        if(gamepad1.xWasPressed()){
//            intake.setIntakeServoL(0.55);
//            intakeToggleVar = true;
//        }
//        if (gamepad1.yWasPressed()) {
//            intake.setIntakeServoL(1);
//            intakeToggleVar = false;
//        }

        //Flower controls
        if (gamepad1.aWasPressed()) {
            flower.flowerToggle(); //Starts up
            if (!intakeToggleVar) {
                intake.setIntakeServoL(0.55); //Down
                intakeToggleVar = true;
            }
            else {
                intake.setIntakeServoL(1); //Up
                intakeToggleVar = false;
            }
        }

        limelight.update();
        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose();
//        turret.aimAtGoal(
//                robotPose,
//
//                follower.velocity().omega,
//
//                follower.velocity().vx,
//
//                follower.velocity().vy,
//
//                TurretConstants.Alliance.RED
//        );// returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        // =====================================
        // TURRET TELEMETRY
        // =====================================

        telemetry.addData(
                "Turret Encoder",
                turret.getTurretEncoder()
        );

        telemetry.addData(
                "Turret Angle",
                Math.toDegrees(turret.getAngleRadians())
        );

        telemetry.addData(
                "Turret Target",
                Math.toDegrees(turret.getTargetRad())
        );

        telemetry.addData(
                "Turret Error",
                Math.toDegrees(turret.getErrorRadians())
        );

        telemetry.addData(
                "Turret Velocity",
                Math.toDegrees(turret.getVelocity())
        );

        telemetry.addData(
                "Turret Feedforward",
                turret.getFeedforwardRate()
        );

        telemetry.addData(
                "Turret Power",
                turret.getLastU()
        );

        telemetry.addData(
                "Turret On Target",
                turret.isOnTarget()
        );

        telemetry.addData(
                "Target Reachable",
                turret.isTargetReachable()
        );
        telemetry.update();

    }

}
