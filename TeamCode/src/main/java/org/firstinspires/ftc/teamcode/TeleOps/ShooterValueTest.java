package org.firstinspires.ftc.teamcode.TeleOps;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.Subsystems.Flower;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.Subsystems.shooter;
import org.firstinspires.ftc.teamcode.Subsystems.shooterConstants;
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

    // ===== Measuring tool (gamepad2) =====
    // The TeleOp shooting speed. (The autos use 1325-1750 depending on distance.)
    private static final double TEST_VELOCITY = 1325;
    // Slower than this (ticks/s) counts as "stopped", so a spin-up test can start.
    private static final double STOPPED_VELOCITY = 50;

    private final ElapsedTime spinUpTimer = new ElapsedTime();
    private boolean measuringSpinUp = false;        // true from GO until the shooter first reaches speed
    private String spinUpResult = "press GO";       // what the Driver Station shows for spin-up

    private double lowestVelocity = 0;              // like a high score: keep the record
    private double highestVelocity = 0;

    private VoltageSensor battery;
    private double batteryAtGo = 0;                 // battery voltage when GO was last pressed

    // ===== PIDF tuning (gamepad2) =====
    private static final double F_STEP = 0.25;
    private static final double P_STEP = 0.5;
    // kP and kF are the P and F numbers from PIDF ("k" = a constant we multiply by).
    // The constants are final, so we adjust copies. Copy the final values, after testing, into shooterConstants!
    private double kP = shooterConstants.SHOOTER_MOTOR_VELOCITY_P;
    private double kF = shooterConstants.SHOOTER_MOTOR_VELOCITY_F;

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
        battery = hardwareMap.voltageSensor.iterator().next();
        batteryAtGo = battery.getVoltage();
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
            shooter.SetShooterVelocity(shooter.getTargetVelocity() - 100);
        }
        if (gamepad1.yWasPressed()) {
            shooter.SetShooterVelocity(shooter.getTargetVelocity() + 100);
        }
        if (gamepad1.bWasPressed()) {
            shooter.SetShooterVelocity(shooter.getTargetVelocity() + 10);
        }
        if (gamepad1.xWasPressed()) {
            shooter.SetShooterVelocity(shooter.getTargetVelocity() - 10);
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

        // ===== Measuring tool (gamepad2) =====
        // Read the shooter once per loop, and use these same answers everywhere below.
        double actualVelocity = shooter.getVelocity();
        boolean atSpeed = shooter.isAtSpeed();

        // GO: jump straight to the test speed and start the spin-up stopwatch
        if (gamepad2.dpadUpWasPressed()) {
            shooter.SetShooterVelocity(TEST_VELOCITY);
            batteryAtGo = battery.getVoltage();
            if (actualVelocity < STOPPED_VELOCITY) {
                spinUpTimer.reset();
                measuringSpinUp = true;
                spinUpResult = "measuring...";
            } else {
                // A fair spin-up test has to start from a stopped shooter
                spinUpResult = "not measured: STOP and wait for Actual = 0";
            }
        }

        // STOP (the last spin-up result stays on screen so you can write it down)
        if (gamepad2.dpadDownWasPressed()) {
            shooter.SetShooterVelocity(0);
            measuringSpinUp = false;
        }

        // If someone changes the target with gamepad1 mid-test, the spin-up time is no good
        if (measuringSpinUp && shooter.getTargetVelocity() != TEST_VELOCITY) {
            measuringSpinUp = false;
            spinUpResult = "cancelled: target changed";
        }

        // Spin-up: the first moment the shooter is at speed after GO
        if (measuringSpinUp && atSpeed) {
            spinUpResult = String.format("%.0f ms", spinUpTimer.milliseconds());
            measuringSpinUp = false;
            // Now that we're at speed, start a fresh Lowest/Highest record
            lowestVelocity = actualVelocity;
            highestVelocity = actualVelocity;
        }

        // B: start a fresh Lowest/Highest record by hand
        if (gamepad2.bWasPressed()) {
            lowestVelocity = actualVelocity;
            highestVelocity = actualVelocity;
        }

        // Keep the record only while the shooter is supposed to be spinning
        if (shooter.getTargetVelocity() > 0) {
            lowestVelocity = Math.min(lowestVelocity, actualVelocity);
            highestVelocity = Math.max(highestVelocity, actualVelocity);
        }

        // Feed: HOLD gamepad2 left trigger, the same control as the real TeleOp
        if (gamepad2.left_trigger > 0) {
            shooter.gateSetPower(1);
            intake.setIndexPower(1);
            intake.setMotorPower(1);
        } else {
            shooter.gateSetPower(0);
            intake.setIndexPower(0);
            intake.setMotorPower(0);
        }

        // ===== PIDF tuning (gamepad2) - only after the shooting spot has been locked in! =====
        boolean pidfChanged = false;
        if (gamepad2.yWasPressed()) {
            kF = kF + F_STEP;
            pidfChanged = true;
        }
        if (gamepad2.xWasPressed()) {
            kF = Math.max(0, kF - F_STEP);
            pidfChanged = true;
        }
        if (gamepad2.rightBumperWasPressed()) {
            kP = kP + P_STEP;
            pidfChanged = true;
        }
        if (gamepad2.leftBumperWasPressed()) {
            kP = Math.max(0, kP - P_STEP);
            pidfChanged = true;
        }
        if (pidfChanged) {
            // Takes effect immediately. I and D stay as they are in shooterConstants.
            shooter.setPIDF(kP, shooterConstants.SHOOTER_MOTOR_VELOCITY_I,
                    shooterConstants.SHOOTER_MOTOR_VELOCITY_D, kF);
        }

        telemetry.addLine("GP2: dpad up = GO   dpad down = STOP   hold LT = feed");
        telemetry.addLine("GP2: B = reset Lowest/Highest   Y/X = F +/-   RB/LB = P +/-");
        telemetry.addData("Target", "%.0f", shooter.getTargetVelocity());
        telemetry.addData("Actual", "%.0f", actualVelocity);
        telemetry.addData("At speed?", atSpeed);
        telemetry.addData("Spin-up", spinUpResult);
        telemetry.addData("Lowest / Highest", "%.0f / %.0f", lowestVelocity, highestVelocity);
        telemetry.addData("P / F", "%.2f / %.2f   (copy into shooterConstants when done!)", kP, kF);
        telemetry.addData("Battery at GO", "%.1f V", batteryAtGo);

        limelight.update();
        follower.manual(powers);
        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        // Same as the match TeleOp, so you can record shooting spots from this OpMode too
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        // TODO BUG HUNT: something is wrong with the next two lines.
        // fix them, then remove these comments
        telemetry.addData("Left Flower Servo Position", flower.getRightFlowerPos());
        telemetry.addData("Right Flower Servo Position",flower.getLeftFlowerPos());
        telemetry.update();
    }

}
