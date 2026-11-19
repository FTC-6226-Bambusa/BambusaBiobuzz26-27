package org.firstinspires.ftc.teamcode.Bambusa.Config;

public class GameSettings {
    // Field size in inches
    public double fieldSize = 12 * 12;

    // Alliance
    public ALLIANCES alliance = ALLIANCES.BLUE;
    public enum ALLIANCES {
        BLUE, RED;

        // Utility
        public ALLIANCES toggle() {
            return (this == RED) ? BLUE : RED;
        }
    }
}
