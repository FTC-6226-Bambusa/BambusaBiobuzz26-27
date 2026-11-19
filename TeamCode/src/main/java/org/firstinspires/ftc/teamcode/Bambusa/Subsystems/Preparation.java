package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Config.GameSettings;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;

import java.util.List;

public class Preparation {
    private final List<LynxModule> hubs;

    public Preparation(HardwareMap hardwareMap) {
        hubs = hardwareMap.getAll(LynxModule.class);
    }

    public void init(Telemetry telemetry) {
        // Enable manual bulk caching
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(
                    Settings.SYSTEM.useManualBulkCaching
                    ? LynxModule.BulkCachingMode.MANUAL
                    : LynxModule.BulkCachingMode.AUTO
            );
        }

        // Displaying alliance
        showAllianceSelection(telemetry);
    }

    public void init_loop(Controls controls) {
        // Switching alliance
        if (controls.switchAlliance()) Settings.GAME.alliance = Settings.GAME.alliance.toggle();
    }

    public void run() {
        // Read hub snapshots in bulk if enabled
        if (Settings.SYSTEM.useManualBulkCaching)
            for (LynxModule hub : hubs) hub.clearBulkCache();
    }

    private void showAllianceSelection(Telemetry telemetry) {
        telemetry.addData(TelemetryPlus.title("Alliance Selection"), "").setRetained(true);
        telemetry.addData("Alliance Selected: ", () -> Settings.GAME.alliance.toString());
        telemetry.addData(TelemetryPlus.separator(), "").setRetained(true);
    }
}
