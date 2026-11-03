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
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
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
            Controller primaryTranslationalForward = Controller.proportional(0.2458156498453205);
            Controller secondaryTranslationalForward = Controller.proportional(0.09082231430271168);
            Controller primaryTranslationalLateral = Controller.proportional(0.3957703907844926);
            Controller secondaryTranslationalLateral = Controller.proportional(0.1462265842152624);

            c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
            c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

            c.coast.set(Controller.proportionalFeedforward(0.019864097551748124));
            c.brake.set(Controller.proportionalFeedforward(0.016884482918985904));

            c.headingFeedback.set(Controller.proportional(4.833990136997207));
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.053242614688476624, 0.0053940128286482985));

            c.linearBrakeCoefficients.set(Matrix.diag(0.019290488663073327, 0.04532263524083615));
            c.quadraticBrakeCoefficients.set(Matrix.diag(0.002333845916563665, 0.0016013433453296948));

            c.maxAchievableForwardVelocity.set(65.33677339304514);
            c.maxAchievableStrafeVelocity.set(51.09426817316891);
            c.naturalForwardDeceleration.set(92.53414704977791);
            c.naturalStrafeDeceleration.set(114.69203308460153);
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