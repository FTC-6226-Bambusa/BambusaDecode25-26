package org.firstinspires.ftc.teamcode.Bambusa.Configurations;

import com.pedropathing.follower.Follower;
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

    /**
     * Instructions for Pedro Pathing Visualizer:
     * Order paths by adding a prefix to each path, p##. Ex: p00 executed first and p99 being the last executed.
     * Use word 'start' in the first path to activate shooting motors.
     * Use word 'end' in your last path to deactivate shooting motors.
     * Use word 'launch' in any of your paths to activate outtake to shoot ball.
     * These custom actions occur AFTER the specific trajectory is done, so if you name a path
     * p03_middle_to_launch, then launching will occur after the robot is at the launching site.
     */

    /** --- BLUE WALL AUTO --- **/
    public static class BLUE_WALL extends AutoConfig {
        public PathChain p01travel;
        public PathChain p02collect;
        public PathChain p03launch1;

        public BLUE_WALL(Follower follower) {
            p01travel = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 9.000),
                                    new Pose(60.000, 65.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(90))

                    .build();

            p02collect = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 65.000),
                                    new Pose(60.000, 78.000)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            p03launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 78.000),
                                    new Pose(60.000, 50.000)
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();
        }
    }

    /** --- BLUE GOAL AUTO ---  **/
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
        public PathChain p01travel;
        public PathChain p02travel;
        public PathChain p03launch1;

        public RED_WALL(Follower follower) {
            p01travel = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.000, 9.000),

                                    new Pose(60.334, 34.226)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();

            p02travel = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(60.334, 34.226),

                                    new Pose(83.255, 34.286)
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            p03launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(83.255, 34.286),

                                    new Pose(83.199, 60.251)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();
        }
    }

    /** --- RED GOAL AUTO --- **/
    public static class RED_GOAL extends AutoConfig {
        public PathChain p01travel;
        public PathChain p02travel;
        public PathChain p03launch1;

        public RED_GOAL(Follower follower) {
            p01travel = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(83.000, 9.000),

                                    new Pose(83.000, 33.725)
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            p02travel = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(83.000, 33.725),

                                    new Pose(63.855, 77.937)
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            p03launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(63.855, 77.937),

                                    new Pose(63.129, 16.934)
                            )
                    ).setTangentHeadingInterpolation()

                    .build();
        }
    }
}