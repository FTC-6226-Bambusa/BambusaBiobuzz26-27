package org.firstinspires.ftc.teamcode.Bambusa.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class PIDF {
    public DcMotorEx motor;
    public double p, i, d, f;

    private double integral = 0;
    private double lastError = 0;
    private double lastTarget = 0;

    private final ElapsedTime timer = new ElapsedTime();

    public PIDF(DcMotorEx motor, double p, double i, double d, double f) {
        this.motor = motor;

        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;

        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        timer.reset();
    }
    
    public double setVelocity(double target) {
        // Reset integral if target changes or motor is stopped
        if (target != lastTarget || target == 0) {
            integral = 0;
            lastTarget = target;
        }

        if (target == 0) {
            motor.setPower(0);
            return 0;
        }

        double seconds = timer.seconds();
        timer.reset();

        // Prevent weird timer errors and divide by zero
        if (seconds <= 0 || seconds > 0.2) seconds = 0.001;

        // Proportion calculation
        double velocity = motor.getVelocity();
        double error = target - velocity;

        // Integral calculation
        integral += error * seconds;
        integral = Range.clip(integral, -2000, 2000);

        // Derivative calculation
        double derivative = (error - lastError) / seconds;
        lastError = error;

        // Calculate output
        double output = (p * error) + (i * integral) + (d * derivative) + (f * target);
        double power = Range.clip(output, -1.0, 1.0);
        motor.setPower(power);

        return power;
    }

    // Resets loop variables
    public void reset() {
        integral = 0;
        lastError = 0;
        timer.reset();
    }
}