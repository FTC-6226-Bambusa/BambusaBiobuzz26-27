package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Config.Settings;

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
    }

    public void init_loop(Telemetry telemetry) {
        // TODO: Add alliance and position selection
    }

    public void run(Telemetry telemetry) {
        // Read hub snapshots in bulk if enabled
        if (Settings.SYSTEM.useManualBulkCaching)
            for (LynxModule hub : hubs) hub.clearBulkCache();
    }

    public void stop(Telemetry telemetry) {
        // This, for example, would store data.
    }
}
