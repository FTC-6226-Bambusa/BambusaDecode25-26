package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class Robot {
    public Gamepad gamepad1, gamepad2;

    public MecanumDrive drive;
    public Launcher launcher;
    public Intake intake;
    public Outtake outtake;

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

        // Launcher
        launcher = new Launcher(hardwareMap.dcMotor.get(LauncherConfig.launcher));

        // Intake
        intake = new Intake(hardwareMap.get(Servo.class, LauncherConfig.intake));

        // Outtake
        outtake = new Outtake(hardwareMap.get(Servo.class, LauncherConfig.outtake));
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

        // Launcher control
        launcher.setPower(gamepad2.y, gamepad2.right_trigger > 0.1);
        launcher.setPower(1);

        // Intake control
        if (gamepad2.dpad_down) {
            intake.reverse();
        }

        // Outtake control
        if (gamepad2.dpad_up) {
            outtake.enable();
        }
    }
}
