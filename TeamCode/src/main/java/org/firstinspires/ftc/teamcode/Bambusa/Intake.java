package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Intake {
    public DcMotor motor;

    /**
     * The launching motor
     * @param motor
     */
    public Intake(DcMotor motor) {
        this.motor = motor;
    }

    /**
     * Turns on motor
     */
    public void enable() {
        motor.setPower(LauncherConfig.launcherSpeed);
    }

    /**
     * Turns off motor
     */
    public void disable() {
        motor.setPower(0);
    }

    /**
     * Sets motor direction to reverse
     */
    public void reverse() {
        motor.setDirection(LauncherConfig.intakeDirection == DcMotorSimple.Direction.FORWARD ?
                                                             DcMotorSimple.Direction.REVERSE :
                                                             DcMotorSimple.Direction.FORWARD);
    }

    /**
     * Sets motor direction to forward
     */
    public void forward() {
        motor.setDirection(LauncherConfig.intakeDirection);
    }
}
