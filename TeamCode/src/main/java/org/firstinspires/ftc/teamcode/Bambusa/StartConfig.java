package org.firstinspires.ftc.teamcode.Bambusa;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class StartConfig {
    // Alliance Color
    public enum AllianceColor { BLUE, RED }
    public enum StartPosition { GOAL, WALL }

    public static AllianceColor color = AllianceColor.BLUE;
    public static StartPosition position = StartPosition.GOAL;

    public static double midline = 72;
    public static boolean flipped = color == AllianceColor.RED;

    private static Pose getPose() {
        if (position == StartPosition.GOAL) {
            double x = 50;
            double y = 50;
            double h = 30;

            double flippedX = (flipped ? x : -x) + midline;
            double flippedH = flipped ? h : 180 - h;

            return new Pose(flippedX, y, flippedH);
        } else /*if start position is wall*/ {
            double x = 63.5;
            double y = 8.5;
            double h = 0;

            double flippedX = (flipped ? x : -x) + midline;
            double flippedH = flipped ? h : 180 - h;

            return new Pose(flippedX, y, flippedH);
        }
    }

    public final static Pose pose = getPose();
}
