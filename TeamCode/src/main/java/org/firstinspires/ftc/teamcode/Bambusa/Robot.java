package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.DriveConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LauncherConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.StartConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.MathPlus;
import org.firstinspires.ftc.teamcode.Bambusa.RottenMustard.LincolnsRottenMustard;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Robot {
    public Controls controls;

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
        controls = new Controls(gamepad1, gamepad2);

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

        // teleInit isn't used because we don't want motors to run during startup
    }

    /**
     * Robot class for Auto
     *
     * @param hardwareMap hardware map
     */
    public Robot(HardwareMap hardwareMap) {
        // Launcher motors
        launcher = new Launcher(hardwareMap.dcMotor.get(LauncherConfig.launcher));
        intake = new Intake(hardwareMap.get(Servo.class, LauncherConfig.intake));
        outtake = new Outtake(hardwareMap.get(Servo.class, LauncherConfig.outtake));

        // Initializes for auto since no gamepads are passed in
        autoInit();
    }

    // Initializes for tele
    public void teleInit() {
        intake.enable();
    }

    // Initializes for auto
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

        // Disables robot
        if (controls.disableRobot()) {
            disable();
            return;
        }

        // Drive inputs
        double turn = controls.turn();

        // Auto aiming
        if (controls.aim()) {
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

        // Driving
        drive.run(controls.strafe(), controls.forward(), turn, controls.boost(), controls.resetIMU());

        // Intake control
        if (controls.reverseIntake()) {
            intake.reverse();
        } else {
            intake.forward();
        }

        // Outtake control
        if (controls.reverseOuttake()) {
            outtake.reverse();
        } else if (controls.powerOuttake()) {
            outtake.forward();
        } else {
            outtake.disable();
        }

        // Launcher control
        launcher.setPower(controls.launch(), controls.launchBoost());
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
