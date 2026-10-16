package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.TelemetryPlus;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Fairwater {
    Follower follower;
    private boolean enabled = true;

    public Fairwater(HardwareMap hardwareMap) {
        this.follower = Constants.create(hardwareMap);
    }

    public void init() {
        // Initiating world model and waypoint system
        Waypoints.init();
        World.init();

        // TODO: Set follower pose here at the start
    }

    public void run(Telemetry telemetry, Controls controls) {
        // Killing sentience if things go bad...
        if (!enabled || controls.killSentience()) {
            enabled = false;
            return;
        }

        // Updating follower position
        follower.localizer.update();

        // Debugging localization
        if (Settings.DEBUG.LOCALIZATION.logRobotPosition) {
            telemetry.addLine(TelemetryPlus.title("POSITION"));
            telemetry.addData("X: ", follower.pose().x());
            telemetry.addData("Y: ", follower.pose().y());
            telemetry.addData("A: ", follower.pose().heading());
            telemetry.addLine(TelemetryPlus.separator());
        }
    }
}
