package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.MathPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class Drive {
    private final IMU imu;
    private final DcMotorEx fl, fr, bl, br;

    private double headingOffset = 0;
    private boolean enabled = true;

    public Drive(HardwareMap hardwareMap) {
        // Wheel motors
        fl = hardwareMap.get(DcMotorEx.class, Settings.DRIVE.NAMES.FL);
        bl = hardwareMap.get(DcMotorEx.class, Settings.DRIVE.NAMES.BL);
        fr = hardwareMap.get(DcMotorEx.class, Settings.DRIVE.NAMES.FR);
        br = hardwareMap.get(DcMotorEx.class, Settings.DRIVE.NAMES.BR);

        // IMU
        imu = hardwareMap.get(IMU.class, Settings.LOCALIZATION.NAMES.IMU);
    }

    public void init() {
        // Setting motor directions
        fl.setDirection(Settings.DRIVE.DIRECTIONS.FL);
        fr.setDirection(Settings.DRIVE.DIRECTIONS.FR);
        bl.setDirection(Settings.DRIVE.DIRECTIONS.BL);
        br.setDirection(Settings.DRIVE.DIRECTIONS.BR);

        // Resetting IMU
        if (Settings.SYSTEM.useFieldCentric && imu != null) {
            imu.initialize(new IMU.Parameters(Settings.LOCALIZATION.DIRECTIONS.IMU));
            imu.resetYaw();
        }
    }

    public void run(Telemetry telemetry, Controls controls) {
        // Disables driving for override
        if (!enabled) return;

        // Final speed
        double finalSpeed = MathPlus.lerp(
                Settings.DRIVE.SPEED.normalSpeed,
                Settings.DRIVE.SPEED.boostSpeed,
                controls.boost()
        );

        double y = controls.forward() * finalSpeed;
        double x = controls.strafe() * finalSpeed * Settings.DRIVE.SPEED.strafeCorrection;
        double rx = controls.turn() * Settings.DRIVE.SPEED.turnSpeed;

        double rawHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Resets forward direction
        if (controls.resetYaw()) headingOffset = rawHeading;

        double botHeading = Settings.SYSTEM.useFieldCentric ? rawHeading - headingOffset : 0;
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Calculations
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        // Apply power
        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);

        // Debugging power consumption
        if (Settings.DEBUG.POWER.debugDrivePowerConsumption) {
            telemetry.addLine(TelemetryPlus.title("DRIVE POWER CONSUMPTION"));
            telemetry.addData("Front Left: ", fl.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("Front Right: ", fr.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("Back Left: ", bl.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("Back Right: ", br.getCurrent(CurrentUnit.AMPS));
            telemetry.addLine(TelemetryPlus.separator());
        }
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }
}
