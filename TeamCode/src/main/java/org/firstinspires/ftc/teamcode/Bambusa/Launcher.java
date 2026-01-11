package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Launcher {
    public DcMotor motor;

    /**
     * The launching motor
     * @param motor the launcher motor
     */
    public Launcher(DcMotor motor) {
        this.motor = motor;

        init();
    }

    public void init() {
        motor.setDirection(LauncherConfig.launcherDirection);
    }

    /**
     * Turns on motor
     */
    public void enable() {
        motor.setPower(LauncherConfig.launcherBoostSpeed);
    }

    /**
     * Turns off motor
     */
    public void disable() {
        motor.setPower(0);
    }

    /**
     *
     * @param power Power to send to motor
     */
    public void setPower(double power) {
        motor.setPower(power);
    }

    /**
     * If user presses normal speed button, the motor is
     * set to motor speed. Pressing the boost speed button
     * will set the code to boost speed.
     *
     * @param normalSpeed less speed
     * @param boostSpeed more speed
     */
    public void setPower(boolean normalSpeed, boolean boostSpeed) {
        double totalPower = 0;

        if (normalSpeed) {
            totalPower += LauncherConfig.launcherNormalSpeed;

            if (boostSpeed) {
                totalPower += LauncherConfig.launcherBoostSpeed;
            }
        }

        motor.setPower(totalPower);
    }
}
