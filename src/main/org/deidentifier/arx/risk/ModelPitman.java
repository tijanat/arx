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

import org.apache.commons.math3.special.Gamma;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.risk.RiskEstimateBuilder.WrappedBoolean;
import org.deidentifier.arx.risk.RiskEstimateBuilder.WrappedInteger;

/**
 * This class implements the PitmanModel, for details see Hoshino, 2001
 * 
 * @author Fabian Prasser
 * @author Michael Schneider
 * @version 1.0
 */
public class ModelPitman extends RiskModelPopulationBased {

    public static enum PitmanNumericMode {
        OPTIMIZED,
        DEFAULT,
        OPTIMIZED_WITH_FALLBACK
    }

    /** The result */
    private final double      numUniques;

    /**
     * Creates a new instance. Public constructor for testing purposes
     * @param model
     * @param classes
     * @param sampleSize
     * @param accuracy
     * @param maxIterations
     * @param optimized
     */
    public ModelPitman(final ARXPopulationModel model,
                final RiskModelEquivalenceClasses classes,
                final int sampleSize,
                final double accuracy,
                final int maxIterations,
                final PitmanNumericMode mode) {
        this(model, 
             classes, 
             sampleSize, 
             accuracy, 
             maxIterations, 
             new WrappedBoolean(),
             mode);
    }
    /**
     * Creates a new instance
     * @param model
     * @param classes
     * @param sampleSize
     * @param accuracy
     * @param maxIterations
     * @param stop
     */
    ModelPitman(final ARXPopulationModel model,
                final RiskModelEquivalenceClasses classes,
                final int sampleSize,
                final double accuracy,
                final int maxIterations,
                final WrappedBoolean stop) {
        this(model, 
             classes, 
             sampleSize, 
             accuracy, 
             maxIterations, 
             stop,
             PitmanNumericMode.DEFAULT);
    }
    /**
     * Creates a new instance
     * @param model
     * @param classes
     * @param sampleSize
     * @param accuracy
     * @param maxIterations
     * @param stop
     */
    ModelPitman(final ARXPopulationModel model,
                final RiskModelEquivalenceClasses classes,
                final int sampleSize,
                final double accuracy,
                final int maxIterations,
                final WrappedBoolean stop,
                final PitmanNumericMode mode) {
        super(classes, model, sampleSize, stop, new WrappedInteger());

        int c1 = (int) super.getNumClassesOfSize(1);
        int c2 = (int) super.getNumClassesOfSize(2);
        if (c2 == 0) c2 = 1; // Overestimate
        int numClasses = (int) super.getNumClasses();
        double populationSize = super.getPopulationSize();

        // Initial guess
        double c = (c1 * (c1 - 1)) / c2;
        double thetaGuess = ((sampleSize * numClasses * c) - (c1 * (sampleSize - 1) * ((2 * numClasses) + c))) /
                            (((2 * c1 * numClasses) + (c1 * c)) - (sampleSize * c));
        double alphaGuess = ((thetaGuess * (c1 - sampleSize)) + ((sampleSize - 1) * c1)) / (sampleSize * numClasses);

        // Apply Newton-Rhapson algorithm to solve the Maximum Likelihood Estimates
        AlgorithmNewtonPitman pitmanNewton = new AlgorithmNewtonPitman(numClasses, sampleSize, classes.getEquivalenceClasses(), maxIterations, accuracy, stop, mode != PitmanNumericMode.DEFAULT);
        double[] initialGuess = { thetaGuess, alphaGuess };
        double[] output = pitmanNewton.getSolution(initialGuess);
        double result = getResult(output, populationSize);
        
        // Fallback, if requested
        if (mode == PitmanNumericMode.OPTIMIZED_WITH_FALLBACK) {
            double fraction = result / model.getPopulationSize(sampleSize);
            if (Double.isNaN(result) || fraction <= 0d || fraction > 1d) {
                pitmanNewton = new AlgorithmNewtonPitman(numClasses, sampleSize, classes.getEquivalenceClasses(), maxIterations, accuracy, stop, false);
                output = pitmanNewton.getSolution(initialGuess);
                result = getResult(output, populationSize);
            }
        }
        
        this.numUniques = result;
    }
    
    /**
     * Compiles the result of the Newton-Rhapson-Algorithm
     * @param output
     * @return
     */
    private double getResult(double[] output, double populationSize) {
        double theta = output[0];
        double alpha = output[1];
        if (!Double.isNaN(alpha) && !Double.isNaN(theta) && alpha != 0) {
            return Math.exp(Gamma.logGamma(theta + 1) - Gamma.logGamma(theta + alpha)) * Math.pow(populationSize, alpha);
        } else {
            return Double.NaN;
        }
    }

    /**
     * Returns the number of uniques
     * @return
     */
    public double getNumUniques() {
        return this.numUniques;
    }
}
