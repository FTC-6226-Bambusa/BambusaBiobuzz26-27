package org.firstinspires.ftc.teamcode.Bambusa.Settings;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class Settings {
    public static SystemSettings SYSTEM = new SystemSettings();
    public static DriveSettings DRIVE = new DriveSettings();
    public static LocalizationSettings LOCALIZATION = new LocalizationSettings();
    public static DebugSettings DEBUG = new DebugSettings();
    public static GameSettings GAME = new GameSettings();
    public static LauncherSettings LAUNCHER = new LauncherSettings();
    public static WaypointSettings WAYPOINTS = new WaypointSettings();
    public static ConveyorSettings CONVEYOR = new ConveyorSettings();
}
