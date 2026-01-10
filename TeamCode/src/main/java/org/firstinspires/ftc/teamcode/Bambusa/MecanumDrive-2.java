package org.firstinspires.ftc.teamcode.Bambusa;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Configurable
public class MecanumDrive {
    public IMU imu;
    public DcMotor fl, fr, bl, br;

    public double headingOffset = 0;
    public boolean enabled = true;

    /**
     * Drive train class
     *
     * @param frontRight front right motor (quad 0)
     * @param frontLeft front left motor (quad 1)
     * @param backLeft back left motor (quad 2)
     * @param backRight back right motor (quad 3)
     **/
    public MecanumDrive(IMU imu, DcMotor frontRight, DcMotor frontLeft, DcMotor backLeft, DcMotor backRight) {
        this.imu = imu;

        this.fr = frontRight;
        this.fl = frontLeft;
        this.bl = backLeft;
        this.br = backRight;

        init();
    }

    /**
     * Sets motor directions and forward direction
     */
    public void init() {
        imu.resetYaw();

        fr.setDirection(DriveConfig.frontRightMotorDirection);
        fl.setDirection(DriveConfig.frontLeftMotorDirection);
        bl.setDirection(DriveConfig.backLeftMotorDirection);
        br.setDirection(DriveConfig.backRightMotorDirection);
    }

    /**
     * Field Centric Drive
     *
     * @param strafe strafing
     * @param forward forwards and backwards
     * @param turn turning
     * @param boost speed boost
     * @param resetYaw sets forward direction
     **/
    public void run(double forward, double strafe, double turn, double boost, boolean resetYaw) {
        // Useful for overriding user input
        if (!enabled) {
            return;
        }

        double y = -forward;
        double x = strafe;
        double rx = turn;

        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (resetYaw) {
            headingOffset = heading;
        }

        double botHeading = -(heading - headingOffset);

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        // Strafing correction
        rotX *= DriveConfig.strafeCorrection;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

        double flP = (rotY + rotX + rx) / denominator;
        double frP = (rotY - rotX - rx) / denominator;
        double blP = (rotY - rotX + rx) / denominator;
        double brP = (rotY + rotX - rx) / denominator;

        double speed = MathPlus.lerp(
                DriveConfig.normalSpeed,
                DriveConfig.boostSpeed,
                boost
        );

        fl.setPower(flP * speed);
        fr.setPower(frP * speed);
        bl.setPower(blP * speed);
        br.setPower(brP * speed);
    }
}
