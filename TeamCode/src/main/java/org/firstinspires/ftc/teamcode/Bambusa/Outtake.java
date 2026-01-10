package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {
    public Servo servo;

    /**
     * The launching motor
     * @param motor
     */
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
     * Reverses motor well
     */
    public void reverse() {
        servo.setDirection(LauncherConfig.outtakeDirection == Servo.Direction.FORWARD ?
                                                              Servo.Direction.REVERSE :
                                                              Servo.Direction.FORWARD);
    }
}
