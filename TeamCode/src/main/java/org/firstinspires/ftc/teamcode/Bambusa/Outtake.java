package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LauncherConfig;

public class Outtake {
    public Servo servo;

    public Outtake(Servo servo) {
        this.servo = servo;
    }

    /**
     * Turns on motor
     */
    public void enable() {
        servo.setPosition(LauncherConfig.outtakeSpeed + 0.5);
    }

    /**
     * Turns off motor
     */
    public void disable() {
        servo.setPosition(0.5);
    }

    /**
     * Sets motor direction to reverse
     */
    public void reverse() {
        servo.setDirection(LauncherConfig.outtakeDirection == Servo.Direction.FORWARD ?
                                                              Servo.Direction.REVERSE :
                                                              Servo.Direction.FORWARD);
        enable();
    }

    /**
     * Sets motor direction to forward
     */
    public void forward() {
        servo.setDirection(LauncherConfig.outtakeDirection);
        enable();
    }
}
