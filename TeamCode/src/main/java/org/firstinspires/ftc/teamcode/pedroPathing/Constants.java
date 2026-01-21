package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.DriveConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LocalizationConfig;

public class Constants {
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(3.6)

            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(false)

            .centripetalScaling(0.0001)
            .forwardZeroPowerAcceleration(-35)
            .lateralZeroPowerAcceleration(-45)

            .translationalPIDFCoefficients(new PIDFCoefficients(0.44, 0, 0.076, 0))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.033,0,0.007,0))

            .headingPIDFCoefficients(new PIDFCoefficients(0.46, 0, 0.05, 0))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(0.4,0,0.01,0))

            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.01, 0, 0.0008, 0.6, 0))

            // Currently unused
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.01, 0, 0, 0.1, 0.06));

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(0.85)
            .xVelocity(88)
            .yVelocity(67)

            .rightFrontMotorName(DriveConfig.frontRightMotor)
            .rightRearMotorName(DriveConfig.backRightMotor)
            .leftRearMotorName(DriveConfig.backLeftMotor)
            .leftFrontMotorName(DriveConfig.frontLeftMotor)

            .leftFrontMotorDirection(DriveConfig.frontLeftMotorDirection)
            .leftRearMotorDirection(DriveConfig.backLeftMotorDirection)
            .rightFrontMotorDirection(DriveConfig.frontRightMotorDirection)
            .rightRearMotorDirection(DriveConfig.backRightMotorDirection);

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
            .IMU_Orientation(DriveConfig.imuOrientation);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .threeWheelIMULocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }

}