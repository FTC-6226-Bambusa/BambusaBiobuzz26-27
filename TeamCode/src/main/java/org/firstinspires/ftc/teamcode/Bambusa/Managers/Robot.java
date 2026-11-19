package org.firstinspires.ftc.teamcode.Bambusa.Managers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Conveyor;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Bambusa.Subsystems.Launcher;

public class Robot {
    public Drive drive;
    public Conveyor conveyor;
    public Launcher launcher;

    public Robot(HardwareMap hardwareMap) {
        drive       = Settings.SYSTEM.useDrive      ? new Drive(hardwareMap)        : new Drive();
        conveyor    = Settings.SYSTEM.useConveyor   ? new Conveyor(hardwareMap)     : new Conveyor();
        launcher    = Settings.SYSTEM.useLauncher   ? new Launcher(hardwareMap)     : new Launcher();
    }

    public void init(Telemetry telemetry) {
        drive.init(telemetry);
        conveyor.init(telemetry);
        launcher.init(telemetry);
    }

    public void run(Controls controls) {
        drive.run(controls);
        conveyor.run(controls);
        launcher.run(controls);
    }

    public void stop() {
        drive.stop();
        conveyor.stop();
        launcher.stop();
    }
}
