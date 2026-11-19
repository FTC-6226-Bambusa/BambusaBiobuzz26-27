package org.firstinspires.ftc.teamcode.Bambusa.Config;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class Settings {
    public static SystemSettings        SYSTEM          = new SystemSettings();
    public static GameSettings          GAME            = new GameSettings();
    public static DebugSettings         DEBUG           = new DebugSettings();
    public static DriveSettings         DRIVE           = new DriveSettings();
    public static LauncherSettings      LAUNCHER        = new LauncherSettings();
    public static ConveyorSettings      CONVEYOR        = new ConveyorSettings();
    public static LocalizationSettings  LOCALIZATION    = new LocalizationSettings();
    public static USBSettings           USB             = new USBSettings();
    public static AutoSettings          AUTO            = new AutoSettings();
}