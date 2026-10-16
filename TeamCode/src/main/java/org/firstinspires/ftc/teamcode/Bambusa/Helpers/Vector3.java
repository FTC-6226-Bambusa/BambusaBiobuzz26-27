package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import androidx.annotation.NonNull;

public class Vector3 {
    public double x;
    public double y;
    public double z;

    // Constructor
    public Vector3() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector3(Number x, Number y, Number z) {
        this.x = x.doubleValue();
        this.y = y.doubleValue();
        this.z = z.doubleValue();
    }

    // Copy constructor
    public Vector3(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    // Math operations
    public Vector3 add(Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    // Scalar
    public Vector3 scale(Number scalar) {
        double s = scalar.doubleValue();
        return new Vector3(this.x * s, this.y * s, this.z * s);
    }

    // Dot product
    public double dot(Vector3 other) {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
    }

    // Cross product
    public Vector3 cross(Vector3 other) {
        return new Vector3(
                (this.y * other.z) - (this.z * other.y),
                (this.z * other.x) - (this.x * other.z),
                (this.x * other.y) - (this.y * other.x)
        );
    }

    // Properties
    public double magnitude() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    // Normalization
    public Vector3 normalize() {
        double mag = magnitude();
        if (mag == 0) {
            return new Vector3();
        }
        return new Vector3(this.x / mag, this.y / mag, this.z / mag);
    }

    public void set(Number x, Number y, Number z) {
        this.x = x.doubleValue();
        this.y = y.doubleValue();
        this.z = z.doubleValue();
    }

    // Debugging
    @NonNull
    @Override
    public String toString() {
        return String.format("Vector3(%.3f, %.3f, %.3f)", x, y, z);
    }

    // Defaults
    public static Vector3 zero() {
        return new Vector3();
    }

    public static Vector3 one() {
        return new Vector3(1, 1, 1);
    }
}