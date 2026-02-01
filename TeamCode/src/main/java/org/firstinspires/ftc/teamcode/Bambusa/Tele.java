package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bambusa.Helpers.Drawing;
import org.firstinspires.ftc.teamcode.Bambusa.SLURPIE.CollectData;

@TeleOp (name = "__TELEOP__", group = "COMPETITION")
public class Tele extends LinearOpMode {

    public Robot robot;
    public CollectData data;

    @Override
    public void runOpMode() throws InterruptedException {
        // Robot
        robot = new Robot(hardwareMap, gamepad1, gamepad2);

        // Start drawing
        Drawing.init();

        waitForStart();
        if (isStopRequested()) return;

        // Starting gamepad data collection
        data = new CollectData();
        data.init(hardwareMap, gamepad1, gamepad2);

        // Initializing robot
        robot.teleInit();

        while (opModeIsActive()) {
            // Running robot
            robot.run(telemetry);

            // Logging joystick (250ms resolution)
            data.update(gamepad1);

            // Robot drawing
            Drawing.drawRobot(robot.follower.getPose());
            Drawing.sendPacket();

            // Updating telemetry
            telemetry.update();
        }

        // Stop data collection
        data.end();

        // Dying words of the robot
        telemetry.addData("Status", "File Saved Successfully.");
        telemetry.update();
    }
}
