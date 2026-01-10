package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Outtake {
    public DcMotor motor;

    /**
     * The launching motor
     * @param motor
     */
    public Outtake(DcMotor motor) {
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
}
