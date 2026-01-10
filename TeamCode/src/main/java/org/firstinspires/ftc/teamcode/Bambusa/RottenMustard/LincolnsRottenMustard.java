package org.firstinspires.ftc.teamcode.Bambusa.RottenMustard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LincolnsRottenMustard {

    public static double[] getCoords(double val1, double val2) {
        String binaryPath = "./encompassing";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    binaryPath,
                    String.valueOf(val1),
                    String.valueOf(val2));

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            Pattern pattern = Pattern.compile("Choice:\\s*\\[(.*?),(.*?)\\]");
            double[] results = null;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    try {
                        double num1 = Double.parseDouble(matcher.group(1).trim());
                        double num2 = Double.parseDouble(matcher.group(2).trim());
                        results = new double[]{num1, num2};
                        break;
                    } catch (NumberFormatException e) {
                        System.err.println("Could not parse numbers from output: " + line);
                    }
                }
            }

            process.waitFor();
            return results;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

