package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controls {
    public Gamepad g1, g2;

    public Controls(Gamepad g1, Gamepad g2) {
        this.g1 = g1;
        this.g2 = g2;
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
    public boolean takeout() {
        return false;
    }

    // Launcher
    public boolean shoot() {
        return g1.right_trigger_pressed;
    }

    // Preparation
    public boolean switchAlliance() {
        return g1.aWasPressed() || g2.aWasPressed();
    }
}
