
package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.*;

public class Turret {

    private CRServo turretA;
    private CRServo turretB;

    private DcMotorEx turretEncoder;

    private final ElapsedTime timer = new ElapsedTime();

    // =====================================
    // STATE
    // =====================================

    private double targetRad = 0;

    private double encoderZero = 0;

    private double lastAngle = 0;

    private double filteredVelocity = 0;

    private double ffRate = 0;

    private double lastU = 0;

    private double lastError = Math.PI;

    private boolean targetReachable = true;

    private int currentGoal = -1;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Turret(HardwareMap hardwareMap) {
        this(hardwareMap, true);
    }

    public Turret(HardwareMap hardwareMap, boolean resetEncoder) {

        turretA = hardwareMap.get(
                CRServo.class,
                SERVO_A
        );

        turretB = hardwareMap.get(
                CRServo.class,
                SERVO_B
        );

        turretEncoder = hardwareMap.get(
                DcMotorEx.class,
                ENCODER_NAME
        );

        if (resetEncoder) {
            turretEncoder.setMode(
                    DcMotor.RunMode.STOP_AND_RESET_ENCODER
            );
        }

        turretEncoder.setMode(
                DcMotor.RunMode.RUN_WITHOUT_ENCODER
        );

        encoderZero = turretEncoder.getCurrentPosition();

        lastAngle = TurretRadians();

        timer.reset();
    }

    // =====================================
    // ENCODER
    // =====================================

    public double getTurretEncoder() {
        return turretEncoder.getCurrentPosition();
    }

    public double TurretRadians() {

        double counts =
                turretEncoder.getCurrentPosition() - encoderZero;

        double radians =
                (counts / ENCODER_CPR)
                        * (2.0 * Math.PI / GEAR_RATIO);

        return ENCODER_REVERSED ? -radians : radians;
    }

    public double getAngleRadians() {
        return wrap(TurretRadians());
    }

    // Call when turret is physically facing forward.
    public void calibrateTurretZero() {

        encoderZero = turretEncoder.getCurrentPosition();

        lastAngle = 0;

        filteredVelocity = 0;
    }

    // =====================================
    // FIELD POSITION
    // =====================================

    public static double[] TurretFieldXY(Pose robot) {

        double h = robot.heading();

        double x =
                robot.x()
                        + OFFSET_X * Math.cos(h)
                        - OFFSET_Y * Math.sin(h);

        double y =
                robot.y()
                        + OFFSET_X * Math.sin(h)
                        + OFFSET_Y * Math.cos(h);

        return new double[]{x, y};
    }

    // =====================================
    // TARGET CALCULATIONS
    // =====================================

    public static double angleToTarget(
            Pose robot,
            Pose target
    ) {

        double[] p = TurretFieldXY(robot);

        double dx = target.x() - p[0];

        double dy = target.y() - p[1];

        double fieldAngle = Math.atan2(dy, dx);

        // Convert field angle to robot-relative angle.
        return wrap(fieldAngle - robot.heading());
    }

    public static double distanceToTarget(
            Pose robot,
            Pose target
    ) {

        double[] p = TurretFieldXY(robot);

        return Math.hypot(
                target.x() - p[0],
                target.y() - p[1]
        );
    }

    // =====================================
    // AIM
    // =====================================

    public void aim(
            Pose robot,
            Pose target,
            double headingRate
    ) {

        aim(robot, target, headingRate, 0, 0);
    }

    public void aim(
            Pose robot,
            Pose target,
            double headingRate,
            double vx,
            double vy
    ) {

        // Calculate robot-relative target angle.
        targetRad = angleToTarget(robot, target);

        double[] p = TurretFieldXY(robot);

        double rx = target.x() - p[0];

        double ry = target.y() - p[1];

        double r2 = Math.max(
                rx * rx + ry * ry,
                1.0
        );

        // Target bearing velocity.
        // Assumes vx and vy are field-relative.
        double bearingRate =
                (ry * vx - rx * vy) / r2;

        // Account for robot rotation.
        ffRate = bearingRate - headingRate;

        run();
    }

    public void holdAngle(double robotRelativeRadians) {

        targetRad = wrap(robotRelativeRadians);

        ffRate = 0;

        run();
    }

    // =====================================
    // AUTOMATIC GOAL SELECTION
    // =====================================

    public Pose pickGoal(
            Pose robot,
            Alliance alliance
    ) {

        Pose[] goals = getGoals(alliance);

        double y = TurretFieldXY(robot)[1];

        if (currentGoal < 0) {

            currentGoal =
                    y < GOAL_SWITCH_Y ? 0 : 1;

        } else if (
                currentGoal == 0 &&
                        y > GOAL_SWITCH_Y + GOAL_SWITCH_MARGIN
        ) {

            currentGoal = 1;

        } else if (
                currentGoal == 1 &&
                        y < GOAL_SWITCH_Y - GOAL_SWITCH_MARGIN
        ) {

            currentGoal = 0;
        }

        return goals[currentGoal];
    }

    public void aimAtGoal(
            Pose robot,
            double headingRate,
            double vx,
            double vy,
            Alliance alliance
    ) {

        Pose goal = pickGoal(robot, alliance);

        aim(
                robot,
                goal,
                headingRate,
                vx,
                vy
        );
    }

    // =====================================
    // PID CONTROL
    // =====================================

    private void run() {

        double dt = Math.max(
                0.001,
                Math.min(timer.seconds(), 0.05)
        );

        timer.reset();

        double angle = TurretRadians();

        double target = chooseTarget(
                targetRad,
                angle
        );

        double error = target - angle;

        lastError = Math.abs(error);

        // Measure turret angular velocity.
        double measuredVelocity =
                (angle - lastAngle) / dt;

        lastAngle = angle;

        // Filter encoder velocity.
        filteredVelocity =
                D_FILTER * filteredVelocity
                        + (1.0 - D_FILTER) * measuredVelocity;

        double u = 0;

        if (Math.abs(error) > HOLD_TOL_RAD) {

            double p = kP * error;

            // Derivative on measurement.
            double d = -kD * filteredVelocity;

            // Static friction compensation.
            double s = kS * Math.signum(error);

            // Robot motion compensation.
            double ff = kV_HEADING * ffRate;

            u = p + d + s + ff;
        }

        u = clamp(
                u,
                -MAX_POWER,
                MAX_POWER
        );

        // Limit sudden power changes.
        double maxChange = MAX_ACCEL * dt;

        u = clamp(
                u,
                lastU - maxChange,
                lastU + maxChange
        );

        lastU = u;

        setTurretPower(u);
    }

    // =====================================
    // CABLE LIMITS
    // =====================================

    private double chooseTarget(
            double wrappedTarget,
            double angle
    ) {

        double best = Double.NaN;

        double bestDistance = Double.MAX_VALUE;

        for (int k = -1; k <= 1; k++) {

            double candidate =
                    wrappedTarget + k * 2 * Math.PI;

            if (Math.abs(candidate) > CABLE_LIMIT_RAD) {
                continue;
            }

            double distance =
                    Math.abs(candidate - angle);

            if (distance < bestDistance) {

                bestDistance = distance;

                best = candidate;
            }
        }

        targetReachable = !Double.isNaN(best);

        if (!targetReachable) {

            return clamp(
                    wrappedTarget,
                    -CABLE_LIMIT_RAD,
                    CABLE_LIMIT_RAD
            );
        }

        return best;
    }

    // =====================================
    // SERVO OUTPUT
    // =====================================

    private void setTurretPower(double power) {

        turretA.setPower(power);

        turretB.setPower(
                SERVO_B_REVERSED ? -power : power
        );
    }

    public void turnTurret(double power) {

        power = clamp(power, -MAX_POWER, MAX_POWER);

        setTurretPower(power);

        lastU = power;
    }

    public void stop() {

        setTurretPower(0);

        lastU = 0;
    }

    // =====================================
    // TELEMETRY GETTERS
    // =====================================

    public double getTargetRad() {
        return targetRad;
    }

    public double getLastU() {
        return lastU;
    }

    public double getErrorRadians() {
        return lastError;
    }

    public double getVelocity() {
        return filteredVelocity;
    }

    public double getFeedforwardRate() {
        return ffRate;
    }

    public boolean isOnTarget() {
        return targetReachable &&
                lastError < ON_TARGET_TOL_RAD;
    }

    public boolean isTargetReachable() {
        return targetReachable;
    }

    // =====================================
    // UTILITIES
    // =====================================

    public static double wrap(double rad) {

        while (rad >= Math.PI) {
            rad -= 2 * Math.PI;
        }

        while (rad < -Math.PI) {
            rad += 2 * Math.PI;
        }

        return rad;
    }

    public static double clamp(
            double value,
            double min,
            double max
    ) {

        return Math.max(
                min,
                Math.min(max, value)
        );
    }
}
