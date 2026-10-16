package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import org.firstinspires.ftc.teamcode.Bambusa.Settings.Settings;

public class Waypoints {
    public static class Waypoint {
        public double x, y;
        public double weight;

        public Waypoint(double x, double y, double weight) {
            this.x = x;
            this.y = y;
            this.weight = weight;
        }
    }

    // Represents a 2D grid of waypoints
    public static Waypoint[][] waypoints;

    public static void init() {
        // Creating the waypoint grid
        waypoints = new Waypoint[Settings.WAYPOINTS.waypointDensity][Settings.WAYPOINTS.waypointDensity];
        for (int x = 0; x < waypoints.length; x++) {
            for (int y = 0; y < waypoints.length; y++) {
                waypoints[x][y] = new Waypoint((double) x / waypoints.length,
                                               (double) y / waypoints.length,
                                               1);
            }
        }

        // Setting illegal waypoints
        for (int[] point : Settings.WAYPOINTS.illegalWaypoints) {
            waypoints[point[0]][point[1]].weight = 0;
        }
    }
}
