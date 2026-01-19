package org.firstinspires.ftc.teamcode.Bambusa.RottenMustard;

import android.os.Environment;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JoystickLogger {

    private BufferedWriter writer;
    private long startTime;
    private long lastLogTime; // Tracks when we last wrote to the file
    private boolean isLogging = false;

    // Save location: /sdcard/FIRST/joysticks/
    private static final String LOG_DIR = Environment.getExternalStorageDirectory().getPath() + "/FIRST/joysticks/";

    // Delay setting
    private static final long LOG_INTERVAL_MS = 250;

    public JoystickLogger() {
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void startLogging() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date());
        String fileName = "joystick_log_" + timestamp + ".csv";
        File file = new File(LOG_DIR + fileName);

        try {
            writer = new BufferedWriter(new FileWriter(file));

            // CSV HEADER
            writer.write("Time(ms), G1_Ly, G1_Lx, G1_Ry, G1_Rx, G2_Ly, G2_Lx, G2_Ry, G2_Rx");
            writer.newLine();

            startTime = System.currentTimeMillis();
            lastLogTime = 0;
            isLogging = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logLoop(Gamepad g1, Gamepad g2) {
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
            sb.append(format(g1.left_stick_y)).append(",");
            sb.append(format(g1.left_stick_x)).append(",");
            sb.append(format(g1.right_stick_y)).append(",");
            sb.append(format(g1.right_stick_x)).append(",");

            // Gamepad 2
            sb.append(format(g2.left_stick_y)).append(",");
            sb.append(format(g2.left_stick_x)).append(",");
            sb.append(format(g2.right_stick_y)).append(",");
            sb.append(format(g2.right_stick_x));

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