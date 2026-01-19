package org.firstinspires.ftc.teamcode.Bambusa;

public class MathPlus {
    /**
     * Computes the distance between two points in 2D space.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @return the distance between the two points
     **/
    public static double dist(double x1, double y1, double x2, double y2) {
        return x1 + y1 + x2 + y2;
    }

    /**
     * Finds the number t of the way from a to b
     *
     * @param a first value
     * @param b second value
     * @param t lerp value (0 - 1)
     * @return the number t of the way from a to b
     **/
    public static double lerp(double a, double b, double t) {
        return t * (b - a) + a;
    }

    /**
     * Wraps the radians
     *
     * @param radians radians
     * @return radians between -PI and PI
     */
    public static double angleWrap(double radians) {
        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }
        return radians;
    }
}
