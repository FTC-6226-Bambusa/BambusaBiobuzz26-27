package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

public class Conveyor {
    private DcMotorEx in1, in2, out;
    private boolean enabled = true;

    public Conveyor(HardwareMap hardwareMap) {
        in1 = hardwareMap.tryGet(DcMotorEx.class, Settings.CONVEYOR.NAMES.in1);
        in2 = hardwareMap.tryGet(DcMotorEx.class, Settings.CONVEYOR.NAMES.in2);
        out = hardwareMap.tryGet(DcMotorEx.class, Settings.CONVEYOR.NAMES.out);
    }

    // Null case
    public Conveyor() {
        enabled = false;
    }

    // OpMode methods
    public void init(Telemetry telemetry) {
        if (!enabled) return;

        // Debugging connection
        if (Settings.DEBUG.CONVEYOR.debugConnection) debugConnection(telemetry);

        // Verifying functionality
        if (in1 == null || in2 == null || out == null) {
            enabled = false;
            return;
        }

        // Setting directions
        in1.setDirection(Settings.CONVEYOR.DIRECTIONS.in1);
        in2.setDirection(Settings.CONVEYOR.DIRECTIONS.in2);
        out.setDirection(Settings.CONVEYOR.DIRECTIONS.out);
    }

    public void run(Telemetry telemetry, Controls controls) {
        if (!enabled) return;

        // Automatically intakes
        in1.setPower(Settings.CONVEYOR.SPEED.normal);
        in2.setPower(Settings.CONVEYOR.SPEED.normal);

        // Pushes pollen or nectar into turret
        out.setPower(controls.takeout() ? Settings.CONVEYOR.SPEED.takeout : 0);

        // Debugging
        if (Settings.DEBUG.CONVEYOR.displayPower) debugPowerConsumption(telemetry);
    }

    public void stop() {
        if (!enabled) return;

        in1.setPower(0);
        in2.setPower(0);
        out.setPower(0);
    }

    // Debugging
    private void debugConnection(Telemetry telemetry) {
        telemetry.addLine(TelemetryPlus.title("CONVEYOR | CONNECTION"));
        telemetry.addData("IN 1: ", in1 != null);
        telemetry.addData("IN 2: ", in2 != null);
        telemetry.addData("OUT: ", out != null);
        telemetry.addLine(TelemetryPlus.separator());
    }

    private void debugPowerConsumption(Telemetry telemetry) {
        telemetry.addLine(TelemetryPlus.title("CONVEYOR POWER CONSUMPTION"));
        telemetry.addData("Intake 1: ", in1.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Intake 2: ", in2.getCurrent(CurrentUnit.AMPS));
        telemetry.addData("Takeout: ", out.getCurrent(CurrentUnit.AMPS));
        telemetry.addLine(TelemetryPlus.separator());
    }
}
