package org.firstinspires.ftc.teamcode.Bambusa.Tests;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Managers.Robot;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class TestLocalization extends OpMode {
    public DcMotorEx left, right, strafe;
    public ThreeWheelLocalizer localizer;
    public Controls controls;
    public Drive drive;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotorEx.class, Settings.LOCALIZATION.NAMES.LEFT);
        right = hardwareMap.get(DcMotorEx.class, Settings.LOCALIZATION.NAMES.RIGHT);
        strafe = hardwareMap.get(DcMotorEx.class, Settings.LOCALIZATION.NAMES.STRAFE);

        localizer = new ThreeWheelLocalizer(hardwareMap, Constants.localizerConfig);
        localizer.setPose(new Pose(0, 0, 0));

        drive = new Drive(hardwareMap);
        drive.init(telemetry);

        controls = new Controls(gamepad1, gamepad2);
    }

    @Override
    public void loop() {
        localizer.update();
        Pose pose = localizer.pose();

        drive.run(telemetry, controls);

        telemetry.addLine(TelemetryPlus.title("LOCALIZER READINGS"));
        telemetry.addData("X: ", pose.x());
        telemetry.addData("Y: ", pose.y());
        telemetry.addData("H: ", pose.heading());

        telemetry.addLine(TelemetryPlus.title("ENCODER POSITIONS"));
        telemetry.addData("LEFT: ", left.getCurrentPosition());
        telemetry.addData("RIGHT: ", right.getCurrentPosition());
        telemetry.addData("STRAFE: ", strafe.getCurrentPosition());
        telemetry.update();
    }
}
