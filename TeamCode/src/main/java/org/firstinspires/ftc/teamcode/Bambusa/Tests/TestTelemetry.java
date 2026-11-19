package org.firstinspires.ftc.teamcode.Bambusa.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "Testing")
public class TestTelemetry extends OpMode {
    @Override
    public void init() {
        telemetry.addData("Telemetry Working (INIT): ", true);
    }

    @Override
    public void loop() {
        telemetry.addData("Telemetry Working (LOOP): ", true);
        telemetry.update();

        //
    }
}
