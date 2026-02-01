package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class AutoConfig {
    public static double shootingTime = 2;

    /**
     * Paste paths here from Pedro Pathing Visualizer.<br>
     * Ensure that you remove 'true' from .setReversed(true), as it was removed in PedroPathing 2.0.0<br>
     * There are four paths to copy and paste, based on the team color and starting positions, named:<br>
     * <br>
     * - BLUE_WALL<br>
     * - BLUE_GOAL<br>
     * - RED_WALL<br>
     * - RED_GOAL<br>
     * <br>
     * Rename the path you copy and paste (originally named 'Paths') to one of the above class names, dependant
     * on the starting position of the auto.
     **/

    /* Instructions for Pedro Pathing Visualizer:
     * Order paths by adding a prefix to each path, p##. Ex: p00 executed first and p99 being the last executed.
     * Use word 'start' in the first path to activate shooting motors.
     * Use word 'end' in your last path to deactivate shooting motors.
     * Use word 'launch' in any of your paths to activate outtake to shoot ball.
     * These custom actions occur AFTER the specific trajectory is done, so if you name a path
     * p03_middle_to_launch, then launching will occur after the robot is at the launching site.
     */

    /** --- BLUE WALL AUTO --- **/
    public static class BLUE_WALL extends AutoConfig {
        public PathChain p01launch;
        public PathChain Path2;
        public PathChain Path3launch;
        public PathChain Path4;
        public PathChain Path5launch;
        public PathChain Path6end;

        public BLUE_WALL(Follower follower) {
            p01launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(116.700, 130.000),

                                    new Pose(87.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(216), Math.toRadians(220))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(87.000, 98.000),
                                    new Pose(99.900, 84.801),
                                    new Pose(121.000, 85.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path3launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(121.000, 85.000),

                                    new Pose(87.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(220))

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(87.000, 98.000),
                                    new Pose(78.800, 60.038),
                                    new Pose(122.000, 60.178)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path5launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(122.000, 60.178),

                                    new Pose(87.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(220))

                    .build();

            Path6end = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(87.000, 98.000),

                                    new Pose(100.000, 35.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(220), Math.toRadians(0))

                    .build();
        }
    }

    /** --- BLUE GOAL AUTO ---  **/ // update
    public static class BLUE_GOAL extends AutoConfig {
        public PathChain p01travel;
        public PathChain p03launch1;

        public BLUE_GOAL(Follower follower) {
            p01travel = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 9.000),

                                    new Pose(79.735, 79.551)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(90))

                    .build();

            p03launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(79.735, 79.551),

                                    new Pose(80.579, 18.732)
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();
        }
    }

    /** --- RED WALL AUTO --- **/
    public static class RED_WALL extends AutoConfig {
        public PathChain p01launch;
        public PathChain p02;
        public PathChain p03launch;
        public PathChain p04;
        public PathChain p05launch;
        public PathChain path06end;

        public RED_WALL(Follower follower) {
            p01launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.000, 9.300),

                                    new Pose(57.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(-40))

                    .build();

            p02 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(57.000, 98.000),
                                    new Pose(54.883, 85.864),
                                    new Pose(22.634, 84.578)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            p03launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(22.634, 84.578),

                                    new Pose(57.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-178), Math.toRadians(-40))
                    .setReversed()
                    .build();

            p04 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(57.000, 98.000),
                                    new Pose(58.168, 60.711),
                                    new Pose(21.875, 60.544)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            p05launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(21.875, 60.544),

                                    new Pose(57.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(-40))

                    .build();

            path06end = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.000, 98.000),

                                    new Pose(46.000, 35.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-40), Math.toRadians(180))

                    .build();
        }
    }

    /** --- RED GOAL AUTO --- **/
    public static class RED_GOAL extends AutoConfig {
        public PathChain p01launch;
        public PathChain Path2;
        public PathChain Path3launch;
        public PathChain Path4;
        public PathChain Path5launch;
        public PathChain Path6end;

        public RED_GOAL(Follower follower) {
            p01launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(27.300, 130.000),

                                    new Pose(57.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-36), Math.toRadians(-40))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(57.000, 98.000),
                                    new Pose(44.092, 84.801),
                                    new Pose(22.983, 84.794)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path3launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(22.983, 84.794),

                                    new Pose(57.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-180), Math.toRadians(-40))

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(57.000, 98.000),
                                    new Pose(65.175, 60.038),
                                    new Pose(22.174, 60.178)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            Path5launch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(22.174, 60.178),

                                    new Pose(67.000, 98.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-180), Math.toRadians(-40))

                    .build();

            Path6end = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(67.000, 98.000),

                                    new Pose(44.000, 35.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-40), Math.toRadians(-180))

                    .build();
        }
    }
}