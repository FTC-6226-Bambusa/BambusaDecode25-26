package org.firstinspires.ftc.teamcode.Bambusa.SLURPIE;

import android.os.Environment;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.Controls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JoystickStoeckl {

    private BufferedWriter writer;
    private Controls controls;
    private long startTime;
    private long lastLogTime;
    private boolean isLogging = false;

    // Save location: /sdcard/FIRST/joysticks/
    private static final String LOG_DIR = Environment.getExternalStorageDirectory().getPath() + "/FIRST/joysticks/";

    // Delay setting
    private static final long LOG_INTERVAL_MS = 100;

    public JoystickStoeckl() {
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void startLogging(Gamepad gamepad1, Gamepad gamepad2) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date());
        String fileName = "joystick_log_" + timestamp + ".csv";
        File file = new File(LOG_DIR + fileName);
        controls = new Controls(gamepad1, gamepad2); // no gamepad2

        try {
            writer = new BufferedWriter(new FileWriter(file));

            // Removed CSV Header

            startTime = System.currentTimeMillis();
            lastLogTime = 0;
            isLogging = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logLoop() {
        if (!isLogging || writer == null) return;

        long now = System.currentTimeMillis();

        if (now - lastLogTime < LOG_INTERVAL_MS) {
            return;
        }

        lastLogTime = now;
        long matchTime = now - startTime;

        try {
            StringBuilder sb = new StringBuilder();

            sb.append(matchTime).append(",");

            // Gamepad 1
            sb.append(format((float) controls.forward())).append(",");
            sb.append(format((float) controls.strafe())).append(",");
            sb.append(format((float) controls.turn())).append(",");
            sb.append(format((float) controls.boost())).append(",");

            writer.write(sb.toString());
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logLoop(Gamepad g1, double boxX, double boxY, double boxSize) {
        if (!isLogging || writer == null) return;

        long now = System.currentTimeMillis();

        if (now - lastLogTime < LOG_INTERVAL_MS) {
            return;
        }

        lastLogTime = now;
        long matchTime = now - startTime;

        try {
            StringBuilder sb = new StringBuilder();
;
            sb.append(matchTime).append(",");

            sb.append(format((float) controls.forward())).append(",");
            sb.append(format((float) controls.strafe())).append(",");
            sb.append(format((float) controls.turn())).append(",");
            sb.append(format((float) controls.boost())).append(",");

            // Box sizes
            sb.append(format((float) boxX)).append(",");
            sb.append(format((float) boxY)).append(",");
            sb.append(format((float) boxSize)).append(",");

            writer.write(sb.toString());
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String format(float val) {
        return String.format(Locale.US, "%.3f", val);
    }

    public void stopLogging() {
        isLogging = false;
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}








// Gordon was here :)