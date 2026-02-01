package org.firstinspires.ftc.teamcode.Bambusa.SLURPIE;

public class Artifact {
    public static int inputSmoothing = 4;

    public double tx;
    public double ty;
    public double ta;

    private double ticksNotSeen = 0;

    public Artifact() {
        tx = -1;
        ty = -1;
        ta = -1;
    }

    /**
     * Tries to smooth blind frames by freezing artifact position on screen (like a sort of lag).
     *
     * @param data the limelight data outputs which include tx, ty, ta
     */
    public void smoothData(double[] data) {
        // If data is seen
        if (data[0] != -1) {
            // Resetting
            ticksNotSeen = 0;

            // Updating data
            tx = data[0];
            ty = data[1];
            ta = data[2];
        } else {
            // Artifact not seen
            ticksNotSeen++;

            // If artifact is not seen, then it essentially freezes input
            // This is the smoothing method, to reduce noise
            if (ticksNotSeen < inputSmoothing) {
                data = new double[]{tx, ty, ta};
            } else {
                data = new double[]{-1, -1, -1};
            }
        }
    }
}
