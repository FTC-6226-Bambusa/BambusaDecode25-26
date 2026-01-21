package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LauncherConfig;

public class Intake {
    public Servo servo;

    /**
     * The intake servo
     * @param servo the intake servo
     */
    public Intake(Servo servo) {
        this.servo = servo;
    }

    /**
     * Turns on motor
     */
    public void enable() {
        servo.setPosition(LauncherConfig.intakeSpeed + 0.5);
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
