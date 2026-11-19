package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
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
        if (Settings.DEBUG.LAUNCHER.debugConnection) debugConnection(telemetry);

        // Motor is disconnected to kill launcher without giving the rest of the robot a stroke
        if (left == null || right == null) {
            enabled = false;
            return;
        }

        telemetry.addData(TelemetryPlus.title("LAUNCHER"), "").setRetained(true);
        if (Settings.DEBUG.LAUNCHER.displayPower) debugPower(telemetry);
        if (Settings.DEBUG.LAUNCHER.displayVelocity) debugVelocity(telemetry);
        telemetry.addData(TelemetryPlus.separator(), "").setRetained(true);

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

    public void run(Controls controls) {
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

    // Autonomous methods
    public Command enableCommand() {
        return instant(() -> {
            double power = Settings.LAUNCHER.SPEED.normal;
            left.setPower(power);
            right.setPower(power);
        })
        .requiring(this)
        .unless(() -> !enabled);
    }

    // Utilities
    public void enable(Telemetry telemetry) {
        // Applying power
        leftPIDF.setVelocity(Settings.LAUNCHER.SPEED.normal);
        rightPIDF.setVelocity(Settings.LAUNCHER.SPEED.normal);

        // Debugging
        boolean displayPower = Settings.DEBUG.LAUNCHER.displayPower;
        boolean displayVelocity = Settings.DEBUG.LAUNCHER.displayVelocity;
        if (!displayPower && !displayVelocity) return;
    }

    private void disable() {
        leftPIDF.setVelocity(0);
        rightPIDF.setVelocity(0);

        left.setPower(0);
        right.setPower(0);
    }

    // Debugging
    private void debugConnection(Telemetry telemetry) {
        telemetry.log().add("Left Connected: ", left != null);
        telemetry.log().add("Right Connected: ", right != null);
    }

    private void debugPower(Telemetry telemetry) {
        telemetry.addData("Left Power: ", () -> left.getPower());
        telemetry.addData("Right Power: ", () -> right.getPower());
    }

    private void debugVelocity(Telemetry telemetry) {
        telemetry.addData("Left Velocity: ", () -> left.getVelocity());
        telemetry.addData("Right Velocity: ", () -> right.getVelocity());
    }
}
