package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.Bambusa.Helpers.Vector3;
import org.firstinspires.ftc.teamcode.Bambusa.Settings.GameSettings;

import java.util.ArrayList;
import java.util.List;

public class World {
    // Hives
    public static class Hive {
        public GameSettings.ALLIANCES alliance;
        public boolean tipped;

        // These are decimals because we allow Schrödinger's pollen and nectar for calculations
        public double pollen, nectar;

        public Hive(GameSettings.ALLIANCES color) {
            this.alliance = color;

            this.pollen = 0;
            this.nectar = 0;
            this.tipped = false;
        }
    }

    // Nectar and pollen
    public static class Ball {
        public Vector3 pos, vel;
        public GameSettings.BALL_TYPES type;

        public double confidence;
        public boolean inRobot, inHive;

        public Ball(Vector3 pos, GameSettings.BALL_TYPES type) {
            // Position and velocity
            this.pos = pos;
            this.vel = Vector3.zero();

            // Pollen or nectar
            this.type = type;

            // Specific locations
            this.confidence = 1.f;
            this.inRobot = false;
            this.inHive = false;
        }
    }

    // All robots
    public static class Robot {
        public Pose pose;
        public Velocity velocity;
        public GameSettings.ALLIANCES alliance;

        public Robot(Pose pose, GameSettings.ALLIANCES alliance) {
            // Position and velocity
            this.pose = pose;
            this.velocity = Velocity.zero();

            // Alliance color
            this.alliance = alliance;
        }
    }

    // World model storage
    public static Hive redHive, blueHive;
    public static List<Ball> balls;
    public static List<Robot> robots;

    // Initiation (starting guess)
    public static void init() {
        // Creating opponent
        robots = new ArrayList<>(4);

        // Creating balls using default ball position
        // TODO: Put correct initial amount of ball elements.
        balls = new ArrayList<>(12);

        // Creating hives
        redHive = new Hive(GameSettings.ALLIANCES.RED);
        blueHive = new Hive(GameSettings.ALLIANCES.BLUE);
    }
}