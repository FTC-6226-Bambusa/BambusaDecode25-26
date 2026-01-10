package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    public Servo servo;

    /**
     * The launching motor
     * @param motor
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

    /**
     * Turns off motor
     */
    public void disable() {
        servo.setPosition(0);
    }

    /**
     * Sets motor direction to reverse
     */
    public void reverse() {
        servo.setDirection(LauncherConfig.intakeDirection == Servo.Direction.FORWARD ?
                                                             Servo.Direction.REVERSE :
                                                             Servo.Direction.FORWARD);
    }

    /**
     * Sets motor direction to forward
     */
    public void forward() {
        servo.setDirection(LauncherConfig.intakeDirection);
    }
}
