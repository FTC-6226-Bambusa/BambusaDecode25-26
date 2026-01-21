package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.Gamepad;

@Configurable
public class Controls {
    // Gamepads
    private Gamepad gamepad1;
    private Gamepad gamepad2;

    // Editable Constants
    public static double stickThreshold = 0.1;

    public Controls(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    /** --- TELEOP CONTROLS --- **/

    // Drive strafe
    public double strafe() {
        return gamepad1.left_stick_y;
    }

    // Drive forward
    public double forward() {
        return gamepad1.left_stick_x;
    }

    // Drive turn
    public double turn() {
        return gamepad1.right_stick_x;
    }

    // Drive boosting speed
    public double boost() {
        return gamepad1.right_trigger;
    }

    // Drive resetting IMU
    public boolean resetIMU() {
        return gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right || gamepad1.dpad_up;
    }

    // Drive aiming toward goal
    public boolean aim() {
        return gamepad2.right_bumper;
    }

    // Intake reversing
    public boolean reverseIntake() {
        return gamepad2.right_stick_y > stickThreshold;
    }

    // Outtake reversing
    public boolean reverseOuttake() {
        return gamepad2.left_stick_y > stickThreshold;
    }

    // Outtake powering
    public boolean powerOuttake() {
        return gamepad2.left_stick_y < -stickThreshold;
    }

    // Disables robot
    public boolean disableRobot() {
        return gamepad1.left_bumper || gamepad2.left_bumper;
    }

    // Enables launch
    public boolean launch() {
        return gamepad2.y;
    }

    public boolean launchBoost() {
        return gamepad2.left_trigger > stickThreshold;
    }

    /** --- AUTO CONTROLS DURING INIT --- **/

    // Toggles alliance colors
    public boolean toggleAllianceColor() {
        return gamepad1.yWasPressed();
    }

    public boolean toggleStartingPosition() {
        return gamepad1.aWasPressed();
    }
}