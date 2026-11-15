package org.firstinspires.ftc.teamcode.Bambusa.Config;

import com.pedropathing.revhub.localizers.Encoder;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LocalizationSettings {
    public HardwareMapSettings NAMES = new HardwareMapSettings();
    public DirectionSettings DIRECTIONS = new DirectionSettings();

    public static class HardwareMapSettings {
        public String LEFT = Settings.DRIVE.NAMES.FL;
        public String RIGHT = Settings.DRIVE.NAMES.BR;
        public String STRAFE = Settings.DRIVE.NAMES.BL;
    }

    public static class DirectionSettings {
        public double LEFT = Encoder.FORWARD;
        public double RIGHT = Encoder.FORWARD;
        public double STRAFE = Encoder.REVERSE;

        public RevHubOrientationOnRobot IMU = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
    }
}