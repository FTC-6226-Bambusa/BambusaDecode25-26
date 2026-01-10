package org.firstinspires.ftc.teamcode.Bambusa;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Configurable
public class DriveConfig {
    public static double normalSpeed = 0.5;
    public static double boostSpeed = 0.8;
    public static double strafeCorrection = 1.1;

    public static String imu = "imu";
    public static String frontLeftMotor  = "leftFront";
    public static String frontRightMotor = "rightFront";
    public static String backLeftMotor   = "leftRear";
    public static String backRightMotor  = "rightRear";

    public static DcMotorSimple.Direction frontLeftMotorDirection = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction frontRightMotorDirection = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction backLeftMotorDirection = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction backRightMotorDirection = DcMotorSimple.Direction.FORWARD;
}
