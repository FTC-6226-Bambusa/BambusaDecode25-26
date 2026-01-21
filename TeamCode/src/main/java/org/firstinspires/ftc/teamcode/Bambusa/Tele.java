package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bambusa.Helpers.Drawing;
import org.firstinspires.ftc.teamcode.Bambusa.RottenMustard.JoystickLogger;

@TeleOp (name = "__TELEOP__", group = "COMPETITION")
public class Tele extends LinearOpMode {

    public Robot robot;
    JoystickLogger logger;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, gamepad1, gamepad2);

        // Gamepad logger (for LRM)
        logger =  new JoystickLogger();

        // Start drawing
        Drawing.init();

        waitForStart();
        if (isStopRequested()) return;

        // Starting logging gamepad
        logger.startLogging();

        // Initializing robot
        robot.teleInit();

        while (opModeIsActive()) {
            // Running robot
            robot.run(telemetry);

            // Logging joystick (250ms resolution)
            logger.logLoop(gamepad1, gamepad2);

            // Robot drawing
            Drawing.drawRobot(robot.follower.getPose());
            Drawing.sendPacket();

            // Updating telemetry
            telemetry.update();
        }

        // Stop logging gamepad
        logger.stopLogging();

        // Dying words of the robot
        telemetry.addData("Status", "File Saved Successfully.");
        telemetry.update();
    }
}
