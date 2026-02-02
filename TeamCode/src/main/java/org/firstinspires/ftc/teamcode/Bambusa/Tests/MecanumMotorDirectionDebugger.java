package org.firstinspires.ftc.teamcode.Bambusa.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.DriveConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.HardwareMapConfig;

@TeleOp (name = "Motor Direction Test", group = "TESTS")
public class MecanumMotorDirectionDebugger extends LinearOpMode {
    public DcMotor fl, fr, bl, br;

    public double testPower = 1;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initializing motors
        fl = hardwareMap.dcMotor.get(HardwareMapConfig.frontLeftMotor);
        fr = hardwareMap.dcMotor.get(HardwareMapConfig.frontRightMotor);
        bl = hardwareMap.dcMotor.get(HardwareMapConfig.backLeftMotor);
        br = hardwareMap.dcMotor.get(HardwareMapConfig.backRightMotor);

        // Setting directions
        fl.setDirection(DriveConfig.frontLeftMotorDirection);
        fr.setDirection(DriveConfig.frontRightMotorDirection);
        bl.setDirection(DriveConfig.backLeftMotorDirection);
        br.setDirection(DriveConfig.backRightMotorDirection);

        waitForStart();
        if (isStopRequested()) return;

        // Power test
        while (opModeIsActive()) {
            fr.setPower(gamepad1.y ? testPower : 0);
            br.setPower(gamepad1.b ? testPower : 0);
            bl.setPower(gamepad1.a ? testPower : 0);
            fl.setPower(gamepad1.x ? testPower : 0);
        }
    }
}
