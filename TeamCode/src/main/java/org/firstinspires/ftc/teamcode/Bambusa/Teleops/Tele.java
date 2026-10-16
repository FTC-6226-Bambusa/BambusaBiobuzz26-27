package org.firstinspires.ftc.teamcode.Bambusa.Teleops;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Robot;

import java.util.List;

@TeleOp(name = "goated teleop")
public class Tele extends OpMode {
    Controls controls;
    Robot robot;

    private List<LynxModule> hubs;

    @Override
    public void init() {
        // Controls
        controls = new Controls(gamepad1, gamepad2);

        // Robot
        robot = new Robot(hardwareMap);
        robot.init();

        // Enable manual bulk caching
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }

    @Override
    public void loop() {
        // Read hub snapshots
        for (LynxModule hub : hubs) hub.clearBulkCache();

        // Running the robot
        robot.run(telemetry, controls);
    }
}
