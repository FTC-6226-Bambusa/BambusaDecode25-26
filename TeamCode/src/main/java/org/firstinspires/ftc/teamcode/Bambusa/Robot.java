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
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LauncherConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.StartConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.MathPlus;
import org.firstinspires.ftc.teamcode.Bambusa.RottenMustard.LincolnsRottenMustard;
import org.firstinspires.ftc.teamcode.Bambusa.RottenMustard.SlurpSlurp;
import org.firstinspires.ftc.teamcode.Bambusa.SLURPIE.Artifact;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.List;

public class Robot {
    // Controls
    public Controls controls;

    // Macros
    public Limelight3A LL;
    public Artifact artifact;
    public Follower follower;

    // Drive
    public MecanumDrive drive;

    // Subsystems
    public Launcher launcher;

    public Intake intake;
    public Intake throughtake;
    public Outtake outtake;

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
        IMU imu = hardwareMap.get(IMU.class, HardwareMapConfig.imu);

        // Wheels
        DcMotor fl = hardwareMap.get(DcMotor.class, HardwareMapConfig.frontLeftMotor);
        DcMotor fr = hardwareMap.get(DcMotor.class, HardwareMapConfig.frontRightMotor);
        DcMotor bl = hardwareMap.get(DcMotor.class, HardwareMapConfig.backLeftMotor);
        DcMotor br = hardwareMap.get(DcMotor.class, HardwareMapConfig.backRightMotor);

        drive = new MecanumDrive(imu, fr, fl, bl, br);

        // Launcher
        launcher = new Launcher(hardwareMap.get(DcMotorEx.class, HardwareMapConfig.launcher));

        // Intake
        intake = new Intake(hardwareMap.get(Servo.class, HardwareMapConfig.intake), LauncherConfig.intakeSpeed);
        throughtake = new Intake(hardwareMap.get(Servo.class, HardwareMapConfig.throughtake), LauncherConfig.throughtakeSpeed);

        // Outtake
        outtake = new Outtake(hardwareMap.get(Servo.class, HardwareMapConfig.outtake));

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
        launcher = new Launcher(hardwareMap.get(DcMotorEx.class, HardwareMapConfig.launcher));
        intake = new Intake(hardwareMap.get(Servo.class, HardwareMapConfig.intake), LauncherConfig.intakeSpeed);
        throughtake = new Intake(hardwareMap.get(Servo.class, HardwareMapConfig.throughtake), LauncherConfig.throughtakeSpeed);
        outtake = new Outtake(hardwareMap.get(Servo.class, HardwareMapConfig.outtake));

        // Initializes for auto since no gamepads are passed in
        autoInit();
    }

    // Initializes for tele
    public void teleInit() {
        intake.enable();
        throughtake.enable();

        // Limelight settings
        LL.setPollRateHz(90);
        LL.pipelineSwitch(0);

        // Starting limelight
        LL.start();
    }

    // Initializes for auto
    public void autoInit() {
        intake.enable();
        throughtake.enable();
        launcher.setPower(LauncherConfig.launcherAutoSpeed);
    }

    /**
     * Performs all necessary actions
     */
    public void run(Telemetry telemetry) {
        // Setting drive to enabled
        drive.enabled = true;

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
            double x = follower.getPose().getY();
            double z = follower.getPose().getX();

            double changed_x = (x - 72) / 12;
            double changed_z = (z - 72) / 12;

            double[] choices = LincolnsRottenMustard.getCoords(changed_x, changed_z);

            if (choices != null) {
                drive.enabled = false;

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

        }

        boolean detectedBall = false;
        boolean negative = false;

        // Slurpie running
        if (controls.slurp()) {
            double[] LLoutput = getLimelightArtifacts();

            // Noise smoothing
            drive.enabled = artifact.smoothData(LLoutput);

            if (LLoutput[0] != -1) {

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
                if (slurpieOutput[0] != -1) {
                    negative = slurpieOutput[0] < 0;

                    detectedBall = true;

                    // Overrides gamepad input
                    drive.runWithoutIMU(true, slurpieOutput[1], slurpieOutput[0], slurpieOutput[2], slurpieOutput[3]);
                }
            }
        }

        // Whether drive is enabled
        telemetry.addData("Drive enabled", drive.enabled);

        // Extra tests
        telemetry.addData("Detected Ball: ", detectedBall);
        telemetry.addData("Negative Forward: ", negative);

        // Driving
        if (DriveConfig.useFieldCentricDrive) {
            drive.run(controls.strafe(), controls.forward(), turn, controls.boost(), controls.resetIMU());
        } else {
            drive.runWithoutIMU(controls.strafe(), controls.forward(), turn, controls.boost());
        }

        // Intake control
        if (controls.reverseIntake()) {
            intake.reverse();
            throughtake.reverse();
        } else {
            intake.forward();
            throughtake.forward();
        }

        // Outtake control
        if (controls.reverseOuttake()) {
            outtake.reverse();
        } else if (controls.powerOuttake()) {
            outtake.forward();
            throughtake.speed = LauncherConfig.throughtakeFullSpeed;
        } else {
            outtake.disable();
            throughtake.speed = LauncherConfig.throughtakeSpeed;
        }

        // Launcher control
        launcher.setPower(controls.launch(), controls.launchBoost());

        // Updating telemetry again
        telemetry.update();
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
        throughtake.disable();
        outtake.disable();
        launcher.disable();
    }
}
