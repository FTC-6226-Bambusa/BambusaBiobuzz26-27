package org.firstinspires.ftc.teamcode.Bambusa.Config;

public class DebugSettings {
    public DebugStyle STYLE = new DebugStyle();

    // Systems
    public LauncherDebug        LAUNCHER = new LauncherDebug();
    public LocalizationDebug    LOCALIZATION = new LocalizationDebug();
    public DriveDebug           DRIVE = new DriveDebug();
    public ConveyorDebug        CONVEYOR = new ConveyorDebug();


    public static class DebugStyle {
        public int separatorLength = 35;
        public String separatorChar = "*";
    }

    public static class LauncherDebug {
        public boolean debugConnection = true;
        public boolean displayPower = true;
        public boolean displayVelocity = true;
    }

    public static class LocalizationDebug {
        public boolean logRobotPosition = true;
    }

    public static class DriveDebug {
        public boolean debugConnection = true;
        public boolean debugPowerConsumption = false;
    }

    public static class ConveyorDebug {
        public boolean debugConnection = true;
        public boolean displayPower = true;
    }
}
