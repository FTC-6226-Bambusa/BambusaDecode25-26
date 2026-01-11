package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
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
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static FollowerConstants followerConstants = new FollowerConstants()
            .forwardZeroPowerAcceleration(-35.2006511)
            .lateralZeroPowerAcceleration(-52.1848131)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.1, 0, 0.01, 0.01))
            .headingPIDFCoefficients(new PIDFCoefficients(1.5, 0, 0.02, 0.01))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.2, 0, 0.02, 0.6,0.01))
            .centripetalScaling(0.0005);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(3.5)
            .rightFrontMotorName(DriveConfig.frontRightMotor)
            .rightRearMotorName(DriveConfig.backRightMotor)
            .leftRearMotorName(DriveConfig.backLeftMotor)
            .leftFrontMotorName(DriveConfig.frontLeftMotor)
            .leftFrontMotorDirection(DriveConfig.frontLeftMotorDirection)
            .leftRearMotorDirection(DriveConfig.backLeftMotorDirection)
            .rightFrontMotorDirection(DriveConfig.frontRightMotorDirection)
            .rightRearMotorDirection(DriveConfig.backRightMotorDirection)
            .xVelocity(55)
            .yVelocity(45);

    public static ThreeWheelIMUConstants localizerConstants = new ThreeWheelIMUConstants()
            .forwardTicksToInches(.0019450627964622915)
            .strafeTicksToInches(.0020464263813119495)
            .turnTicksToInches(.0020309)
            .leftPodY(7.5)
            .rightPodY(-7.5)
            .strafePodX(-7.5)
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