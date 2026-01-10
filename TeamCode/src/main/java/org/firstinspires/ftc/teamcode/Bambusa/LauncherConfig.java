package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class LauncherConfig {
    public static String outtake = "outtake";
    public static String intake = "intake";
    public static String launcher = "launcher";

    public static Servo.Direction outtakeDirection = Servo.Direction.FORWARD;
    public static Servo.Direction intakeDirection = Servo.Direction.FORWARD;
    public static DcMotorSimple.Direction launcherDirection = DcMotorSimple.Direction.FORWARD;

    public static double outtakeSpeed = 0.8;
    public static double intakeSpeed = 0.8;
    public static double launcherNormalSpeed = 0.6;
    public static double launcherBoostSpeed = 1.0;
}
