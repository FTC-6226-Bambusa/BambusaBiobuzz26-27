package org.firstinspires.ftc.teamcode.Bambusa.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bambusa.Helpers.USBConnection;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

@TeleOp(name = "Test USB Connection", group = "Testing")
public class TestUSBConnection extends OpMode {

    private USBConnection connection;
    private String lastCommand = "None";

    @Override
    public void init() {
        connection = new USBConnection(Settings.USB.PORTS.LL1);
    }

    @Override
    public void loop() {
        // Updating the connection
        connection.update();

        // Receiving data
        String incoming = connection.read();
        if (!incoming.isEmpty()) lastCommand = incoming;

        // Writing data
        connection.write("Telemetry | " + gamepad1.left_stick_x + " | Command: " + lastCommand);

        // Display on driver station
        telemetry.addData("Status", connection.status);
        telemetry.addData("Last Command Received", lastCommand);
        telemetry.update();
    }

    @Override
    public void stop() {
        connection.stop();
    }
}