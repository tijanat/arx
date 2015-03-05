package org.deidentifier.arx.risk;

/*
 * Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
 * This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
 * http://www.cs.umass.edu/~mccallum/mallet
 * This software is provided under the terms of the Common Public License,
 * version 1.0, as published by http://www.opensource.org. For further
 * information, see the file `LICENSE' included with this distribution.
 */

/**
 * Various useful functions related to Dirichlet distributions.
 *
 * @author Andrew McCallum and David Mimno
 */

public class Util2 {

    /** Actually the negative Euler-Mascheroni constant */
    public static final double EULER_MASCHERONI    = -0.5772156649015328606065121;
    public static final double PI_SQUARED_OVER_SIX = Math.PI * Math.PI / 6;
    public static final double HALF_LOG_TWO_PI     = Math.log(2 * Math.PI) / 2;

    public static final double DIGAMMA_COEF_1      = 1 / 12;
    public static final double DIGAMMA_COEF_2      = 1 / 120;
    public static final double DIGAMMA_COEF_3      = 1 / 252;
    public static final double DIGAMMA_COEF_4      = 1 / 240;
    public static final double DIGAMMA_COEF_5      = 1 / 132;
    public static final double DIGAMMA_COEF_6      = 691 / 32760;
    public static final double DIGAMMA_COEF_7      = 1 / 12;
    public static final double DIGAMMA_COEF_8      = 3617 / 8160;
    public static final double DIGAMMA_COEF_9      = 43867 / 14364;
    public static final double DIGAMMA_COEF_10     = 174611 / 6600;

    public static final double DIGAMMA_LARGE       = 9.5;
    public static final double DIGAMMA_SMALL       = .000001;

    /**
     * Calculate digamma using an asymptotic expansion involving
     * Bernoulli numbers.
     */
    public static double digamma(double z) {
        // This is based on matlab code by Tom Minka
        double psi = 0;

        if (z < DIGAMMA_SMALL) {
            psi = EULER_MASCHERONI - (1 / z);
            return psi;
        }

        while (z < DIGAMMA_LARGE) {
            psi -= 1 / z;
            z++;
        }

        double invZ = 1 / z;
        double invZSquared = invZ * invZ;

        psi += Math.log(z) - .5 * invZ
               - invZSquared * (DIGAMMA_COEF_1 - invZSquared *
                                                 (DIGAMMA_COEF_2 - invZSquared *
                                                                   (DIGAMMA_COEF_3 - invZSquared *
                                                                                     (DIGAMMA_COEF_4 - invZSquared *
                                                                                                       (DIGAMMA_COEF_5 - invZSquared *
                                                                                                                         (DIGAMMA_COEF_6 - invZSquared *
                                                                                                                                           DIGAMMA_COEF_7))))));

        return psi;
    }
}
