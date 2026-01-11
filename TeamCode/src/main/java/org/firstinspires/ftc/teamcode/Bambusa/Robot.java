package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

        drive = new MecanumDrive(imu, fr, fl, bl, br);

        // Launcher
        launcher = new Launcher(hardwareMap.dcMotor.get(LauncherConfig.launcher));

        // Intake
        intake = new Intake(hardwareMap.get(Servo.class, LauncherConfig.intake));

        // Outtake
        outtake = new Outtake(hardwareMap.get(Servo.class, LauncherConfig.outtake));
    }

    public void init() {
        intake.enable();
        outtake.enable();
    }

    /**
     * Performs all necessary actions
     */
    public void run(Telemetry telemetry) {
        // Driving
        drive.run(gamepad1.left_stick_x,
                  gamepad1.left_stick_y,
                  gamepad1.right_stick_x,
                  gamepad1.right_trigger,
                  gamepad1.dpad_left || gamepad1.dpad_right ||
                           gamepad1.dpad_down || gamepad1.dpad_up);

        // Launcher control
        launcher.setPower(gamepad2.y, gamepad2.right_trigger > 0.1);

//         if (gamepad2.left_bumper) {
//             telemetry.addData("HERE, getting coords", "");
//             double[] yaw_and_power = getCoords(0,0);
//             assert yaw_and_power != null;
//            double yaw = yaw_and_power[0];
//            double power = yaw_and_power[0];
//            launcher.setPower(power/1000);
//            telemetry.addData("yaw: ", yaw);
//         }

        // Intake control
        if (gamepad2.right_stick_y > 0.1) {
            intake.reverse();
        } else {
            intake.forward();
        }

        // Outtake control
        if (gamepad2.left_stick_y > GamepadConfig.stickThreshold) {
            outtake.reverse();
        } else if (gamepad2.left_stick_y < -GamepadConfig.stickThreshold) {
            outtake.forward();
        } else {
            outtake.disable();
        }

        // Failsafe (complete shutoff)
        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            drive.stop();
            intake.disable();
            outtake.disable();
            launcher.disable();
        }
    }
}
