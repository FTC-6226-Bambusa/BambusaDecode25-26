package org.firstinspires.ftc.teamcode.Bambusa.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PID {
    public DcMotorEx motor;
    public final ElapsedTime timer = new ElapsedTime();

    public double p, i, d, f;
    public double s, a, v;

    public double lastPower  = 0;
    public double lastError = 0;
    public double lastPos = 0;
    public double lastVel = 0;

    public double ticksPerDegree, horizontalPos;
    public double integralSum = 0;

    /**
     * Creates a PID controller with zeroed gains.
     *
     * @param motor Motor to control
     */
    public PID(DcMotorEx motor) {
        this(motor, 0, 0, 0, 0, 0, 0, 0, 90.0 / 360.0);
    }

    /**
     * Creates a PID controller with basic PIDF gains.
     */
    public PID(DcMotorEx motor, double p, double i, double d, double f) {
        this(motor, p, i, d, f, 0, 0, 0, 90.0 / 360.0);
    }

    /**
     * Creates a PID controller with PIDF gains and ticks-per-degree.
     */
    public PID(DcMotorEx motor,
               double p, double i, double d, double f,
               double ticksPerDegree) {
        this(motor, p, i, d, f, 0, 0, 0, ticksPerDegree);
    }

    /**
     * Full constructor with PID + SAV feedforward.
     */
    public PID(DcMotorEx motor,
               double p, double i, double d, double f,
               double s, double a, double v,
               double ticksPerDegree) {

        this.motor = motor;
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
        this.s = s;
        this.a = a;
        this.v = v;
        this.ticksPerDegree = ticksPerDegree;

        init();
    }

    /**
     * Initialization
     */
    private void init() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        horizontalPos = motor.getCurrentPosition();
        lastPos = horizontalPos;
        timer.reset();
    }

    /**
     * Calculates PID + feedforward output power.
     *
     * @param target Target encoder position (ticks)
     * @return Motor power output
     */
    public double calculatePower(double target) {
        double pos = motor.getCurrentPosition();
        double error = target - pos;

        double dt = Math.max(timer.seconds(), 1e-6);

        double velocity = (pos - lastPos) / dt;
        double acceleration = (velocity - lastVel) / dt;
        double derivative = (error - lastError) / dt;

        double angleDeg = (pos - horizontalPos) / ticksPerDegree;
        double cosineFF = Math.cos(Math.toRadians(angleDeg));
        double staticFF = Math.signum(error);

        double output =
                (p * error) +
                        (i * integralSum) +
                        (d * derivative) +
                        (f * cosineFF) +
                        (v * velocity) +
                        (a * acceleration) +
                        (s * staticFF);

        integralSum += error * dt;

        lastError = error;
        lastPos = pos;
        lastVel = velocity;
        lastPower = output;

        timer.reset();
        return output;
    }

    /**
     * Applies PID power to move motor toward target.
     *
     * @param target Target encoder position (ticks)
     */
    public void moveTo(double target) {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setPower(calculatePower(target));
    }

    /**
     * Sets motor power directly.
     *
     * @param power Motor power [-1, 1]
     */
    public void setPower(double power) {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setPower(power);
        lastPower = power;
    }

    /**
     * Applies cosine feedforward only (useful for tuning).
     */
    public void levitate() {
        double pos = motor.getCurrentPosition();
        double angleDeg = (pos - horizontalPos) / ticksPerDegree;
        motor.setPower(Math.abs(Math.cos(Math.toRadians(angleDeg))));
    }

    /**
     * Disables PID and floats the motor.
     */
    public void disable() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setPower(0);
    }

    /**
     * Actively brakes the motor.
     */
    public void brake() {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setPower(0);
    }

    /**
     * Sets PIDF gains.
     */
    public void setPID(double p, double i, double d, double f) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
    }

    /**
     * Sets static, acceleration, and velocity feedforward gains.
     */
    public void setSAV(double s, double a, double v) {
        this.s = s;
        this.a = a;
        this.v = v;
    }

    /**
     * Sets horizontal reference position.
     */
    public void setHorizontalPos(double pos) {
        this.horizontalPos = pos;
    }

    /**
     * Sets horizontal reference to current motor position.
     */
    public void setHorizontalPos() {
        this.horizontalPos = motor.getCurrentPosition();
    }

    /**
     * Sets encoder ticks per degree.
     */
    public void setTicksPerDegree(double tpd) {
        this.ticksPerDegree = tpd;
    }

    /**
     * Manually jogs motor to help determine encoder targets.
     *
     * @param speed Motor power
     * @param forward Move forward
     * @param backward Move backward
     * @return Current encoder position
     */
    public double testPosition(double speed, boolean forward, boolean backward) {
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setPower(forward ? speed : (backward ? -speed : 0));
        return motor.getCurrentPosition();
    }

    /**
     * Converts degrees to encoder ticks.
     *
     * @param degrees Angle in degrees
     * @return Encoder ticks
     */
    public double degreesToTicks(double degrees) {
        return degrees * ticksPerDegree;
    }

    /**
     * Determines if the mechanism is at target.
     *
     * @param maxError Maximum allowed error (ticks)
     * @param maxVelocity Maximum allowed velocity (ticks/sec)
     * @return True if within tolerance
     */
    public boolean isAtTarget(double maxError, double maxVelocity) {
        return Math.abs(lastError) < maxError && Math.abs(lastVel) < maxVelocity;
    }
}
