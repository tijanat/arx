/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2015 Florian Kohlmayer, Fabian Prasser
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.deidentifier.arx.risk;

import org.deidentifier.arx.risk.RiskEstimateBuilder.WrappedBoolean;

/**
 * This class implements Newton Raphson algorithm for the Pitman Model to obtain
 * results for the Maximum Likelihood estimation. For further details see
 * Hoshino, 2001
 * 
 * @author Fabian Prasser
 * @author Michael Schneider
 * @version 1.0
 */
class AlgorithmNewtonPitman extends AlgorithmNewtonRaphson {

    /**
     * The number of equivalence class sizes (keys) and corresponding frequency (values)
     */
    private final int[]  classes;

    /** The total number of entries in our sample data set */
    private final double numberOfEntries;

    /** The total number of equivalence classes in the sample data set */
    private final double numberOfEquivalenceClasses;

    public static int    match = 0;

    public static int    total = 0;

    /**
     * Creates an instance of the Newton-Raphson Algorithm to determine the
     * Maximum Likelihood Estimator for the Pitman Model
     * 
     * @param u total number of entries in the sample data set
     * @param n size of sample
     * @param classes
     */
    AlgorithmNewtonPitman(final double u,
                          final double n,
                          final int[] classes,
                          final int maxIterations,
                          final double accuracy,
                          final WrappedBoolean stop) {
        super(accuracy, maxIterations, stop);
        numberOfEquivalenceClasses = u;
        numberOfEntries = n;
        this.classes = classes;
        match = 0;
        total = 0;
    }

    private void compare(double d1, double d2, String context) {
        total++;
        double min = Math.min(d1, d2);
        double max = Math.max(d1, d2);
        if ((1.0d - (min / max)) > 0.01d) { // Difference by more than 1%
            System.out.println(context + ": " + d1 + "!=" + d2);
        } else {
            match++;
        }
    }

    private void compare(double[] d1, double[] d2, String context) {
        compare(d1[0], d2[0], context + "[0]");
        compare(d1[1], d2[1], context + "[1]");
        if (d1.length == 3) {
            compare(d1[2], d2[2], context + "[2]");
        }
    }

    private double[] computeWY(double numberOfEquivalenceClasses, double a, double t) {
        // double[] w1 = computeWY1(numberOfEquivalenceClasses, a, t);
        double[] w4 = computeWY4(numberOfEquivalenceClasses, a, t);
        // compare(w1, w4, "WY");
        return w4;
    }

    private double computeX(double numberOfEntries, double t) {
        // double x1 = computeX1(numberOfEntries, t);
        double x4 = computeX4(numberOfEntries, t);
        // compare(x1, x4, "X");
        return x4;
    }

    private double computeZStar(double n, double a) {
        // double z1 = computeZStar1(n, a);
        double z4 = computeZStar4(n, a);
        // compare(z1, z4, "Z*");
        return z4;
    }

    private double[] computeVXZ(double numberOfEquivalenceClasses, double a, double t) {
        // double[] v1 = computeVXZ1(numberOfEquivalenceClasses, a, t);
        double[] v7 = computeVXZ7(numberOfEquivalenceClasses, a, t);
        // compare(v1, v7, "VXZ: " + numberOfEquivalenceClasses + " " + a + " " + t);
        return v7;
    }

    private double computeYStar(double n, double a) {
        // double ystar1 = computeYStar1(n, a);
        double ystar7 = computeYStar7(n, a);
        // compare(ystar1, ystar7, "Y*");
        return ystar7;
    }

    private double[] computeVXZ1(double numberOfEquivalenceClasses, double a, double t) {

        double v = 0;
        double x = 0;
        double z = 0;

        // For each...
        // TODO: Find closed form
        for (int i = 1; i < numberOfEquivalenceClasses; i++) {
            double val0 = (t + (i * a));
            double val1 = 1d / (val0 * val0);
            double val2 = i * val1;
            double val3 = i * val2;
            v += val1; // Compute d^2L/(dtheta)^2
            z += val2; // Compute d^2L/(d theta d alpha)
            x += val3; // Compute d^2L/(d alpha)^2

        }
        return new double[] { v, x, z };
    }

    private double[] computeVXZ2(double numberOfEquivalenceClasses, double a, double t) {

        // this is verified twice, with Matlab/Mupad and mathematica!

        double n = numberOfEquivalenceClasses - 1d;
        double val0 = Util4.trigamma((t / a) + 1);
        double val1 = Util4.digamma(n + (t / a) + 1);
        double val2 = Util4.trigamma((a + t + (a * n)) / a);
        double val3 = Util4.trigamma((a + t) / a);
        double val4 = Util4.digamma((a + t) / a);
        double val5 = Util4.digamma((t / a) + 1);
        double val6 = a * a;

        double v = (val3 - val2) / (val6);
        double z = (((a * val1) + (t * val2)) - (a * val4) - (t * val3)) / (val6 * a);
        double x = (((((val6 * n) - (t * t * val2)) + (t * t * val0)) - (2 * a * t * val1)) + (2 * a * t * val5)) / (val6 * val6);
        return new double[] { v, x, z };
    }

    private double[] computeVXZ2_ALT(double numberOfEquivalenceClasses, double a, double t) {

        double n = numberOfEquivalenceClasses - 1d;
        double val0 = Util4.trigamma((t / a) + 1);
        double val1 = Util4.trigamma(n + (t / a));
        double val2 = Util4.digamma((t / a) + 1);
        double val3 = Util4.digamma(n + (t / a));
        double val4 = a * a;
        double val5 = t * t;
        double v = (Util4.trigamma((a + t) / a) - val1) / (val4);
        double z = (((a * val3) + (t * val1)) - (a * val2) - (t * val0)) / (val4 * a);
        double x = (((val4 * n) - val4 - (val5 * val1) - (2 * a * t * val3)) + (val5 * val0) + (2 * a * t * val2)) / (val4 * val4);
        return new double[] { v, x, z };

    }

    private double[] computeVXZ3(double numberOfEquivalenceClasses, double a, double t) {

        double n = numberOfEquivalenceClasses - 1d;
        double val0 = Util.trigamma((t / a) + 1);
        double val1 = Util.trigamma(n + (t / a));
        double val2 = Util4.digamma((t / a) + 1);
        double val3 = Util4.digamma(n + (t / a));
        double val4 = a * a;
        double val5 = t * t;
        double v = (Util.trigamma((a + t) / a) - val1) / (val4);
        double z = (((a * val3) + (t * val1)) - (a * val2) - (t * val0)) / (val4 * a);
        double x = (((val4 * n) - val4 - (val5 * val1) - (2 * a * t * val3)) + (val5 * val0) + (2 * a * t * val2)) / (val4 * val4);
        return new double[] { v, x, z };

    }

    private double[] computeVXZ5(double numberOfEquivalenceClasses, double a, double t) {

        double n = numberOfEquivalenceClasses - 1d;
        double val0 = Util5.trigamma((t / a) + 1);
        double val1 = Util5.trigamma(n + (t / a));
        double val2 = Util4.digamma((t / a) + 1);
        double val3 = Util4.digamma(n + (t / a));
        double val4 = a * a;
        double val5 = t * t;
        double v = (Util5.trigamma((a + t) / a) - val1) / (val4);
        double z = (((a * val3) + (t * val1)) - (a * val2) - (t * val0)) / (val4 * a);
        double x = (((val4 * n) - val4 - (val5 * val1) - (2 * a * t * val3)) + (val5 * val0) + (2 * a * t * val2)) / (val4 * val4);
        return new double[] { v, x, z };

    }

    private double[] computeVXZ6(double numberOfEquivalenceClasses, double a, double t) {

        // this is verified twice, with Matlab/Mupad and mathematica!

        double n = numberOfEquivalenceClasses - 1d;
        double val0 = Util6.trigamma((t / a) + 1);
        double val1 = Util6.digamma(n + (t / a) + 1);
        double val2 = Util6.trigamma((a + t + (a * n)) / a);
        double val3 = Util6.trigamma((a + t) / a);
        double val4 = Util6.digamma((a + t) / a);
        double val5 = Util6.digamma((t / a) + 1);
        double val6 = a * a;

        double v = (val3 - val2) / (val6);
        double z = (((a * val1) + (t * val2)) - (a * val4) - (t * val3)) / (val6 * a);
        double x = (((((val6 * n) - (t * t * val2)) + (t * t * val0)) - (2 * a * t * val1)) + (2 * a * t * val5)) / (val6 * val6);
        return new double[] { v, x, z };
    }

    private double[] computeVXZ7(double numberOfEquivalenceClasses, double a, double t) {

        // this is verified twice, with Matlab/Mupad and mathematica!

        double n = numberOfEquivalenceClasses - 1d;
        double val0 = Util6.trigamma((t / a) + 1);
        double val1 = Util4.digamma(n + (t / a) + 1);
        double val2 = Util6.trigamma((a + t + (a * n)) / a);
        double val3 = Util6.trigamma((a + t) / a);
        double val4 = Util4.digamma((a + t) / a);
        double val5 = Util4.digamma((t / a) + 1);
        double val6 = a * a;

        double v = (val3 - val2) / (val6);
        double z = (((a * val1) + (t * val2)) - (a * val4) - (t * val3)) / (val6 * a);
        double x = (((((val6 * n) - (t * t * val2)) + (t * t * val0)) - (2 * a * t * val1)) + (2 * a * t * val5)) / (val6 * val6);
        return new double[] { v, x, z };
    }

    private double[] computeWY1(double numberOfEquivalenceClasses, double a, double t) {
        double w = 0;
        double y = 0;
        for (int i = 1; i < numberOfEquivalenceClasses; i++) {
            double val0 = 1d / (t + (i * a));
            double val1 = i * val0;
            w += val0;
            y += val1;
        }
        return new double[] { w, y };
    }

    private double[] computeWY2(double numberOfEquivalenceClasses, double a, double t) {
        double n = numberOfEquivalenceClasses - 1d;
        double dVal0 = Util.digamma(n + (t / a) + 1d);
        double dVal1 = Util.digamma((a + t) / a);
        double w = (dVal0 - dVal1) / a;
        double y = ((-t * dVal0) + (a * n) + (t * dVal1)) / (a * a);
        return new double[] { w, y };
    }

    private double[] computeWY3(double numberOfEquivalenceClasses, double a, double t) {
        double n = numberOfEquivalenceClasses - 1d;
        double dVal0 = Util2.digamma(n + (t / a) + 1d);
        double dVal1 = Util2.digamma((a + t) / a);
        double w = (dVal0 - dVal1) / a;
        double y = ((-t * dVal0) + (a * n) + (t * dVal1)) / (a * a);
        return new double[] { w, y };
    }

    private double[] computeWY4(double numberOfEquivalenceClasses, double a, double t) {
        double n = numberOfEquivalenceClasses - 1d;
        double dVal0 = Util4.digamma(n + (t / a) + 1d);
        double dVal1 = Util4.digamma((a + t) / a);
        double w = (dVal0 - dVal1) / a;
        double y = ((-t * dVal0) + (a * n) + (t * dVal1)) / (a * a);
        return new double[] { w, y };
    }

    private double[] computeWY6(double numberOfEquivalenceClasses, double a, double t) {
        double n = numberOfEquivalenceClasses - 1d;
        double dVal0 = Util6.digamma(n + (t / a) + 1d);
        double dVal1 = Util6.digamma((a + t) / a);
        double w = (dVal0 - dVal1) / a;
        double y = ((-t * dVal0) + (a * n) + (t * dVal1)) / (a * a);
        return new double[] { w, y };
    }

    private double computeX1(double numberOfEntries, double t) {
        double x = 0;
        for (int i = 1; i < numberOfEntries; i++) {
            x += 1d / (t + i);
        }
        return x;
    }

    private double computeX2(double numberOfEntries, double t) {
        double n = numberOfEntries - 1d;
        return Util.digamma(n + t + 1d) - Util.digamma(t + 1d);
    }

    private double computeX3(double numberOfEntries, double t) {
        double n = numberOfEntries - 1d;
        return Util2.digamma(n + t + 1d) - Util2.digamma(t + 1d);
    }

    private double computeX4(double numberOfEntries, double t) {
        double n = numberOfEntries - 1d;
        return Util4.digamma(n + t + 1d) - Util4.digamma(t + 1d);
    }

    private double computeX6(double numberOfEntries, double t) {
        double n = numberOfEntries - 1d;
        return Util6.digamma(n + t + 1d) - Util6.digamma(t + 1d);
    }

    private double computeYStar1(double n, double a) {
        double yStar = 0;
        for (int j = 1; j < n; j++) {
            double val2 = j - a;
            yStar += 1d / (val2 * val2);
        }
        return yStar;
    }

    private double computeYStar2(double n, double a) {
        n = n - 1;
        return Util4.trigamma(1 - a) - Util4.trigamma((n + 1) - a); // TODO: First is a constant
    }

    private double computeYStar2_ALT(double n, double a) {
        return Util4.trigamma(1 - a) - Util4.trigamma(n - a); // TODO: First is a constant
    }

    private double computeYStar3(double n, double a) {
        return Util.trigamma(1 - a) - Util.trigamma(n - a); // TODO: First is a constant
    }

    private double computeYStar5(double n, double a) {
        return Util5.trigamma(1 - a) - Util5.trigamma(n - a); // TODO: First is a constant
    }

    private double computeYStar6(double n, double a) {
        n = n - 1;
        return Util6.trigamma(1 - a) - Util6.trigamma((n + 1) - a); // TODO: First is a constant
    }

    private double computeYStar7(double n, double a) {
        n = n - 1;
        return Util6.trigamma(1 - a) - Util6.trigamma((n + 1) - a); // TODO: First is a constant
    }

    private double computeZStar1(double n, double a) {
        double val0 = 0;
        for (int j = 1; j < n; j++) {
            val0 += 1d / (j - a);
        }
        return val0;
    }

    private double computeZStar2(double n, double a) {
        n = n - 1d;
        return Util.digamma((n - a) + 1d) - Util.digamma(1d - a); // TODO: This is a constant
    }

    private double computeZStar3(double n, double a) {
        n = n - 1d;
        return Util2.digamma((n - a) + 1d) - Util2.digamma(1d - a); // TODO: This is a constant
    }

    private double computeZStar4(double n, double a) {
        n = n - 1d;
        return Util4.digamma((n - a) + 1d) - Util4.digamma(1d - a); // TODO: This is a constant
    }

    private double computeZStar6(double n, double a) {
        n = n - 1d;
        return Util6.digamma((n - a) + 1d) - Util6.digamma(1d - a); // TODO: This is a constant
    }

    /**
     * The method for computing the first derivatives of the object functions
     * evaluated at the iterated solutions.
     * 
     * @param iteratedSolution
     *            the iterated vector of solutions.
     * @return the first derivatives of the object functions evaluated at the
     *         iterated solutions.
     */
    @Override
    protected double[][] firstDerivativeMatrix(final double[] iteratedSolution) {
        double t = iteratedSolution[0]; // Theta
        double a = iteratedSolution[1]; // Alpha

        double[] vxz = computeVXZ(numberOfEquivalenceClasses, a, t);
        double v = vxz[0];
        double x = vxz[1];
        double z = vxz[2];

        checkInterrupt();

        // For each class...
        double w = 0;
        double y = 0;
        for (int i = 0; i < classes.length; i += 2) {
            int key = classes[i];
            int value = classes[i + 1];
            double val0 = t + key;
            w += 1d / (val0 * val0);
            y += key != 1 ? value * computeYStar(key, a) : 0;
            checkInterrupt();
        }

        // Pack
        double[][] result = new double[2][2];
        result[0][0] = w - v;
        result[0][1] = 0 - z;
        result[1][0] = 0 - z;
        result[1][1] = 0 - x - y;

        // Return
        return result;
    }

    /**
     * The method for computing the object functions evaluated at the iterated
     * solutions.
     * 
     * @param iteratedSolution
     *            the iterated vector of solutions.
     * @return the object functions evaluated at the iterated solutions.
     */
    @Override
    protected double[] objectFunctionVector(final double[] iteratedSolution) {
        double t = iteratedSolution[0]; // Theta
        double a = iteratedSolution[1]; // Alpha

        // Compute z
        double z = 0;
        for (int i = 0; i < classes.length; i += 2) {
            int key = classes[i];
            int value = classes[i + 1];
            if (key != 1) {
                z += value * computeZStar(key, a);
            }
            checkInterrupt();
        }

        // Compute w,y,x
        double[] wy = computeWY(numberOfEquivalenceClasses, a, t);
        double w = wy[0];
        double y = wy[1];
        double x = computeX(numberOfEntries, t);

        // Return
        double[] result = new double[2];
        result[0] = w - x;
        result[1] = y - z;
        return result;
    }

}
