package org.firstinspires.ftc.teamcode.Bambusa.Settings;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LauncherSettings {
    public HardwareMapSettings NAMES = new HardwareMapSettings();
    public DirectionSettings DIRECTIONS = new DirectionSettings();
    public SpeedSettings SPEED = new SpeedSettings();
    public PIDFSettings PIDF = new PIDFSettings();

    public static class HardwareMapSettings {
        public String LEFT_MOTOR = "turretLeft";
        public String RIGHT_MOTOR = "turretRight";
    }

    public static class DirectionSettings {
        public DcMotorSimple.Direction LEFT_MOTOR = DcMotorSimple.Direction.REVERSE;
        public DcMotorSimple.Direction RIGHT_MOTOR = DcMotorSimple.Direction.FORWARD;
    }

    public static class PIDFSettings {
        public double p = 0;
        public double i = 0;
        public double d = 0;
        public double f = 0;
    }

    public static class SpeedSettings {
        // Temporary single value for speed
        // Later, this should mold to distance from hive
        public double normal = 0.8;
    }
}
