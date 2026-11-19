package org.firstinspires.ftc.teamcode.Bambusa.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Preparation;
import org.firstinspires.ftc.teamcode.Bambusa.Managers.Robot;

/* TODO:
    * Fix IMU rotation
    *
    * Create recorded autonomous which uses inheritance from Gamepad to feed recorded values.
    *
    * Add code for the conveyor which accepts servos instead of all DcMotor
    *
    * MAYBE: Use battery voltage detection or PID to ensure that the drive maintains the same speed despite voltage.
    * MAYBE: Set controller colors using gamepad1.setLedColor(r, g, b)
    *


    * When you update something you should only update it in one place!
 */

@TeleOp(name = "goated teleop", group = "TeleOp")
public class Tele extends OpMode {
    Preparation preparation;
    Controls controls;
    Robot robot;

    @Override
    public void init() {
        // Preparation (activated before each OpMode stage)
        preparation = new Preparation(hardwareMap);
        preparation.init(telemetry);

        // Controls
        controls = new Controls(gamepad1, gamepad2);

        // Robot
        robot = new Robot(hardwareMap);
        robot.init(telemetry);
    }

    @Override
    public void init_loop() {
        preparation.init_loop(controls);
    }

    @Override
    public void loop() {
        preparation.run();
        robot.run(controls);
    }

    @Override
    public void stop() {
        robot.stop();
    }
}