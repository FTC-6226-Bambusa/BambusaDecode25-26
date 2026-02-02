package org.firstinspires.ftc.teamcode.Bambusa.SLURPIE;

import android.os.Environment;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.DriveConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.HardwareMapConfig;
import org.firstinspires.ftc.teamcode.Bambusa.MecanumDrive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Joystick Playback", group = "Debug")
@Configurable
public class JoystickPlaybackOpMode extends LinearOpMode {

    private static final String LOG_DIR =
            Environment.getExternalStorageDirectory().getPath() + "/FIRST/joysticks/";

    // Editable
    public static final String FILE_NAME = "joystick_log_2026-01-27_17-24-04.csv";

    static class Frame {
        long time;
        double strafe, forward, turn, boost;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // IMU
        IMU imu = hardwareMap.get(IMU.class, HardwareMapConfig.imu);

        // Wheels
        DcMotor fl = hardwareMap.get(DcMotor.class, HardwareMapConfig.frontLeftMotor);
        DcMotor fr = hardwareMap.get(DcMotor.class, HardwareMapConfig.frontRightMotor);
        DcMotor bl = hardwareMap.get(DcMotor.class, HardwareMapConfig.backLeftMotor);
        DcMotor br = hardwareMap.get(DcMotor.class, HardwareMapConfig.backRightMotor);

        // Mecanum drive
        MecanumDrive drive = new MecanumDrive(imu, fr, fl, bl, br);

        List<Frame> frames = loadFrames();
        telemetry.addData("Frames Loaded", frames.size());
        telemetry.update();

        waitForStart();

        long startTime = System.currentTimeMillis();
        int index = 0;

        while (opModeIsActive() && index < frames.size()) {
            Frame f = frames.get(index);

            long now = System.currentTimeMillis() - startTime;
            if (now < f.time) {
                idle();
                continue;
            }

            // Re-drive using recorded joystick values
            drive.run(
                    f.strafe,       // strafe
                    f.forward,     // forward
                    f.turn,         // turn
                    f.boost,        // boost
                    false           // resetYaw
            );

            index++;
        }

        drive.stop();
    }

    private List<Frame> loadFrames() {
        List<Frame> frames = new ArrayList<>();

        File file = new File(LOG_DIR + FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");

                Frame f = new Frame();
                f.time = Long.parseLong(p[0]);
                f.forward = Double.parseDouble(p[1]);
                f.strafe = Double.parseDouble(p[2]);
                f.boost = Double.parseDouble(p[3]);
                f.boost = Double.parseDouble(p[4]);

                frames.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return frames;
    }
}
