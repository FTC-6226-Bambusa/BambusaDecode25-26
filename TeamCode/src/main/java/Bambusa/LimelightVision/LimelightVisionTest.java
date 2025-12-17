package Bambusa.LimelightVision;

import Bambusa.Robot;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;

/*
 * When ran, if a selected color is within the right half of the Limelight 3A's vision, it turns right.
 * If it is in the left half, it turns left, otherwise not at all. This class is meant to check if the
 * Limelight 3A is properly detecting colors. Learn how to use in SensorLimelight3A.java.
*/

@TeleOp(name = "LimelightColorTest_Turn")
public class LimelightVisionTest extends LinearOpMode {

    // power to apply when turning; positive for clockwise
    private static final double TURN_POWER = 0.3;

    private Limelight3A limelight;
    private Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        // instantiate your Robot helper (assumes it takes hardwareMap)
        robot = new Robot(hardwareMap);

        // get the Limelight from the hardware map (name must match your config)
        limelight = hardwareMap.get(Limelight3A.class, "lemonlight");
        limelight.pipelineSwitch(0);   // use pipeline 0; change if needed
        limelight.start();             // begin streaming

        telemetry.addLine("Press PLAY to start color-based turning");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            boolean sawColor = false;

            if (result != null && result.isValid()) {
                // get all color detections
                List<LLResultTypes.ColorResult> colors = result.getColorResults();
                if (!colors.isEmpty()) {
                    // pick the first detection
                    LLResultTypes.ColorResult cr = colors.get(0);
                    double xDeg = cr.getTargetXDegrees();  // negative = left, positive = right

                    // decide turn direction
                    if (xDeg > 0) {
                        // color is on right half → turn right (clockwise)
                        robot.setMotorPowers(
                                TURN_POWER,   // frontLeft
                                -TURN_POWER,   // frontRight
                                TURN_POWER,   // backLeft
                                -TURN_POWER    // backRight
                        );
                        telemetry.addData("Turn", "Right (x=%.1f°)", xDeg);
                    } else {
                        // color on left → turn left (counter‐clockwise)
                        robot.setMotorPowers(
                                -TURN_POWER,
                                TURN_POWER,
                                -TURN_POWER,
                                TURN_POWER
                        );
                        telemetry.addData("Turn", "Left (x=%.1f°)", xDeg);
                    }
                    sawColor = true;
                }
            }

            if (!sawColor) {
                // no color seen → stop
                robot.setMotorPowers(0, 0, 0, 0);
                telemetry.addLine("No color detected");
            }

            telemetry.update();
        }

        // clean up
        limelight.stop();
        robot.setMotorPowers(0, 0, 0, 0);
    }
}
