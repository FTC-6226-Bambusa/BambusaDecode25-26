package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.RottenMustard.LincolnsRottenMustard;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Robot {
    public Gamepad gamepad1, gamepad2;

    public MecanumDrive drive;
    public Launcher launcher;
    public Intake intake;
    public Outtake outtake;

    public Follower follower;

    /**
     * Robot class for Tele
     *
     * @param hardwareMap hardware map
     * @param gamepad1    game controller 1
     * @param gamepad2    game controller 2
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

        // Follower
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(StartConfig.pose);
    }

    /**
     * Robot class for Auto
     *
     * @param hardwareMap hardware map
     */
    public Robot(HardwareMap hardwareMap) {
        launcher = new Launcher(hardwareMap.dcMotor.get(LauncherConfig.launcher));
        intake = new Intake(hardwareMap.get(Servo.class, LauncherConfig.intake));
        outtake = new Outtake(hardwareMap.get(Servo.class, LauncherConfig.outtake));

        autoInit();
    }

    public void teleInit() {
        intake.enable();
    }

    public void autoInit() {
        intake.enable();
        launcher.setPower(LauncherConfig.launcherAutoSpeed);
    }

    /**
     * Performs all necessary actions
     */
    public void run(Telemetry telemetry) {
        // Update localizer
        follower.poseTracker.update();

        // Drive inputs
        double forward = gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        // Auto aiming
        if (gamepad2.right_bumper) {
            drive.enabled = true;

            double x = follower.getPose().getX();
            double z = follower.getPose().getY();

            double changed_x = (x - 72) / 12;
            double changed_z = (z - 72) / 12;

            double[] choices = LincolnsRottenMustard.getCoords(changed_x, changed_z);

            if (choices != null) {
                double targetYaw = Math.toRadians(choices[0] + 45); // Target Angle
                double launcherPower = choices[1];

                double currentYaw = follower.getPose().getHeading();
                double difference = MathPlus.angleWrap(targetYaw - currentYaw);

                turn = -difference * 2.0;
                turn = Math.max(-1.0, Math.min(1.0, turn));

                launcher.setPower(launcherPower);

                telemetry.addData("X", changed_x);
                telemetry.addData("Z", changed_z);
                telemetry.addData("Mode", "Auto-Aiming");
                telemetry.addData("Target Heading", Math.toDegrees(targetYaw));
                telemetry.addData("Turn Correction", turn);
                telemetry.addData("Launcher Power", launcherPower / 10);
            }

        } else {
            drive.enabled = true;
        }

        boolean dpad = gamepad1.dpad_left || gamepad1.dpad_right || gamepad1.dpad_down || gamepad1.dpad_up;
        drive.run(strafe, forward, turn, gamepad1.right_trigger, dpad);

        // Intake control
        if (gamepad2.right_stick_y > GamepadConfig.stickThreshold) {
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
    }

    /**
     * Disables all robot motors
     */
    public void disable() {
        drive.stop();

        disableLaunchers();
    }

    /**
     * Disables all shooting motors
     */
    public void disableLaunchers() {
        intake.disable();
        outtake.disable();
        launcher.disable();
    }
}
