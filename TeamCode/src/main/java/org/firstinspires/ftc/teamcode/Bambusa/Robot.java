package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class Robot {
    public Gamepad gamepad1, gamepad2;

    public MecanumDrive drive;

    /**
     * Robot class (manages everything)
     *
     * @param hardwareMap hardware map
     * @param gamepad1 game controller 1
     * @param gamepad2 game controller 2
     */
    public Robot(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        // Gamepads
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        // IMU
        IMU imu = hardwareMap.get(IMU.class, DriveConfig.imu);

        // Wheels
        DcMotor fl = hardwareMap.get(DcMotor.class, DriveConfig.frontLeftMotor);
        DcMotor fr = hardwareMap.get(DcMotor.class, DriveConfig.frontRightMotor);
        DcMotor bl = hardwareMap.get(DcMotor.class, DriveConfig.backLeftMotor);
        DcMotor br = hardwareMap.get(DcMotor.class, DriveConfig.backRightMotor);

        drive = new MecanumDrive(imu, fl, fr, bl, br);
    }

    /**
     * Performs all necessary actions
     */
    public void run() {
        // Driving
        drive.run(gamepad1.left_stick_y,
                  gamepad1.left_stick_x,
                  gamepad1.right_stick_y,
                  gamepad1.left_trigger,
                  gamepad1.dpad_left);
    }
}
