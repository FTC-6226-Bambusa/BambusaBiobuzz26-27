package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class MathPlus {
    // Returns t of the way from a to b
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    // Normalizes field position
    public static double normalizeFieldPosition(double pos) {
        return pos / Settings.GAME.fieldSize;
    }
}
