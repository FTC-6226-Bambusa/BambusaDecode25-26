package org.firstinspires.ftc.teamcode.Bambusa;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LauncherConfig {
    public static String outtake = "outtake";
    public static String intake = "intake";
    public static String launcher = "launcher";

    public static DcMotorSimple.Direction outtakeDirection = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction intakeDirection = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction launcherDirection = DcMotorSimple.Direction.FORWARD;

    public static double outtakeSpeed = 0.8;
    public static double intakeSpeed = 0.8;
    public static double launcherSpeed = 0.8;
}
