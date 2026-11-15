package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.PIDF;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

public class Launcher {
    // Both launcher motors
    private DcMotorEx left, right;
    private PIDF leftPIDF, rightPIDF;
    private boolean enabled = true;

    public Launcher(HardwareMap hardwareMap) {
        // Getting launcher motors safely
        left = hardwareMap.tryGet(DcMotorEx.class, Settings.LAUNCHER.NAMES.LEFT_MOTOR);
        right = hardwareMap.tryGet(DcMotorEx.class, Settings.LAUNCHER.NAMES.RIGHT_MOTOR);
    }

    // Null case
    public Launcher() {
        enabled = false;
    }

    // OpMode methods
    public void init(Telemetry telemetry) {
        if (!enabled) return;

        // Motor is disconnected to kill launcher without giving the rest of the robot a stroke
        if (left == null || right == null) {
            enabled = false;
            return;
        }

        // Setting directions
        left.setDirection(Settings.LAUNCHER.DIRECTIONS.LEFT_MOTOR);
        right.setDirection(Settings.LAUNCHER.DIRECTIONS.RIGHT_MOTOR);

        // Installing PIDF settings
        double p = Settings.LAUNCHER.PIDF.p;
        double i = Settings.LAUNCHER.PIDF.i;
        double d = Settings.LAUNCHER.PIDF.d;
        double f = Settings.LAUNCHER.PIDF.f;

        leftPIDF = new PIDF(left, p, i, d, f);
        rightPIDF = new PIDF(right, p, i, d, f);
    }

    public void run(Telemetry telemetry, Controls controls) {
        if (!enabled) return;

        // Automatically enables
        // enable(telemetry);
        double power = controls.shoot() ? Settings.LAUNCHER.SPEED.normal : 0.0;
        left.setPower(power);
        right.setPower(power);
    }

    public void stop() {
        if (!enabled) return;
        disable();
    }

    // Utilities
    private void enable(Telemetry telemetry) {
        // Applying power
        double leftPower = leftPIDF.setVelocity(Settings.LAUNCHER.SPEED.normal);
        double rightPower = rightPIDF.setVelocity(Settings.LAUNCHER.SPEED.normal);

        // Debugging
        boolean displayPower = Settings.DEBUG.LAUNCHER.displayPower;
        boolean displayVelocity = Settings.DEBUG.LAUNCHER.displayVelocity;
        if (!displayPower && !displayVelocity) return;

        telemetry.addLine(TelemetryPlus.title("LAUNCHER"));
        if (displayPower) debugPower(telemetry);
        if (displayVelocity) debugVelocity(telemetry);
        telemetry.addLine(TelemetryPlus.separator());
    }

    private void disable() {
        leftPIDF.setVelocity(0);
        rightPIDF.setVelocity(0);
    }

    // Debugging
    private void debugConnection(Telemetry telemetry) {
        telemetry.addLine(TelemetryPlus.title("LAUNCHER | CONNECTION"));
        telemetry.addData("LEFT: ", left != null);
        telemetry.addData("RIGHT: ", right != null);
        telemetry.addLine(TelemetryPlus.separator());
    }

    private void debugPower(Telemetry telemetry) {
        telemetry.addData("Left Power: ", left.getPower());
        telemetry.addData("Right Power: ", right.getPower());
    }

    private void debugVelocity(Telemetry telemetry) {
        telemetry.addData("Left Velocity: ", left.getVelocity());
        telemetry.addData("Right Velocity: ", right.getVelocity());
    }
}
