package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LauncherConfig;

public class Intake {
    public Servo servo;
    public double speed;

    /**
     * The intake servo
     * @param servo the intake servo
     */
    public Intake(Servo servo, double speed) {
        this.servo = servo;
        this.speed = speed;
    }

    /**
     * Turns on motor
     */
    public void enable() {
        servo.setPosition(speed + 0.5);
    }

    public void disable() {
        servo.setPosition(0.5);
    }

    /**
     * Sets motor direction to reverse
     */
    public void reverse() {
        servo.setDirection(LauncherConfig.intakeDirection == Servo.Direction.FORWARD ?
                                                             Servo.Direction.REVERSE :
                                                             Servo.Direction.FORWARD);
        enable();
    }

    /**
     * Sets motor direction to forward
     */
    public void forward() {
        servo.setDirection(LauncherConfig.intakeDirection);
        enable();
    }
}
