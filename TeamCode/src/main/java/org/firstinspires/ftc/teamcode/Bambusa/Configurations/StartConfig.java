package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

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

    public static Pose getPose(StartPosition start, AllianceColor col) {
        boolean red = col == AllianceColor.RED;
        boolean wall = start == StartPosition.WALL;
        boolean goal = start == StartPosition.GOAL;

        if (wall) {
            return red ? new Pose(57, 9.3, Math.toRadians(90)):
                         new Pose(87, 9.3, Math.toRadians(90));
        } else if (goal) {
            return red ? new Pose(27.3, 130, Math.toRadians(-36)):
                         new Pose(116, 130, Math.toRadians(216));
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
