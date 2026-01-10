package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Bambusa.DriveConfig;
import org.firstinspires.ftc.teamcode.Bambusa.LocalizationConfig;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName(DriveConfig.frontRightMotor)
            .rightRearMotorName(DriveConfig.backRightMotor)
            .leftRearMotorName(DriveConfig.backLeftMotor)
            .leftFrontMotorName(DriveConfig.frontLeftMotor)
            .leftFrontMotorDirection(DriveConfig.frontLeftMotorDirection)
            .leftRearMotorDirection(DriveConfig.backLeftMotorDirection)
            .rightFrontMotorDirection(DriveConfig.frontRightMotorDirection)
            .rightRearMotorDirection(DriveConfig.backRightMotorDirection);

    public static ThreeWheelIMUConstants localizerConstants = new ThreeWheelIMUConstants()
            .forwardTicksToInches(.001989436789)
            .strafeTicksToInches(.001989436789)
            .turnTicksToInches(.001989436789)
            .leftPodY(1)
            .rightPodY(-1)
            .strafePodX(-2.5)
            .leftEncoder_HardwareMapName(LocalizationConfig.leftEncoder)
            .rightEncoder_HardwareMapName(LocalizationConfig.rightEncoder)
            .strafeEncoder_HardwareMapName(LocalizationConfig.strafeEncoder)
            .leftEncoderDirection(LocalizationConfig.leftEncoderDirection)
            .rightEncoderDirection(LocalizationConfig.rightEncoderDirection)
            .strafeEncoderDirection(LocalizationConfig.strafeEncoderDirection)
            .IMU_HardwareMapName(DriveConfig.imu)
            .IMU_Orientation(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP));

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .threeWheelIMULocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }

}