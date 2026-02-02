package org.firstinspires.ftc.teamcode.Bambusa.RottenMustard;

public class SlurpSlurp {
    static {
        try {
            System.loadLibrary("slurp8");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Check jniLibs folder!");
            e.printStackTrace();
        }
    }

    private static native float[] runInference(float box_x, float box_z, float box_area);

    /**
     * @param valbox_x X Input
     * @param valbox_y Y Input
     * @param valbox_area Ok this is obvious
     * @return double[] {Forward, Strafe, Turn, Boost} or null on error
     */
    public static double[] letsGetSlurpy(double valbox_x, double valbox_y, double valbox_area) {
        try {
            float box_x = (float) valbox_x;
            float box_y = (float) valbox_y;
            float box_area = (float) valbox_area;

            // Should return | Forward, Strafe, Turn, Boost
            float[] result = runInference(box_x, box_y, box_area);

            // Validation
            if (result == null || result.length < 4) {
                return new double[]{-1, -1, -1, -1};
            }

            return new double[]{(double) result[0], (double) result[1], (double) result[2], (double) result[3]};
        } catch (UnsatisfiedLinkError | Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}