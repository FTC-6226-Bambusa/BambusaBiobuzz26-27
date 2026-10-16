package org.firstinspires.ftc.teamcode.Bambusa.Settings;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class ConveyorSettings {
    public HardwareMapSettings NAMES = new HardwareMapSettings();
    public DirectionSettings DIRECTIONS = new DirectionSettings();
    public SpeedSettings SPEED = new SpeedSettings();

    public static class HardwareMapSettings {
        public String in1 = "intake1";
        public String in2 = "intake2";
        public String out = "outtake";
    }

    public static class DirectionSettings {
        public DcMotorSimple.Direction in1 = DcMotorSimple.Direction.REVERSE;
        public DcMotorSimple.Direction in2 = DcMotorSimple.Direction.FORWARD;
        public DcMotorSimple.Direction out = DcMotorSimple.Direction.FORWARD;
    }

    public static class SpeedSettings {
        public double normal = 0.8;
        public double outtake = 1;
    }
}
