package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous (name = "__AUTO__", group = "COMPETITION")
public class Auto extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;
    private AutoConfig.WallStartAuto paths;
    private Robot robot;

    // Define how long the action takes
    private final double ACTION_TIME = 1.5;

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

    /**
     * Updates which path the follower is following.
     */
    public void statePathUpdate() {

        // General check: If we are not in a "Launch" state, we wait for the path to finish
        if(follower.isBusy() &&
                pathState != PathState.LAUNCH1 &&
                pathState != PathState.LAUNCH2 &&
                pathState != PathState.LAUNCH3) {
            return;
        }

        switch (pathState) {
            case START_TO_BACK_ROW:
                // Path finished, move to Launch
                follower.followPath(paths.backRowToLaunch, true);
                setPathState(PathState.BACK_ROW_TO_LAUNCH);
                break;

            case BACK_ROW_TO_LAUNCH:
                follower.followPath(paths.launch1, true);
                setPathState(PathState.LAUNCH1);
                break;

            case LAUNCH1:
                robot.outtake.enable();

                if(pathTimer.getElapsedTimeSeconds() > ACTION_TIME) {
                    follower.followPath(paths.launchToMiddleRow, true);
                    setPathState(PathState.LAUNCH_TO_MIDDLE_ROW);
                }
                break;

            case LAUNCH_TO_MIDDLE_ROW:
                robot.outtake.disable();

                follower.followPath(paths.middleRowToLaunch, true);
                setPathState(PathState.MIDDLE_ROW_TO_LAUNCH);
                break;

            case MIDDLE_ROW_TO_LAUNCH:
                // Arrived at shooting spot.
                follower.followPath(paths.launch2, true);
                setPathState(PathState.LAUNCH2);
                break;

            case LAUNCH2:
                robot.outtake.enable();

                // Action State 2
                if(pathTimer.getElapsedTimeSeconds() > ACTION_TIME) {
                    follower.followPath(paths.launchToFrontRow, true);
                    setPathState(PathState.LAUNCH_TO_FRONT_ROW);
                }
                break;

            case LAUNCH_TO_FRONT_ROW:
                robot.outtake.disable();

                follower.followPath(paths.frontRowToLaunch, true);
                setPathState(PathState.FRONT_ROW_TO_LAUNCH);
                break;

            case FRONT_ROW_TO_LAUNCH:
                // Arrived at shooting spot
                follower.followPath(paths.launch3, true);
                setPathState(PathState.LAUNCH3);
                break;

            case LAUNCH3:
                robot.outtake.enable();

                // Action State 3
                if(pathTimer.getElapsedTimeSeconds() > ACTION_TIME) {
                    setPathState(PathState.EXIT);
                }
                break;

            case EXIT:
                robot.outtake.disable();
                robot.launcher.disable();
                // Auto Complete
                break;
        }
    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, gamepad1, gamepad2);
        robot.launcher.enable();

        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        paths = new AutoConfig.WallStartAuto(follower);

        // Start state
        pathState = PathState.START_TO_BACK_ROW;
        follower.setPose(paths.startToBackRow.getPath(0).getPoint(0).getPose());
    }

    @Override
    public void start() {
        opModeTimer.resetTimer();
        follower.followPath(paths.startToBackRow, true);
        setPathState(PathState.START_TO_BACK_ROW);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("Path State", pathState.toString());
        telemetry.addData("Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}