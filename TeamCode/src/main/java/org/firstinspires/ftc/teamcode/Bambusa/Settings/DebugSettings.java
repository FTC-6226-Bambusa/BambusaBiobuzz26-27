package org.firstinspires.ftc.teamcode.Bambusa.Settings;

public class DebugSettings {
    public DebugStyle STYLE = new DebugStyle();
    public LocalizationDebug LOCALIZATION = new LocalizationDebug();
    public PowerDebug POWER = new PowerDebug();
    public TurretDebug TURRET = new TurretDebug();

    public static class DebugStyle {
        public int separatorLength = 35;
        public String separatorChar = "*";
    }

    public static class TurretDebug {
        public boolean displayPower = true;
        public boolean displayVelocity = true;
    }

    public static class LocalizationDebug {
        public boolean logRobotPosition = true;
    }

    public static class PowerDebug {
        public boolean debugDrivePowerConsumption = false;
        public boolean debugConveyorPowerConsumption = false;
    }
}
