package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class Launcher {
    // Both launcher motors
    DcMotorEx left, right;
    PIDF leftPIDF, rightPIDF;
    boolean disconnected = false;

    public Launcher(HardwareMap hardwareMap) {
        // Getting launcher motors safely
        left = hardwareMap.tryGet(DcMotorEx.class, Settings.LAUNCHER.NAMES.LEFT_MOTOR);
        right = hardwareMap.tryGet(DcMotorEx.class, Settings.LAUNCHER.NAMES.RIGHT_MOTOR);
    }

    public void init() {
        // Motor is disconnected so kill launcher without giving the rest of the robot a stroke
        if (left == null || right == null) {
            disconnected = true;
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
        if (disconnected) return;

        // Automatically enables
        enable(telemetry);
    }

    public void enable(Telemetry telemetry) {
        // Applying power
        double leftPower = leftPIDF.setVelocity(Settings.LAUNCHER.SPEED.normal);
        double rightPower = rightPIDF.setVelocity(Settings.LAUNCHER.SPEED.normal);

        // Debugging
        boolean showPower = Settings.DEBUG.TURRET.displayPower;
        boolean showVelocity = Settings.DEBUG.TURRET.displayVelocity;

        if (!showPower && !showVelocity) return;

        telemetry.addLine(TelemetryPlus.title("LAUNCHER"));

        if (showPower) {
            telemetry.addData("Left Power: ", leftPower);
            telemetry.addData("Right Power: ", rightPower);
        }

        if (showVelocity) {
            telemetry.addData("Left Velocity: ", left.getVelocity());
            telemetry.addData("Right Velocity: ", right.getVelocity());
        }

        telemetry.addLine(TelemetryPlus.separator());
    }

    public void disable() {
        leftPIDF.setVelocity(0);
        rightPIDF.setVelocity(0);
    }
}
