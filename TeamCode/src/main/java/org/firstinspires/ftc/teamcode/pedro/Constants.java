package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set(Settings.DRIVE.NAMES.FL);
        c.frontRightName.set(Settings.DRIVE.NAMES.FR);
        c.backLeftName.set(Settings.DRIVE.NAMES.BL);
        c.backRightName.set(Settings.DRIVE.NAMES.BR);
        c.frontLeftDirection.set(Settings.DRIVE.DIRECTIONS.FL);
        c.frontRightDirection.set(Settings.DRIVE.DIRECTIONS.FR);
        c.backLeftDirection.set(Settings.DRIVE.DIRECTIONS.BL);
        c.backRightDirection.set(Settings.DRIVE.DIRECTIONS.BR);
    });

    public static ThreeWheelConfig localizerConfig = new ThreeWheelConfig(c -> {
        c.leftEncoderName.set(Settings.LOCALIZATION.NAMES.LEFT);
        c.rightEncoderName.set(Settings.LOCALIZATION.NAMES.RIGHT);
        c.strafeEncoderName.set(Settings.LOCALIZATION.NAMES.STRAFE);
        c.leftPodY.set(7.4332);
        c.rightPodY.set(-7.1580);
        c.strafePodX.set(-3.7284);
        c.forwardTicksToInches.set(0.00199674760631);
        c.strafeTicksToInches.set(0.00200631152166);
        c.turnTicksToRadians.set(0.0020219519116983);
        c.leftEncoderDirection.set(Settings.LOCALIZATION.DIRECTIONS.LEFT);
        c.rightEncoderDirection.set(Settings.LOCALIZATION.DIRECTIONS.RIGHT);
        c.strafeEncoderDirection.set(Settings.LOCALIZATION.DIRECTIONS.STRAFE);
    });


    public static Follower create(HardwareMap h) {
        // return new Follower(drivetrainConfig, localizerConfig, Foresight);
        return null;
    }
}