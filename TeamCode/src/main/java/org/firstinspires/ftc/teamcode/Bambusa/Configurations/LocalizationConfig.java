package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

import com.pedropathing.ftc.localization.Encoder;

public class LocalizationConfig {
    public static String rightEncoder = HardwareMapConfig.frontRightMotor;
    public static String leftEncoder  = HardwareMapConfig.backLeftMotor;
    public static String strafeEncoder = HardwareMapConfig.frontLeftMotor;

    public static double rightEncoderDirection = Encoder.FORWARD;
    public static double leftEncoderDirection = Encoder.FORWARD;
    public static double strafeEncoderDirection = Encoder.REVERSE;
}
