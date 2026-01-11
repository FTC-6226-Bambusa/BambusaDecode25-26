package org.firstinspires.ftc.teamcode.Bambusa;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class LauncherConfig {
    public static String outtake = "outtake";
    public static String intake = "intake";
    public static String launcher = "launcher";

    public static Servo.Direction outtakeDirection = Servo.Direction.FORWARD;
    public static Servo.Direction intakeDirection = Servo.Direction.FORWARD;
    public static DcMotorSimple.Direction launcherDirection = DcMotorSimple.Direction.REVERSE;

    public static double outtakeSpeed = 1;
    public static double intakeSpeed = 1;

    public static double launcherNormalSpeed = 0.8;
    public static double launcherBoostSpeed = 1.0;
}
