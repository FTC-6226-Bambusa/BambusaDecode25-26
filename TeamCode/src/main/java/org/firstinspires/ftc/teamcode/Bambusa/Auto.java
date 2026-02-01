package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bambusa.Configurations.AutoConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.Controls;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.LauncherConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Configurations.StartConfig;
import org.firstinspires.ftc.teamcode.Bambusa.Helpers.Drawing;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous(name = "AUTO", group = "COMPETITION")
public class Auto extends OpMode {
    // Robot
    private Robot robot;

    // Controls
    private Controls controls;

    // Follower
    private Follower follower;

    // Timer
    private final ElapsedTime timer = new ElapsedTime();

    // State machine
    private int pathIndex = -1;
    private boolean isDriving = false;

    // Ordered paths
    private final List<NamedPath> pathSequence = new ArrayList<>();

    // Helper class
    private static class NamedPath {
        String name;
        PathChain pathChain;

        NamedPath(String name, PathChain pathChain) {
            this.name = name;
            this.pathChain = pathChain;
        }
    }

    /**
     * <div style="color: lime; font-weight: bold;">PLACE CUSTOM ACTIONS HERE</div>
     * Runs a custom action after a specific trajectory has been completed
     *
     * @param pathName the path triggering the custom action
     * @return if the custom action executed
     */
    private boolean runCustomAction(String pathName) {
        if (pathName == null) return true;

        // Default custom actions

        // Launching actions
        if (pathName.toLowerCase().contains("launch")) {
            telemetry.addData("Action: ", "Shooting");

            // Shooting ball
            robot.outtake.enable();

            return timer.milliseconds() > AutoConfig.shootingTime * 1000;
        }

        // Starting actions
        if (pathName.toLowerCase().contains("start")) {
            robot.launcher.setPower(LauncherConfig.launcherAutoSpeed);
            robot.intake.enable();
            return true;
        }

        // Ending actions
        if (pathName.toLowerCase().contains("end")) {
            robot.disableLaunchers();

            // Dead robot
            return false;
        }

        return true;
    }

    @Override
    public void init() {
        // Initializing auto setup controls
        controls = new Controls(gamepad1, gamepad2);
    }

    @Override
    public void init_loop() {
        // Alliance color selection
        if (controls.toggleAllianceColor()) {
            StartConfig.color = StartConfig.color == StartConfig.AllianceColor.RED ?
                    StartConfig.AllianceColor.BLUE:
                    StartConfig.AllianceColor.RED;
        }

        // Start position selection
        if (controls.toggleStartingPosition()) {
            StartConfig.position = StartConfig.position == StartConfig.StartPosition.WALL ?
                    StartConfig.StartPosition.GOAL:
                    StartConfig.StartPosition.WALL;
        }

        // Logging selection
        String col = StartConfig.color == StartConfig.AllianceColor.RED ? "[RED]  /  BLUE" : "RED  /  [BLUE]";
        String pos = StartConfig.position == StartConfig.StartPosition.WALL ? "[WALL]  /  GOAL" : "WALL  /  [GOAL]";

        telemetry.addData("Alliance Color: ", col);
        telemetry.addData("Start Position: ", pos);
    }

    /**
     * Grabs path from AutoConfig and prepares for running auto
     */
    public void initStart() {

        // Initialize Follower
        follower = Constants.createFollower(hardwareMap);

        // Fields
        Field[] fields;

        // Initialize paths from auto configuration
        AutoConfig paths;

        if (StartConfig.color == StartConfig.AllianceColor.RED) {
            if (StartConfig.position == StartConfig.StartPosition.WALL) {
                // COLOR: RED
                // START: WALL

                paths = new AutoConfig.RED_WALL(follower);
                fields = AutoConfig.RED_WALL.class.getFields();
            } else {
                // COLOR: RED
                // START: GOAL

                paths = new AutoConfig.RED_GOAL(follower);
                fields = AutoConfig.RED_GOAL.class.getFields();
            }
        } else {
            if (StartConfig.position == StartConfig.StartPosition.WALL) {
                // COLOR: BLUE
                // START: WALL

                paths = new AutoConfig.BLUE_WALL(follower);
                fields = AutoConfig.BLUE_WALL.class.getFields();
            } else {
                // COLOR: BLUE
                // START: GOAL

                paths = new AutoConfig.BLUE_GOAL(follower);
                fields = AutoConfig.BLUE_GOAL.class.getFields();
            }
        }

        // Finding paths (cool stuff)
        for (Field field : fields) {
            if (field.getType() == PathChain.class && field.getName().toLowerCase().startsWith("p")) {
                try {
                    PathChain p = (PathChain) field.get(paths);

                    if (p != null) {
                        pathSequence.add(new NamedPath(field.getName(), p));
                    }
                } catch (IllegalAccessException e) {
                    telemetry.addData("Error", "Couldn't find path: " + field.getName());
                }
            }
        }

        // Alphabetical sorting for path order
        pathSequence.sort(Comparator.comparing(o -> o.name));
        Drawing.init();
    }

    @Override
    public void start() {
        // Initializing start positions and alliance color
        initStart();

        // Initializing autonomous robot
        robot = new Robot(hardwareMap);

        // Sets starting pose
        if (!pathSequence.isEmpty()) {
            follower.setStartingPose(StartConfig.pose);
        }

        // Starting path
        startNextPath();
    }

    @Override
    public void loop() {
        // Update Follower
        follower.update();

        // Drawing robot
        Drawing.drawRobot(follower.getPose());
        Drawing.sendPacket();

        // State machine
        if (isDriving) {
            if (!follower.isBusy()) {
                isDriving = false;

                timer.reset();
                telemetry.addData("Status", "Path Finished. Running Action...");
            } else {
                telemetry.addData("Status", "Driving: " + getCurrentPathName());
            }
        } else {
            // State
            String completedPath = getCurrentPathName();
            boolean actionFinished = runCustomAction(completedPath);

            if (actionFinished) {
                startNextPath();
            }
        }

        // Displaying alliance color and starting position for auto
        telemetry.addData("ALLIANCE COLOR: ", StartConfig.color.toString());
        telemetry.addData("STARTING POSITION: ", StartConfig.position.toString());
        telemetry.update();
    }

    /**
     * Raises path index by 1 to transfer to next path
     */
    private void startNextPath() {
        pathIndex++;
        if (pathIndex < pathSequence.size()) {
            NamedPath next = pathSequence.get(pathIndex);
            follower.followPath(next.pathChain, true);
            isDriving = true;
        }
    }

    /**
     * Gets current path name
     *
     * @return current path name
     */
    private String getCurrentPathName() {
        if (pathIndex >= 0 && pathIndex < pathSequence.size()) {
            return pathSequence.get(pathIndex).name;
        }
        return "NONE";
    }
}