package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class Robot {
    public Launcher turret;
    public Drive drive;
    public Fairwater fairwater;
    public Conveyor conveyor;

    public Robot(HardwareMap hardwareMap) {
        drive = Settings.SYSTEM.useDriving ? new Drive(hardwareMap) : null;
        conveyor = Settings.SYSTEM.useConveyor ? new Conveyor(hardwareMap) : null;
        fairwater = Settings.SYSTEM.useFairwater ? new Fairwater(hardwareMap) : null;
        turret = Settings.SYSTEM.useTurret ? new Launcher(hardwareMap) : null;
    }

    public void init() {
        // Initializing robot subsystems
        if (drive != null) drive.init();
        if (conveyor != null) conveyor.init();
        if (turret != null) turret.init();
        if (fairwater != null) fairwater.init();
    }

    public void run(Telemetry telemetry, Controls controls) {
        // Running subsystems
        if (drive != null) drive.run(telemetry, controls);
        if (conveyor != null) conveyor.run(telemetry, controls);
        if (turret != null) turret.run(telemetry, controls);
        if (fairwater != null) fairwater.run(telemetry, controls);
    }
}
