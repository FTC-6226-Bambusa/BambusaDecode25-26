package org.firstinspires.ftc.teamcode.Bambusa.SLURPIE;

public class Artifact {
    public static int inputSmoothing = 10;

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
     * @return whether drive should be enabled
     */
    public boolean smoothData(double[] data) {
        // If data is seen
        if (data[0] != -1) {
            // Resetting
            ticksNotSeen = 0;

            // Updating data
            tx = data[0];
            ty = data[1];
            ta = data[2];

            return false;
        } else {
            // Artifact not seen
            ticksNotSeen++;

            // If artifact is not seen, then it essentially freezes input
            // This is the smoothing method, to reduce noise
            if (ticksNotSeen < inputSmoothing) {
                data[0] = tx;
                data[1] = ty;
                data[2] = ta;

                return false;
            } else {
                data[0] = -1;
                data[1] = -1;
                data[2] = -1;

                return true;
            }
        }
    }
}
