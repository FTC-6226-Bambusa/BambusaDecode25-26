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
        imu.initialize(new IMU.Parameters(DriveConfig.imuOrientation));
        imu.resetYaw();

        headingOffset = StartConfig.pose.getHeading() - Math.PI/2;

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
    public void run(double strafe, double forward, double turn, double boost, boolean resetYaw) {
        // User input override
        if (!enabled) {
            return;
        }

        double y = -forward;
        double x = strafe * DriveConfig.strafeCorrection;
        double rx = turn;

        double rawHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (resetYaw) {
            headingOffset = rawHeading;
        }

        double botHeading = rawHeading - headingOffset;

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Calculations
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        // Final Speed
        double finalSpeed = MathPlus.lerp(DriveConfig.normalSpeed,
                                          DriveConfig.boostSpeed,
                                          boost);

        // Apply Power
        fl.setPower(frontLeftPower * finalSpeed);
        bl.setPower(backLeftPower * finalSpeed);
        fr.setPower(frontRightPower * finalSpeed);
        br.setPower(backRightPower * finalSpeed);
    }

    /**
     * Stops everything
     */
    public void stop() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }
}
