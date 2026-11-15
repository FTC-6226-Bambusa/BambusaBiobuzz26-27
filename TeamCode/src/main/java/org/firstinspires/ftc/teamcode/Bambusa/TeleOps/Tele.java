package org.firstinspires.ftc.teamcode.Bambusa.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Preparation;
import org.firstinspires.ftc.teamcode.Bambusa.Managers.Robot;

/* TODO:
    * Get controls to work using json file and web interface
    * Fix IMU rotation
 */

@TeleOp(name = "goated teleop")
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
        preparation.init_loop(telemetry);
    }

    @Override
    public void loop() {
        preparation.run(telemetry);
        robot.run(telemetry, controls);
    }

    @Override
    public void stop() {
        preparation.stop(telemetry);
        robot.stop(telemetry);
    }
}