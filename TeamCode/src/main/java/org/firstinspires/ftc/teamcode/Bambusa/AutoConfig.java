package org.firstinspires.ftc.teamcode.Bambusa;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class AutoConfig {
    // Converted to Radians
    public static Pose closeShootingPosition = new Pose(60, 90, Math.toRadians(-43));
    public static Pose farShootingPosition = new Pose(0, 0, 0);

    public static class WallStartAuto {
        public PathChain startToBackRow;
        public PathChain backRowToLaunch;
        public PathChain launch1;
        public PathChain launchToMiddleRow;
        public PathChain middleRowToLaunch;
        public PathChain launch2;
        public PathChain launchToFrontRow;
        public PathChain frontRowToLaunch;
        public PathChain launch3;

        private Pose p(double x, double y) {
            if (StartConfig.flipped) {
                double newX = (2 * StartConfig.midline) - x;
                return new Pose(newX, y);
            }
            return new Pose(x, y);
        }

        private double h(double headingRadians) {
            if (StartConfig.flipped) {
                return Math.PI - headingRadians;
            }
            return headingRadians;
        }

        public WallStartAuto(Follower follower) {
            Pose shootPose = p(closeShootingPosition.getX(), closeShootingPosition.getY());
            double shootHeading = h(closeShootingPosition.getHeading());

            // Start -> Back Row
            startToBackRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    p(63.500, 8.500),
                                    p(63.687, 35.700),
                                    p(20.000, 35.667)
                            )
                    ).setTangentHeadingInterpolation()
                    .build();

            // Back Row -> Launch
            backRowToLaunch = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    p(20.000, 35.667),
                                    p(49.341, 35.694),
                                    shootPose
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            // Launch 1
            launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    shootPose,
                                    shootPose
                            )
                    ).setLinearHeadingInterpolation(shootHeading, shootHeading)
                    .build();

            // Launch -> Middle Row
            launchToMiddleRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    shootPose,
                                    p(53.848, 58.044),
                                    p(20.000, 59.812)
                            )
                    ).setTangentHeadingInterpolation()
                    .build();

            // Middle Row -> Launch
            middleRowToLaunch = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    p(20.000, 59.812),
                                    p(52.258, 60.528),
                                    shootPose
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            // Launch 2
            launch2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    shootPose,
                                    shootPose
                            )
                    ).setLinearHeadingInterpolation(shootHeading, shootHeading)
                    .build();

            // Launch -> Front Row (Top Row)
            launchToFrontRow = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    shootPose,
                                    p(52.855, 84.319),
                                    p(20.000, 84.613)
                            )
                    ).setTangentHeadingInterpolation()
                    .build();

            // Front Row -> Launch
            frontRowToLaunch = follower.pathBuilder().addPath(
                            new BezierLine(
                                    p(20.000, 84.613),
                                    shootPose
                            )
                    ).setTangentHeadingInterpolation()
                    .setReversed()
                    .build();

            // Launch 3
            launch3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    shootPose,
                                    shootPose
                            )
                    ).setLinearHeadingInterpolation(shootHeading, shootHeading)
                    .build();
        }
    }
}