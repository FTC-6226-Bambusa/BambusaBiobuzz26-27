package org.firstinspires.ftc.teamcode.Bambusa.Config;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DriveSettings {
    public HardwareMapSettings  NAMES       = new HardwareMapSettings();
    public SpeedSettings        SPEED       = new SpeedSettings();
    public DirectionSettings    DIRECTIONS  = new DirectionSettings();

    public static class HardwareMapSettings {
        public String FL = "leftFront";
        public String BL = "leftRear";
        public String BR = "rightRear";
        public String FR = "rightFront";

        public String IMU = "imu";
    }

    public static class SpeedSettings {
        // Speed
        public double normalSpeed = 0.6;
        public double boostSpeed = 1.0;
        public double turnSpeed = 0.7;

        // Strafing
        public double strafeCorrection = 1.1;
    }

    public static class DirectionSettings {
        public DcMotorSimple.Direction FL = DcMotorSimple.Direction.REVERSE;
        public DcMotorSimple.Direction BL = DcMotorSimple.Direction.REVERSE;
        public DcMotorSimple.Direction FR = DcMotorSimple.Direction.FORWARD;
        public DcMotorSimple.Direction BR = DcMotorSimple.Direction.FORWARD;
    }
}

