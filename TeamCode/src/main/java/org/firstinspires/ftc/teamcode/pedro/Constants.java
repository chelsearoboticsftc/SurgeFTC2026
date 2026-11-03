package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    // Programming constants
// public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
//    c.frontLeftName.set("fl");
//    c.frontRightName.set("fr");
//    c.backLeftName.set("bl");
//    c.backRightName.set("br");
//    c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
//    c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
//    c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
//    c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
//});

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("fl");
        c.frontRightName.set("fr");
        c.backLeftName.set("bl");
        c.backRightName.set("br");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        c.xPodOffset.set(-7.027787936954049);
        c.yPodOffset.set(6.087778046375185);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
//    public static ForesightConfig foresightConfig = new ForesightConfig(
//            c -> {
//                Controller primaryTranslationalForward = Controller.proportional(0.3355587431031364);
//                Controller secondaryTranslationalForward = Controller.proportional(0.12397998928185856);
//                Controller primaryTranslationalLateral = Controller.proportional(0.47336237244494167);
//                Controller secondaryTranslationalLateral = Controller.proportional(0.17489474814286393);
//
//                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
//                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));
//
//                c.coast.set(Controller.proportionalFeedforward(0.016686146123268353));
//                c.brake.set(Controller.proportionalFeedforward(0.0141832242047781));
//
//                c.headingFeedback.set(Controller.proportional(8.157378572616322));
//                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.052565297731636075, 0.008252896450663152));
//
//                c.linearBrakeCoefficients.set(Matrix.diag(0.040953429035681194, 0.18864715512706476));
//                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0020533986486626714, -0.0012754800175341994));
//
//                c.maxAchievableForwardVelocity.set(67.8527789267188);
//                c.maxAchievableStrafeVelocity.set(54.85431443204037);
//                c.naturalForwardDeceleration.set(71.7276711741861);
//                c.naturalStrafeDeceleration.set(89.73017348540341);
//            }
    //);
public static ForesightConfig foresightConfig = new ForesightConfig(
        c -> {
            Controller primaryTranslationalForward = Controller.proportional(0.36649024537433517);
            Controller secondaryTranslationalForward = Controller.proportional(0.13540835286610386);
            Controller primaryTranslationalLateral = Controller.proportional(0.4375681719065548);
            Controller secondaryTranslationalLateral = Controller.proportional(0.16166974748258328);

            c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
            c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

            c.coast.set(Controller.proportionalFeedforward(0.015138349359200269));
            c.brake.set(Controller.proportionalFeedforward(0.012867596955320228));

            c.headingFeedback.set(Controller.proportional(6.8524702187951805));
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05116369266924896, 0.006144656365326549));

            c.linearBrakeCoefficients.set(Matrix.diag(0.0657065334939785, 0.052563802883886214));
            c.quadraticBrakeCoefficients.set(Matrix.diag(0.0011483269175398374, 0.001414448434992414));

            c.maxAchievableForwardVelocity.set(77.31370152799333);
            c.maxAchievableStrafeVelocity.set(63.92064334247166);
            c.naturalForwardDeceleration.set(77.15617076222603);
            c.naturalStrafeDeceleration.set(68.55125485003516);
        }
);
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

}