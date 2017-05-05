package se.liu.ida.noahe116.tddd78.swarm.common;

/**
 * Utility math functions that are not in Math.
 **/
public final class Math2 {
    private Math2() {}

    /**
     * Returns the floor modulus of the double arguments.
     * @param x the dividend
     * @param y the divisor
     * @return the floor modulus
     **/
    public static double floorMod(double x, double y) {
        double modulo = x % y;
        if (modulo < 0) modulo += y;
        return modulo;
    }
}
