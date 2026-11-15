package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.MathPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

public class Drive {
    private IMU imu;
    private DcMotorEx fl, fr, bl, br;

    private double headingOffset = 0;
    private boolean enabled = true;

    public Drive(HardwareMap hardwareMap) {
        // Wheel motors
        fl = hardwareMap.tryGet(DcMotorEx.class, Settings.DRIVE.NAMES.FL);
        bl = hardwareMap.tryGet(DcMotorEx.class, Settings.DRIVE.NAMES.BL);
        fr = hardwareMap.tryGet(DcMotorEx.class, Settings.DRIVE.NAMES.FR);
        br = hardwareMap.tryGet(DcMotorEx.class, Settings.DRIVE.NAMES.BR);

        // IMU
        imu = hardwareMap.tryGet(IMU.class, Settings.DRIVE.NAMES.IMU);
    }

    // Null case
    public Drive() {
        enabled = false;
    }

    // OpMode methods
    public void init(Telemetry telemetry) {
        if (!enabled) return;

        // Debugging connection
        if (Settings.DEBUG.DRIVE.debugConnection) debugConnection(telemetry);

        // Verifying functionality
        if (fl == null || fr == null || br == null || bl == null) {
            enabled = false;
            return;
        }

        // Setting motor directions
        fl.setDirection(Settings.DRIVE.DIRECTIONS.FL);
        fr.setDirection(Settings.DRIVE.DIRECTIONS.FR);
        bl.setDirection(Settings.DRIVE.DIRECTIONS.BL);
        br.setDirection(Settings.DRIVE.DIRECTIONS.BR);

        // Setting brake behavior
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Resetting IMU
        if (Settings.SYSTEM.useFieldCentric && imu != null) {
            imu.initialize(new IMU.Parameters(Settings.LOCALIZATION.DIRECTIONS.IMU));
            imu.resetYaw();
        }
    }

    public void run(Telemetry telemetry, Controls controls) {
        // Disables driving for override
        if (!enabled) return;

        // Both base speeds set to 1.0 for maximum performance
        double finalSpeed = MathPlus.lerp(
                Settings.DRIVE.SPEED.normalSpeed, // Set to 1.0 in Settings
                Settings.DRIVE.SPEED.boostSpeed,  // Set to 1.0 in Settings
                controls.boost()
        );

        double y  = controls.forward() * finalSpeed;
        double x  = controls.strafe()  * finalSpeed * Settings.DRIVE.SPEED.strafeCorrection;
        double rx = controls.turn()    * Settings.DRIVE.SPEED.turnSpeed; // Set to 1.0 in Settings

        // IMU / Heading
        double rawHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Resets forward direction
        if (controls.resetYaw()) headingOffset = rawHeading;

        double botHeading = Settings.SYSTEM.useFieldCentric ? rawHeading - headingOffset : 0;

        // Pedro Pathing field-centric vector rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Raw mecanum wheel power calculations
        double frontLeftPower  = rotY + rotX + rx;
        double backLeftPower   = rotY - rotX + rx;
        double frontRightPower = rotY - rotX - rx;
        double backRightPower  = rotY + rotX - rx;

        // Dynamic normalization
        double max = Math.max(
                Math.abs(frontLeftPower),
                Math.max(Math.abs(backLeftPower),
                Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(backRightPower), 1.0)))
        );

        frontLeftPower  /= max;
        backLeftPower   /= max;
        frontRightPower /= max;
        backRightPower  /= max;

        // Apply power directly
        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);

        // Debugging power consumption
        if (Settings.DEBUG.DRIVE.debugPowerConsumption) debugPowerConsumption(telemetry);
    }

    public void stop() {
        if (!enabled) return;

        fl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
        bl.setPower(0);
    }

    // Debugging
    private void debugConnection(Telemetry telemetry) {
        telemetry.addLine(TelemetryPlus.title("DRIVE | CONNECTION"));
        telemetry.addData("Front Left: ", fl != null);
        telemetry.addData("Front Right: ", fr != null);
        telemetry.addData("Back Left: ", bl != null);
        telemetry.addData("Back Left: ", br != null);
        telemetry.addLine(TelemetryPlus.separator());
    }

    private void debugPowerConsumption(Telemetry telemetry) {
        double flC = fl.getCurrent(CurrentUnit.AMPS);
        double frC = fr.getCurrent(CurrentUnit.AMPS);
        double blC = bl.getCurrent(CurrentUnit.AMPS);
        double brC = br.getCurrent(CurrentUnit.AMPS);

        telemetry.addLine(TelemetryPlus.title("DRIVE POWER CONSUMPTION"));
        telemetry.addData("Total Power: ", flC + frC + blC + brC);
        telemetry.addLine(TelemetryPlus.separator());
    }
}
