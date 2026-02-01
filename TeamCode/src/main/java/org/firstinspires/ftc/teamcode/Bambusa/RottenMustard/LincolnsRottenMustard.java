package org.firstinspires.ftc.teamcode.Bambusa.RottenMustard;

public class LincolnsRottenMustard {
    static {
        try {
            System.loadLibrary("shared");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load Rust library. Check jniLibs folder!");
            e.printStackTrace();
        }
    }
    private static native float[] runInference(float x, float z);

    /**
     * @param val1 X Input
     * @param val2 Z Input
     * @return double[] {Yaw, Power} or null on error
     */
    public static double[] getCoords(double val1, double val2) {
        try {
            float x = (float) val1;
            float z = (float) val2;

            float[] result = runInference(x, z);

            // Validation
            if (result == null || result.length < 2) {
                return null;
            }

            return new double[] { (double)result[0], (double)result[1] };

        } catch (UnsatisfiedLinkError | Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}