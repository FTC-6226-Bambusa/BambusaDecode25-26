package org.firstinspires.ftc.teamcode.Bambusa;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;

@Configurable
public class StartConfig {
    // Alliance color
    public enum AllianceColor { BLUE, RED }

    // Start position
    public enum StartPosition { GOAL, WALL }

    // Configurable positions
    public static AllianceColor color = AllianceColor.RED;
    public static StartPosition position = StartPosition.WALL;

    private static Pose getPose(StartPosition start, AllianceColor col) {
        boolean red = col == AllianceColor.RED;
        boolean wall = start == StartPosition.WALL;
        boolean goal = start == StartPosition.GOAL;

        if (wall) {
            return red ? new Pose(60, 9, Math.toRadians(-90)):
                    new Pose(60, 9, Math.toRadians(-90));
        } else if (goal) {
            return red ? new Pose(83, 9, Math.toRadians(-90)):
                    new Pose(60, 9, Math.toRadians(-90));
        }

        // Default case
        return new Pose(72, 72, 90); // Middle of field
    }

    // Starting pose
    public static Pose pose = getPose(position, color);

    // Updates pose for init
    public static void updatePose() {
        pose = getPose(position, color);
    }

    public static void updatePose(StartPosition start, AllianceColor col) {
        pose = getPose(start, col);
    }
}
