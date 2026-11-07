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
        c.xPodOffset.set(-6.25);
        c.yPodOffset.set(7.0);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

public static ForesightConfig foresightConfig = new ForesightConfig(
        c -> {
            Controller primaryTranslationalForward = Controller.proportional(0.30255110760648546);
            Controller secondaryTranslationalForward = Controller.proportional(0.11178454994611017);
            Controller primaryTranslationalLateral = Controller.proportional(0.4115371306049148);
            Controller secondaryTranslationalLateral = Controller.proportional(0.15205197328386127);

            c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
            c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

            c.coast.set(Controller.proportionalFeedforward(0.015263426320264516));
            c.brake.set(Controller.proportionalFeedforward(0.012973912372224838));

            c.headingFeedback.set(Controller.proportional(5.952277106538399));
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.042289035881175295, 0.006783984578318827));

            c.linearBrakeCoefficients.set(Matrix.diag(0.07298841930985557, 0.05085811277778232));
            c.quadraticBrakeCoefficients.set(Matrix.diag(9.899825791372485E-4, 0.0014944099888860966));

            c.maxAchievableForwardVelocity.set(64.97295652897088);
            c.maxAchievableStrafeVelocity.set(57.870388227897806);
            c.naturalForwardDeceleration.set(80.16282952456892);
            c.naturalStrafeDeceleration.set(88.71454828890519);
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