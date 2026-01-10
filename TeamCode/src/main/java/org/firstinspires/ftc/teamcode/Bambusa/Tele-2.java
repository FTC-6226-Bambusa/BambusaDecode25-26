package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp (name = "__TELEOP__", group = "COMPETITION")
public class Tele extends LinearOpMode {

    public Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, gamepad1, gamepad2);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Running robot
            robot.run();

            // Driver Station telemetry
            telemetry.addData("Sample Telemetry Working", true);
            telemetry.update();
        }
    }
}
