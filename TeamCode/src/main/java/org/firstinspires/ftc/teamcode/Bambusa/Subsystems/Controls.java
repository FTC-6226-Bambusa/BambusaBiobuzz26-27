package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Gamepad;

@Configurable
public class Controls {
    public Gamepad g1;
    public Gamepad g2;

    public Controls(Gamepad gamepad1, Gamepad gamepad2) {
        g1 = gamepad1;
        g2 = gamepad2;
    }

    // Driving
    public double forward() {
        return -g1.left_stick_y;
    }

    public double strafe() {
        return g1.left_stick_x;
    }

    public double turn() {
        return g1.right_stick_x;
    }

    public double boost() {
        return g1.left_trigger;
    }

    public boolean resetYaw() {
        // Resets field centric driving forward direction
        return g1.dpad_down || g1.dpad_up || g1.dpad_left || g1.dpad_right;
    }

    // Conveyor
    public boolean shoot() {
        // i dunno ill talk with drivers
        return false;
    }

    // Fairwater
    public boolean killSentience() {
        return g1.a && g2.a;
    }

    // TODO: Utilize gamepad rumble to signal drivers
}
