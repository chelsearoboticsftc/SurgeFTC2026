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
    // Shooter in reverse to clear a jam (same as the match TeleOp's gamepad2 A)
    private static final double UNJAM_VELOCITY = -900;

    private boolean rightTriggerWasDown = false;    // so we can tell when the trigger is first pressed or let go

    private final ElapsedTime spinUpTimer = new ElapsedTime();
    private boolean measuringSpinUp = false;        // true from GO until the shooter first reaches speed
    private String spinUpResult = "press GO";       // what the Driver Station shows for spin-up

    private double lowestVelocity = 0;              // like a high score: keep the record
    private double highestVelocity = 0;

    private VoltageSensor battery;
    private double batteryAtStart = 0;                 // battery voltage when the last test started

    // ===== PIDF tuning (gamepad2, only while holding BACK) =====
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
        batteryAtStart = battery.getVoltage();
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

        // ===== gamepad2: same controls as the match TeleOp, plus the measuring tool =====
        // Read the shooter once per loop, and use these same answers everywhere below.
        double actualVelocity = shooter.getVelocity();
        boolean atSpeed = shooter.isAtSpeed();

        // Right trigger: HOLD to spin up, like the match TeleOp. Pressing it also starts a test.
        boolean rightTriggerDown = gamepad2.right_trigger > 0;
        if (rightTriggerDown && !rightTriggerWasDown) {
            startShooterTest(actualVelocity);   // the trigger was just pressed
        }
        if (!rightTriggerDown && rightTriggerWasDown) {
            stopShooter();                      // the trigger was just let go
        }
        rightTriggerWasDown = rightTriggerDown;

        // dpad up / down: GO / STOP without holding anything (for longer tests, like wobble)
        if (gamepad2.dpadUpWasPressed()) {
            startShooterTest(actualVelocity);
        }
        if (gamepad2.dpadDownWasPressed()) {
            stopShooter();
        }

        // A: HOLD to run the shooter in reverse and clear a jam, like the match TeleOp
        if (gamepad2.aWasPressed()) {
            shooter.SetShooterVelocity(UNJAM_VELOCITY);
        }
        if (gamepad2.aWasReleased()) {
            stopShooter();
        }

        // If the target changes mid-test (gamepad1 +/- or unjam), the spin-up time is no good
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

        // dpad right: start a fresh Lowest/Highest record by hand
        if (gamepad2.dpadRightWasPressed()) {
            lowestVelocity = actualVelocity;
            highestVelocity = actualVelocity;
        }

        // Keep the record only while the shooter is supposed to be spinning forward
        if (shooter.getTargetVelocity() > 0) {
            lowestVelocity = Math.min(lowestVelocity, actualVelocity);
            highestVelocity = Math.max(highestVelocity, actualVelocity);
        }

        // Gate, index, and intake: same as the match TeleOp
        // HOLD left trigger = feed, HOLD B = gate in reverse, HOLD left bumper = index in reverse
        if (gamepad2.left_trigger > 0) {
            shooter.gateSetPower(1);
            intake.setIndexPower(1);
            intake.setMotorPower(1);
        } else if (gamepad2.b) {
            shooter.gateSetPower(-1);
        } else {
            shooter.gateSetPower(0);
        }
        boolean indexReverse = gamepad2.left_bumper && !gamepad2.back;   // LB means "P down" while tuning
        if (indexReverse) {
            intake.setIndexPower(-1);
        }
        if (gamepad2.left_trigger <= 0 && !indexReverse) {
            intake.setIndexPower(0);
        }
        if (gamepad2.left_trigger <= 0) {
            intake.setMotorPower(0);
        }

        // ===== PIDF tuning - only after the shooting spot has been locked in! =====
        // Safety switch: the tuning buttons only work while BACK is held, so nobody tunes by accident.
        // We check every button every loop (even when not tuning), so an old press can't count later.
        boolean tuning = gamepad2.back;
        boolean fUp = gamepad2.yWasPressed();
        boolean fDown = gamepad2.xWasPressed();
        boolean pUp = gamepad2.rightBumperWasPressed();
        boolean pDown = gamepad2.leftBumperWasPressed();

        boolean pidfChanged = false;
        if (tuning && fUp) {
            kF = kF + F_STEP;
            pidfChanged = true;
        }
        if (tuning && fDown) {
            kF = Math.max(0, kF - F_STEP);
            pidfChanged = true;
        }
        if (tuning && pUp) {
            kP = kP + P_STEP;
            pidfChanged = true;
        }
        if (tuning && pDown) {
            kP = Math.max(0, kP - P_STEP);
            pidfChanged = true;
        }
        if (pidfChanged) {
            // Takes effect immediately. I and D stay as they are in shooterConstants.
            shooter.setPIDF(kP, shooterConstants.SHOOTER_MOTOR_VELOCITY_I,
                    shooterConstants.SHOOTER_MOTOR_VELOCITY_D, kF);
        }

        telemetry.addLine("GP2 (like match): hold RT = spin, hold LT = feed, hold A/B/LB = reverse");
        telemetry.addLine("GP2 test: dpad up = GO, dpad down = STOP, dpad right = reset Lowest/Highest");
        telemetry.addLine("GP2 tune: hold BACK + Y/X = F +/-, RB/LB = P +/-");
        telemetry.addData("Target", "%.0f", shooter.getTargetVelocity());
        telemetry.addData("Actual", "%.0f", actualVelocity);
        telemetry.addData("At speed?", atSpeed);
        telemetry.addData("Spin-up", spinUpResult);
        telemetry.addData("Lowest / Highest", "%.0f / %.0f", lowestVelocity, highestVelocity);
        telemetry.addData("P / F", "%.2f / %.2f   (copy into shooterConstants when done!)", kP, kF);
        telemetry.addData("Battery at start", "%.1f V", batteryAtStart);

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

    // Spin up to the test speed. If the shooter is stopped, also start the spin-up stopwatch.
    private void startShooterTest(double actualVelocity) {
        shooter.SetShooterVelocity(TEST_VELOCITY);
        batteryAtStart = battery.getVoltage();
        if (actualVelocity < STOPPED_VELOCITY) {
            spinUpTimer.reset();
            measuringSpinUp = true;
            spinUpResult = "measuring...";
        } else {
            // A fair spin-up test has to start from a stopped shooter
            spinUpResult = "not measured: STOP and wait for Actual = 0";
        }
    }

    // Stop the shooter (the last spin-up result stays on screen so you can write it down)
    private void stopShooter() {
        shooter.SetShooterVelocity(0);
        measuringSpinUp = false;
    }
}
