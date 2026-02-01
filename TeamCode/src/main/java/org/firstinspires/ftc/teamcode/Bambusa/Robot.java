package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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
import org.firstinspires.ftc.teamcode.Bambusa.RottenMustard.SlurpSlurp;
import org.firstinspires.ftc.teamcode.Bambusa.SLURPIE.Artifact;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.List;

public class Robot {
    public Limelight3A LL;
    public Controls controls;

    public MecanumDrive drive;
    public Launcher launcher;
    public Intake intake;
    public Outtake outtake;
    public Follower follower;
    public Artifact artifact;

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

        // Limelight
        LL = hardwareMap.tryGet(Limelight3A.class, "lemonlight");

        // IMU
        IMU imu = hardwareMap.get(IMU.class, DriveConfig.imu);

        // Wheels
        DcMotor fl = hardwareMap.get(DcMotor.class, DriveConfig.frontLeftMotor);
        DcMotor fr = hardwareMap.get(DcMotor.class, DriveConfig.frontRightMotor);
        DcMotor bl = hardwareMap.get(DcMotor.class, DriveConfig.backLeftMotor);
        DcMotor br = hardwareMap.get(DcMotor.class, DriveConfig.backRightMotor);

        drive = new MecanumDrive(imu, fr, fl, bl, br);

        // Launcher
        launcher = new Launcher(hardwareMap.get(DcMotorEx.class, LauncherConfig.launcher));

        // Intake
        intake = new Intake(hardwareMap.get(Servo.class, LauncherConfig.intake));

        // Outtake
        outtake = new Outtake(hardwareMap.get(Servo.class, LauncherConfig.outtake));

        // Follower
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(StartConfig.pose);

        artifact = new Artifact();

        // teleInit isn't used because we don't want motors to run during startup
    }

    /**
     * Robot class for Auto
     *
     * @param hardwareMap hardware map
     */
    public Robot(HardwareMap hardwareMap) {
        // Launcher motors
        launcher = new Launcher(hardwareMap.get(DcMotorEx.class, LauncherConfig.launcher));
        intake = new Intake(hardwareMap.get(Servo.class, LauncherConfig.intake));
        outtake = new Outtake(hardwareMap.get(Servo.class, LauncherConfig.outtake));

        // Initializes for auto since no gamepads are passed in
        autoInit();
    }

    // Initializes for tele
    public void teleInit() {
        intake.enable();

        // Limelight settings
        LL.setPollRateHz(100);
        LL.pipelineSwitch(0);

        // Starting limelight
        LL.start();
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
            boolean flip = StartConfig.color == StartConfig.AllianceColor.RED;

            double x = follower.getPose().getY();
            double z = follower.getPose().getX();

            double changed_x = (x - 72) / 12;
            double changed_z = (z - 72) / 12;

            double[] choices = LincolnsRottenMustard.getCoords(changed_x, changed_z);

            if (choices != null) {
                double targetYaw = Math.toRadians(choices[0] + 225); // Target Angle

                double launcherPower = choices[1] * LauncherConfig.LRMLauncherSpeedFactor + LauncherConfig.LRMLauncherSpeedBias;

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
                telemetry.addData("Launcher Power", launcherPower);
            }

        } else {
            drive.enabled = true;
        }

        // Slurpie running
        if (controls.slurp()) {
            double[] LLoutput = getLimelightArtifacts();

            // Noise smoothing
            artifact.smoothData(LLoutput);

            if (LLoutput[0] != -1) {
                drive.enabled = false;

                // Running slurpie
                double[] slurpieOutput = SlurpSlurp.letsGetSlurpy(LLoutput[0], LLoutput[1], LLoutput[2]);

                // Limelight outputs
                telemetry.addLine("LIMELIGHT OUTPUT: ");
                telemetry.addData("X: ", LLoutput[0]);
                telemetry.addData("Y: ", LLoutput[1]);
                telemetry.addData("A: ", LLoutput[2]);

                // Slurpie outputs (currently bugged)
                telemetry.addLine("SLURPIE OUTPUT: ");
                telemetry.addData("Forward: ", slurpieOutput[0]);
                telemetry.addData("Strafe: ", slurpieOutput[1]);
                telemetry.addData("Turn: ", slurpieOutput[2]);
                telemetry.addData("Boost: ", slurpieOutput[3]);

                // Driving with slurpie
                 if ((slurpieOutput != null ? slurpieOutput[0] : -1) != -1) {
                     drive.runWithoutIMU(slurpieOutput[1], slurpieOutput[0], slurpieOutput[2], slurpieOutput[3]);
                 }
            } else {
                drive.enabled = true;
            }
        } else {
            drive.enabled = true;
        }

        // Driving
        if (DriveConfig.useFieldCentricDrive) {
            drive.run(controls.strafe(), controls.forward(), turn, controls.boost(), controls.resetIMU());
        } else {
            drive.runWithoutIMU(controls.strafe(), controls.forward(), turn, controls.boost());
        }

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
     * Helper class which gets Limelight output
     */
    public double[] getLimelightArtifacts() {
        LLResult result = LL.getLatestResult();

        if (result != null && result.isValid()) {
            List<LLResultTypes.DetectorResult> detections = result.getDetectorResults();

            if (detections != null && !detections.isEmpty()) {
                LLResultTypes.DetectorResult closestObject = null;
                double maxBoxSize = -1;

                for (LLResultTypes.DetectorResult detection : detections) {
                    if (detection.getTargetArea() > maxBoxSize) {
                        maxBoxSize = detection.getTargetArea();
                        closestObject = detection;
                    }
                }

                // Log the values for the closest object found
                if (closestObject != null) {
                    return new double[]{closestObject.getTargetXDegrees(),
                                        closestObject.getTargetYDegrees(),
                                        closestObject.getTargetArea()};
                }
            }
        }

        return new double[] {-1, -1, -1};
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
