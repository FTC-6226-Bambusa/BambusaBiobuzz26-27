package org.firstinspires.ftc.teamcode.Bambusa.Settings;

import com.pedropathing.revhub.localizers.Encoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LocalizationSettings {
    public HardwareMapSettings NAMES = new HardwareMapSettings();
    public DirectionSettings DIRECTIONS = new DirectionSettings();

    public static class HardwareMapSettings {
        public String LEFT = Settings.DRIVE.NAMES.FR;
        public String RIGHT = Settings.DRIVE.NAMES.BL;
        public String PARALLEL = Settings.DRIVE.NAMES.FL;

        public String IMU = "imu";
    }

    public static class DirectionSettings {
        public double LEFT = Encoder.FORWARD;
        public double RIGHT = Encoder.FORWARD;
        public double PARALLEL = Encoder.REVERSE;

        public RevHubOrientationOnRobot IMU = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
    }
}

