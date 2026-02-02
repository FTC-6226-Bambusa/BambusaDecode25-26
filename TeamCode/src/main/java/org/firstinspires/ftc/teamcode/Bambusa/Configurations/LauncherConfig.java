package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Configurable
public class LauncherConfig {
    public static DcMotorSimple.Direction launcherDirection = DcMotorSimple.Direction.REVERSE;
    public static Servo.Direction outtakeDirection = Servo.Direction.FORWARD;
    public static Servo.Direction intakeDirection = Servo.Direction.FORWARD;
    public static Servo.Direction throughtakeDirection = Servo.Direction.FORWARD;

    public static double outtakeSpeed = 1;
    public static double intakeSpeed = 1;
    public static double throughtakeSpeed = 0.8;
    public static double throughtakeFullSpeed = 1;

    public static double launcherNormalSpeed = 0.75;
    public static double launcherBoostSpeed = 0.9;
    public static double launcherReverseSpeed = 0.15;

    // Autos only
    public static double launcherAutoSpeed = 0.9;

    // Macros only
    public static double LRMLauncherSpeedFactor = 0.86;
    public static double LRMLauncherSpeedBias = 0.135;
}
