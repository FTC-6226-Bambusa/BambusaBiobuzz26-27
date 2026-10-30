package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class Robot {
    public Launcher launcher;
    public Drive drive;
    public Fairwater fairwater;
    public Conveyor conveyor;

    public Robot(HardwareMap hardwareMap) {
        drive       = Settings.SYSTEM.useDrive      ? new Drive(hardwareMap)        : new Drive();
        conveyor    = Settings.SYSTEM.useConveyor   ? new Conveyor(hardwareMap)     : new Conveyor();
        launcher    = Settings.SYSTEM.useLauncher   ? new Launcher(hardwareMap)     : new Launcher();
        fairwater   = Settings.SYSTEM.useFairwater  ? new Fairwater(hardwareMap)    : new Fairwater();
    }

    public void init(Telemetry telemetry) {
        drive.init(telemetry);
        conveyor.init(telemetry);
        launcher.init(telemetry);
        fairwater.init(/*no hardware yet*/);
    }

    public void run(Telemetry telemetry, Controls controls) {
        drive.run(telemetry, controls);
        conveyor.run(telemetry, controls);
        launcher.run(telemetry, controls);
        fairwater.run(telemetry, controls);
    }

    public void stop(Telemetry telemetry) {
        drive.stop();
        conveyor.stop();
        launcher.stop();
        fairwater.stop();
    }
}
