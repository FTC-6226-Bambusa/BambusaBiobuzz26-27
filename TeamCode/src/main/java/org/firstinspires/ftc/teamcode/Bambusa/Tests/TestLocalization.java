package org.firstinspires.ftc.teamcode.Bambusa.Tests;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name = "Test Localization", group = "Testing")
public class TestLocalization extends OpMode {
    public DcMotorEx left, right, strafe;
    public Controls controls;
    public Drive drive;
    public ThreeWheelLocalizer localizer;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotorEx.class, Settings.LOCALIZATION.NAMES.LEFT);
        right = hardwareMap.get(DcMotorEx.class, Settings.LOCALIZATION.NAMES.RIGHT);
        strafe = hardwareMap.get(DcMotorEx.class, Settings.LOCALIZATION.NAMES.STRAFE);

        drive = new Drive(hardwareMap);
        drive.init(telemetry);

        controls = new Controls(gamepad1, gamepad2);

        // Localizer
        localizer = new ThreeWheelLocalizer(hardwareMap, Constants.localizerConfig);
        localizer.setPose(new Pose(0, 0, 0));

        // Enabling debugs
        telemetry.addData(TelemetryPlus.title("ENCODER POSITIONS"), "").setRetained(true);
        telemetry.addData("LEFT: ", () -> left.getCurrentPosition());
        telemetry.addData("RIGHT: ", () -> right.getCurrentPosition());
        telemetry.addData("STRAFE: ", () -> strafe.getCurrentPosition());

        telemetry.addData(TelemetryPlus.title("LOCALIZER POSITION"), "").setRetained(true);
        telemetry.addData("x", () -> localizer.pose().x());
        telemetry.addData("y", () -> localizer.pose().y());
        telemetry.addData("heading", () -> localizer.pose().heading());
    }

    @Override
    public void loop() {
        localizer.update();

        // Running drive
        drive.run(controls);
    }
}
