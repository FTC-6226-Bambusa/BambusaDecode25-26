package org.firstinspires.ftc.teamcode.Bambusa.SLURPIE;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class CollectData {
    // Gamepad logger (for LRM)
    public JoystickStoeckl logger;

    // Limelight 3A
    public Limelight3A LL;

    public CollectData() {
        // Logs gamepad controls
        logger = new JoystickStoeckl();
    }

    public void init(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        // Initialize Limelight
        LL = hardwareMap.get(Limelight3A.class, "lemonlight");

        // Limelight settings
        LL.setPollRateHz(90);
        LL.pipelineSwitch(0);

        // Start the camera
        LL.start();

        // Starting gamepad logging
        logger.startLogging(gamepad1, gamepad2);
    }

    public void update(Gamepad g1) {
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
                    logger.logLoop(g1, closestObject.getTargetXDegrees(), closestObject.getTargetYDegrees(), closestObject.getTargetArea());
                    return;
                }
            }
        }

        // If result is null or no objects are seen
        logger.logLoop(g1, -1, -1, -1);
    }

    public void end() {
        // Stop logging gamepad
        logger.stopLogging();

        // Stop the Limelight
        if (LL != null) {
            LL.stop();
        }
    }
}