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

@TeleOp(name = "Field Centric TeleOp")
public class TeleOpFieldCentric extends OpMode {
    private Intake intake;
    private shooter shooter;
    private Limelight limelight;
    private Flower flower;
    private Turret turret;

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
        else if (gamepad2.left_bumper) {
            intake.setIndexPower(-1);
        }
        else {
            shooter.gateSetPower(0);
        }
        if (gamepad2.left_trigger <= 0 && gamepad1.left_trigger <= 0 && !gamepad2.left_bumper) {
            intake.setIndexPower(0);
        }

        //Shooter controls
        if (gamepad2.right_trigger > 0){
            shooter.SetShooterVelocity(1400);
        }
        else {
            shooter.SetShooterVelocity(0);
        }

        //Flower controls
        if (gamepad1.aWasPressed()) {
            flower.flowerToggleL();
        }
        if (gamepad1.bWasPressed()) {
            flower.flowerToggleR();
        }

        limelight.update();
        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("TurretEncoder", turret.getTurretEncoder());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.addData("Turret deg", Math.toDegrees(turret.getAngleRadians()));
        telemetry.addData("Target deg", Math.toDegrees(turret.getTargetRad()));
        telemetry.addData("Error deg", Math.toDegrees(Turret.wrap(turret.getTargetRad() - turret.getAngleRadians())));
        telemetry.addData("On target", turret.isOnTarget());
        telemetry.addData("Turret continuous deg", Math.toDegrees(turret.TurretRadians()));
        telemetry.addData("Cable limit deg", Math.toDegrees(TurretConstants.CABLE_LIMIT_RAD));
        telemetry.update();
        turret.aimAtGoal(robotPose, follower.velocity().omega, TurretConstants.Alliance.RED);
    }

}
