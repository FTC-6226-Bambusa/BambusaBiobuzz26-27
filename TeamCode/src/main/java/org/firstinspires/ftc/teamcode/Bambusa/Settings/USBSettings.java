package org.firstinspires.ftc.teamcode.Bambusa.Settings;

public class USBSettings {
    public PortSettings PORTS = new PortSettings();
    public DataSettings DATA = new DataSettings();

    public static class PortSettings {
        // TODO: This is a placeholder, replace with the actual limelight ports
        public int LL1 = 8888;
    }

    public static class DataSettings {
        public int TIMEOUT = 20;
    }
}
