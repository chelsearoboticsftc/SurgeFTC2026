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
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

@TeleOp(name = "Turret Test (Manual swap?)")
public class manualTurretTest extends OpMode {
    private Intake intake;
    private shooter shooter;
    private Limelight limelight;
    private Flower flower;
    private Turret turret;
    private double rightFlowerPos = 0;
    private double leftFlowerPos = 0;
    private double setTurretPos = gamepad2.left_stick_x;

    private Follower follower;
    private int velocityVar = 0;
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

        if (gamepad2.left_stick_x != 0) {
            turret.turnTurret(setTurretPos);
        }

        if (gamepad2.right_stick_y != 0) {
            shooter.SetShooterVelocity(gamepad2.right_stick_y * 300);
        }

        limelight.update();
        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Left Flower Servo Position", flower.getRightFlowerPos());
        telemetry.addData("Right Flower Servo Position",flower.getLeftFlowerPos());
        telemetry.addData("Shooter Velocity", shooter.getShooterVelocity());
        telemetry.update();
    }

}
