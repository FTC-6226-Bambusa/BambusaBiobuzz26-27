package org.firstinspires.ftc.teamcode.Bambusa.Tests;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

@TeleOp(name = "Test Drive Train")
@Configurable
public class TestDrivetrain extends OpMode {
    public DcMotor FL, FR, BL, BR;
    public double speed = 1.0;

    @Override
    public void init() {
        // Obtaining drive train motors
        FL = hardwareMap.dcMotor.get(Settings.DRIVE.NAMES.FL);
        FR = hardwareMap.dcMotor.get(Settings.DRIVE.NAMES.FR);
        BL = hardwareMap.dcMotor.get(Settings.DRIVE.NAMES.BL);
        BR = hardwareMap.dcMotor.get(Settings.DRIVE.NAMES.BR);
    }

    @Override
    public void loop() {
        // Powering motors if the user presses the corresponding button
        telemetry.addLine("-- CONTROLS --");
        telemetry.addLine(displaySelection("Front Right: Y", powerMotor(FR, gamepad1.y || gamepad2.y)));
        telemetry.addLine(displaySelection("Front Left: X", powerMotor(FL, gamepad1.x || gamepad2.x)));
        telemetry.addLine(displaySelection("Back Right: B", powerMotor(BR, gamepad1.b || gamepad2.b)));
        telemetry.addLine(displaySelection("Back Left: A", powerMotor(BL, gamepad1.a || gamepad2.a)));
    }

    // Powers motor
    public boolean powerMotor(DcMotor motor, boolean on) {
        motor.setPower(on ? speed : 0);
        return on;
    }

    // User feedback function
    public String displaySelection(String txt, boolean selected) {
        return selected ? "* [" + txt + "]" : txt;
    }
}
