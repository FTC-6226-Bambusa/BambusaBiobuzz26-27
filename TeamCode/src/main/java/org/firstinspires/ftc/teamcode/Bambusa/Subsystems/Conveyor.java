package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class Conveyor {
    public DcMotorEx in1, in2, out;
    private boolean disconnected = false;

    public Conveyor(HardwareMap hardwareMap) {
        in1 = hardwareMap.tryGet(DcMotorEx.class, Settings.CONVEYOR.NAMES.in1);
        in2 = hardwareMap.tryGet(DcMotorEx.class, Settings.CONVEYOR.NAMES.in2);
        out = hardwareMap.tryGet(DcMotorEx.class, Settings.CONVEYOR.NAMES.out);
    }

    public void init() {
        // Checking if any motors are disconnected
        if (in1 == null || in2 == null || out == null) {
            disconnected = true;
            return;
        }

        // Setting directions
        in1.setDirection(Settings.CONVEYOR.DIRECTIONS.in1);
        in2.setDirection(Settings.CONVEYOR.DIRECTIONS.in2);
        out.setDirection(Settings.CONVEYOR.DIRECTIONS.out);
    }

    public void run(Telemetry telemetry, Controls controls) {
        if (disconnected) return;

        // Automatically intakes
        in1.setPower(Settings.CONVEYOR.SPEED.normal);
        in2.setPower(Settings.CONVEYOR.SPEED.normal);

        // Pushes pollen or nectar into turret
        out.setPower(controls.shoot() ? Settings.CONVEYOR.SPEED.outtake : 0);

        // Debugging
        if (Settings.DEBUG.POWER.debugConveyorPowerConsumption) {
            telemetry.addLine(TelemetryPlus.title("CONVEYOR POWER CONSUMPTION"));
            telemetry.addData("Intake 1: ", in1.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("Intake 2: ", in2.getCurrent(CurrentUnit.AMPS));
            telemetry.addData("Outtake: ", out.getCurrent(CurrentUnit.AMPS));
            telemetry.addLine(TelemetryPlus.separator());
        }
    }
}
