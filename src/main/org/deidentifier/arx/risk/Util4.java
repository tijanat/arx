package org.deidentifier.arx.risk;

public class Util4 {

    /**
     * TRIGAMMA Trigamma function.
     * TRIGAMMA(X) returns trigamma(x) = d**2 log(gamma(x)) / dx**2
     * If X is a matrix, returns the trigamma function evaluated at each element.
     * 
     * Reference:
     * 
     * B Schneider,
     * Trigamma Function,
     * Algorithm AS 121,
     * Applied Statistics,
     * Volume 27, Number 1, page 97-99, 1978.
     * From http://www.psc.edu/~burkardt/src/dirichlet/dirichlet.f
     */
    static double small = 1e-4;
    static double large = 8;
    static double c     = Math.pow(Math.PI, 2.0) / 6.0;
    static double c1    = -2.404113806319188570799476;
    static double b2    = 1.0 / 6.0;
    static double b4    = -1.0 / 30.0;
    static double b6    = 1.0 / 42.0;
    static double b8    = -1.0 / 30.0;
    static double b10   = 5.0 / 66.0;

    public static double trigamma(double x) {
        double y = 0.0;
        double z = 0.0;

        if (Double.isInfinite(x) || Double.isNaN(x)) {
            return 0.0 / 0.0;
        }

        // zero or negative integer
        if (x <= 0.0 && Math.floor(x) == x) {
            return 1.0 / 0.0;
        }

        // Negative non-integer
        if (x < 0 && Math.floor(x) != x) {
            // Use the derivative of the digamma reflection formula:
            // -trigamma(-x) = trigamma(x+1) - (pi*csc(pi*x))^2
            y = -trigamma(-x + 1.0) + Math.pow(Math.PI * (1.0 / Math.sin(-Math.PI * x)), 2.0);
            return y;
        }

        // Small value approximation
        if (x <= small) {
            y = 1.0 / (x * x) + c + c1 * x;
            return y;
        }

        // Reduce to trigamma(x+n) where ( X + N ) >= large.
        while (true) {
            if (x > small && x < large) {
                y = y + 1.0 / (x * x);
                x = x + 1.0;
            } else {
                break;
            }
        }

        if (x >= large) {
            z = 1.0 / (x * x);
            y = y + 0.5 * z + (1.0 + z * (b2 + z * (b4 + z * (b6 + z * (b8 + z * b10))))) / x;
        }

        return y;
    }
    
    /**
     * DIGAMMA Digamma function.
     * DIGAMMA(X) returns digamma(x) = d log(gamma(x)) / dx
     * If X is a matrix, returns the digamma function evaluated at each element.
     * 
     * Reference:
     * 
     * J Bernardo,
     * Psi ( Digamma ) Function,
     * Algorithm AS 103,
     * Applied Statistics,
     * Volume 25, Number 3, pages 315-317, 1976.
     * From http://www.psc.edu/~burkardt/src/dirichlet/dirichlet.f
     */
    public static double digamma(double x) {
        double y = 0.0;
        double r = 0.0;

        if (Double.isInfinite(x) || Double.isNaN(x)) {
            return 0.0 / 0.0;
        }

        if (x == 0.0) {
            return -1.0 / 0.0;
        }

        if (x < 0.0) {
            // Use the reflection formula (Jeffrey 11.1.6):
            // digamma(-x) = digamma(x+1) + pi*cot(pi*x)
            y = Util4.digamma(-x + 1) + (Math.PI * (1.0 / Math.tan(-Math.PI * x)));
            return y;
            // This is related to the identity
            // digamma(-x) = digamma(x+1) - digamma(z) + digamma(1-z)
            // where z is the fractional part of x
            // For example:
            // digamma(-3.1) = 1/3.1 + 1/2.1 + 1/1.1 + 1/0.1 + digamma(1-0.1)
            // = digamma(4.1) - digamma(0.1) + digamma(1-0.1)
            // Then we use
            // digamma(1-z) - digamma(z) = pi*cot(pi*z)
        }

        // Use approximation if argument <= small.
        if (x <= SMALL) {
            y = ((y + D1) - (1.0 / x)) + (D2 * x);
            return y;
        }

        // Reduce to digamma(X + N) where (X + N) >= large.
        while (true) {
            if ((x > SMALL) && (x < LARGE)) {
                y = y - (1.0 / x);
                x = x + 1.0;
            } else {
                break;
            }
        }

        // Use de Moivre's expansion if argument >= large.
        // In maple: asympt(Psi(x), x);
        if (x >= LARGE) {
            r = 1.0 / x;
            y = (y + Math.log(x)) - (0.5 * r);
            r = r * r;
            y = y - (r * (S3 - (r * (S4 - (r * (S5 - (r * (S6 - (r * S7)))))))));
        }

        return y;
    }

    private final static double LARGE = 9.5;
    private final static double D1    = -0.5772156649015328606065121; // digamma(1)
    private final static double D2    = Math.pow(Math.PI, 2.0) / 6.0;
    private final static double SMALL = 1e-6;
    private final static double S3    = 1.0 / 12.0;
    private final static double S4    = 1.0 / 120.0;
    private final static double S5    = 1.0 / 252.0;
    private final static double S6    = 1.0 / 240.0;
    private final static double S7    = 1.0 / 132.0;
}
