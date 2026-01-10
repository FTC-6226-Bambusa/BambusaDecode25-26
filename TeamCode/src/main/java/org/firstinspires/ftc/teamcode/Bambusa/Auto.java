package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (name = "__AUTO__", group = "COMPETITION")
public class Auto extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        START_TO_BACK_ROW,
        BACK_ROW_TO_LAUNCH,
        LAUNCH1,
        LAUNCH_TO_MIDDLE_ROW,
        MIDDLE_ROW_TO_LAUNCH,
        LAUNCH2,
        LAUNCH_TO_FRONT_ROW,
        FRONT_ROW_TO_LAUNCH,
        LAUNCH3,
        EXIT
    }

    PathState pathState;

    // All trajectories
    private PathChain startToBackRow, backRowToLaunch, launch1, launchToMiddleRow,
                      middleRowToLaunch, launch2, launchToFrontRow, frontRowToLaunch,
                      launch3;

    /**
     * Builds the actual trajectories of the paths.<br>
     * Use PedroPathing visualizer to create.
     */
    public void buildPaths() {
        startToBackRow = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        backRowToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        launch1 = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        launchToMiddleRow = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        middleRowToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        launch2 = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        launchToFrontRow = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        frontRowToLaunch = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();

        launch3 = follower.pathBuilder()
                .addPath(new BezierLine(StartConfig.pose, StartConfig.pose))
                .build();
    }

    /**
     * Updates which path the follower is following.<br>
     * Switches path after it has completed one.
     */
    public void statePathUpdate() {
        switch (pathState) {
            case START_TO_BACK_ROW:
                follower.followPath(startToBackRow, true);
                setPathState(PathState.BACK_ROW_TO_LAUNCH);
                break;

            case BACK_ROW_TO_LAUNCH:
                follower.followPath(backRowToLaunch, true);
                setPathState(PathState.LAUNCH1);
                break;

            case LAUNCH1:
                follower.followPath(launch1, true);
                setPathState(PathState.LAUNCH_TO_MIDDLE_ROW);
                break;

            case LAUNCH_TO_MIDDLE_ROW:
                follower.followPath(launchToMiddleRow, true);
                setPathState(PathState.MIDDLE_ROW_TO_LAUNCH);
                break;

            case MIDDLE_ROW_TO_LAUNCH:
                follower.followPath(middleRowToLaunch, true);
                setPathState(PathState.LAUNCH2);
                break;

            case LAUNCH2:
                follower.followPath(launch2, true);
                setPathState(PathState.LAUNCH_TO_FRONT_ROW);
                break;

            case LAUNCH_TO_FRONT_ROW:
                follower.followPath(launchToFrontRow, true);
                setPathState(PathState.FRONT_ROW_TO_LAUNCH);
                break;

            case FRONT_ROW_TO_LAUNCH:
                follower.followPath(frontRowToLaunch, true);
                setPathState(PathState.LAUNCH3);
                break;

            case LAUNCH3:
                follower.followPath(launch3, true);
                setPathState(PathState.EXIT);
                break;

            default:
                // do nothing
                break;
        }
    }

    /**
     * Helper function to switch trajectory
     *
     * @param newState next state
     */
    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathState = PathState.START_TO_BACK_ROW;
        pathTimer = new Timer();
        opModeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);

        buildPaths();
        follower.setPose(StartConfig.pose);
    }

    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("Path State: ", pathState.toString());
        telemetry.addData("X: ", pathState.toString());
        telemetry.addData("Y: ", pathState.toString());
        telemetry.addData("Heading: ", pathState.toString());
        telemetry.addData("Path Time: ", pathState.toString());
    }
}
