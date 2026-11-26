package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.Subsystems.TurretConstants.*;

public class Turret {

    private final ServoImplEx turretA;
    private final ServoImplEx turretB;

    private final ElapsedTime timer = new ElapsedTime();

    // =====================================
    // STATE
    // =====================================

    // Where the target is (robot-relative, wrapped).
    private double targetRad = 0;

    // Turret angle we are currently commanding (unwrapped, within limits).
    private double commandRad = 0;

    // Modeled turret angle (servos have no feedback).
    private double estimatedRad = 0;

    private double ffRate = 0;

    private double lastError = Math.PI;

    private boolean targetReachable = true;

    private int currentGoal = -1;

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public Turret(HardwareMap hardwareMap) {

        turretA = hardwareMap.get(ServoImplEx.class, SERVO_A);
        turretB = hardwareMap.get(ServoImplEx.class, SERVO_B);

        // REV Smart Robot Servo uses the full 500-2500 us range.
        PwmControl.PwmRange range =
                new PwmControl.PwmRange(SERVO_PWM_MIN_US, SERVO_PWM_MAX_US);

        turretA.setPwmRange(range);
        turretB.setPwmRange(range);

        // Start facing forward.
        commandRad = 0;
        estimatedRad = 0;
        writeServos(0);

        timer.reset();
    }

    // =====================================
    // ANGLE
    // =====================================

    // Estimated turret angle (unwrapped).
    public double TurretRadians() {
        return estimatedRad;
    }

    public void setTurretPos(double pos){
        turretA.setPosition(pos);
        turretB.setPosition(pos);
    }

    public double getAngleRadians() {
        return wrap(estimatedRad);
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
    // POSITION CONTROL
    // =====================================

    private void run() {

        double dt = tick();

        // Lead the target slightly to hide servo lag.
        double predicted = wrap(targetRad + ffRate * LEAD_TIME_SEC);

        double target = chooseTarget(predicted, commandRad);

        commandRad = target;

        writeServos(commandRad);

        updateEstimate(dt);

        lastError = Math.abs(commandRad - estimatedRad);
    }

    // Manual jog: power -1..1 moves the commanded angle.
    public void turnTurret(double power) {

        double dt = tick();

        power = clamp(power, -1, 1);

        commandRad = clamp(
                commandRad + power * JOG_SPEED_RAD_PER_SEC * dt,
                -MAX_TURRET_ANGLE_RAD,
                MAX_TURRET_ANGLE_RAD
        );

        ffRate = 0;

        writeServos(commandRad);

        updateEstimate(dt);

        lastError = Math.abs(commandRad - estimatedRad);
    }

    // Servos hold position on their own, so stop just freezes the command.
    public void stop() {

        double dt = tick();

        ffRate = 0;

        writeServos(commandRad);

        updateEstimate(dt);

        lastError = Math.abs(commandRad - estimatedRad);
    }

    private double tick() {

        double dt = Math.max(
                0.001,
                Math.min(timer.seconds(), 0.05)
        );

        timer.reset();

        return dt;
    }

    // Model the servo travelling toward the command at its max speed.
    private void updateEstimate(double dt) {

        double step = SERVO_SPEED_RAD_PER_SEC * dt;

        estimatedRad += clamp(
                commandRad - estimatedRad,
                -step,
                step
        );
    }

    // =====================================
    // CABLE LIMITS
    // =====================================

    // Picks the equivalent angle (target +/- 360) that is within travel
    // and closest to where the turret already is.
    private double chooseTarget(
            double wrappedTarget,
            double current
    ) {

        double best = Double.NaN;

        double bestDistance = Double.MAX_VALUE;

        for (int k = -1; k <= 1; k++) {

            double candidate =
                    wrappedTarget + k * 2 * Math.PI;

            if (Math.abs(candidate) > MAX_TURRET_ANGLE_RAD) {
                continue;
            }

            double distance =
                    Math.abs(candidate - current);

            if (distance < bestDistance) {

                bestDistance = distance;

                best = candidate;
            }
        }

        targetReachable = !Double.isNaN(best);

        if (!targetReachable) {

            return clamp(
                    wrappedTarget,
                    -MAX_TURRET_ANGLE_RAD,
                    MAX_TURRET_ANGLE_RAD
            );
        }

        return best;
    }

    // =====================================
    // SERVO OUTPUT
    // =====================================

    private double angleToServoPosition(double turretRad) {

        double dir = TURRET_REVERSED ? -1.0 : 1.0;

        double pos =
                0.5 + dir * (turretRad - ZERO_OFFSET_RAD) / TURRET_TRAVEL_RAD;

        return clamp(pos, 0.0, 1.0);
    }

    private void writeServos(double turretRad) {

        double pos = angleToServoPosition(turretRad);

        turretA.setPosition(SERVO_A_REVERSED ? 1.0 - pos : pos);

        turretB.setPosition(SERVO_B_REVERSED ? 1.0 - pos : pos);
    }

    // =====================================
    // TELEMETRY GETTERS
    // =====================================

    public double getTargetRad() {
        return targetRad;
    }

    public double getCommandRad() {
        return commandRad;
    }

    public double getServoPosition() {
        return angleToServoPosition(commandRad);
    }

    public double getErrorRadians() {
        return lastError;
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