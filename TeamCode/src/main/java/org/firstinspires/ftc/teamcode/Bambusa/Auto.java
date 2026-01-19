package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    // Follower
    private Follower follower;

    private AutoConfig paths;

    // Timer
    private final ElapsedTime timer = new ElapsedTime();

    // State machine
    private int pathIndex = -1;
    private boolean isDriving = false;

    // Ordered paths
    private List<NamedPath> pathSequence = new ArrayList<>();

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

        switch (pathName) {
            case "p03launch1":
                telemetry.addData("Action: ", "Shooting");

                // Shooting ball
                robot.outtake.enable();

                if (timer.milliseconds() > 2000) {
                    robot.disableLaunchers();

                    return true;
                }

                return false;

            default:
                // Nothing happens if there is no custom action
                return true;
        }
    }

    @Override
    public void init() {
        // Updating pose
        StartConfig.updatePose();

        // Start position telemetry
        telemetry.addData("START X: ", StartConfig.pose.getX());
        telemetry.addData("START Y: ", StartConfig.pose.getY());
        telemetry.addData("START H: ", StartConfig.pose.getHeading());

        // Initialize Follower
        follower = Constants.createFollower(hardwareMap);

        // Fields
        Field[] fields;

        // Initialize Path
        if (StartConfig.color == StartConfig.AllianceColor.RED) {
            if (StartConfig.position == StartConfig.position.WALL) {
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
            if (StartConfig.position == StartConfig.position.WALL) {
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

        for (Field field : fields) {
            if (field.getType() == PathChain.class && field.getName().startsWith("p")) {
                try {
                    PathChain p = (PathChain) field.get(paths);
                    if (p != null) {
                        pathSequence.add(new NamedPath(field.getName(), p));
                    }
                } catch (IllegalAccessException e) {
                    telemetry.addData("Error", "Could not access path: " + field.getName());
                }
            }
        }

        // Alphabetical sorting for path order
        Collections.sort(pathSequence, Comparator.comparing(o -> o.name));
        Drawing.init();

        if (!pathSequence.isEmpty()) {
            follower.setStartingPose(StartConfig.pose);
        }

        telemetry.addData("Path: ", "Initialized");
        telemetry.addData("Sequence Size: ", pathSequence.size());

        for(NamedPath np : pathSequence) {
            telemetry.addData("Found Path: ", np.name);
        }

        telemetry.update();
    }

    @Override
    public void start() {
        robot = new Robot(hardwareMap);

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

        // Telemetry
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
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