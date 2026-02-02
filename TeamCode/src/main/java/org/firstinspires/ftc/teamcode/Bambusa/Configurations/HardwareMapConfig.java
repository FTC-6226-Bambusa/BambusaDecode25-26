package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class HardwareMapConfig {
    /** --- Drive Train Motors --- **/
    // Front Left - PORT: Control Hub #0
    // Type: DcMotor
    public static String frontLeftMotor = "leftFront";

    // Front Right - PORT: Expansion Hub #0
    // Type: DcMotor
    public  static String frontRightMotor = "rightFront";

    // Back Left - PORT: Control Hub #1
    // Type: DcMotor
    public static String backLeftMotor = "leftRear";

    // Back Right - PORT: Expansion Hub #1
    // Type: DcMotor
    public static String backRightMotor = "rightRear";

    // IMU
    // Type: IMU
    public static String imu = "imu";

    /** --- Launcher Motors --- **/
    // Launcher - PORT: Expansion Hub #2
    // Type: DcMotorEx
    public static String launcher = "launcher";

    // Outtake - PORT: Control Hub #0
    // Type: Servo
    public static String outtake = "outtake";

    // Intake - PORT: Control Hub #1
    // Type: Servo
    public static String intake = "intake";

    // Intake - PORT: Control Hub #2
    // Type: Servo
    public static String throughtake = "throughtake";
}
