/*
 * ARX: Efficient, Stable and Optimal Data Anonymization
 * Copyright (C) 2012 - 2013 Florian Kohlmayer, Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.deidentifier.arx.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.deidentifier.arx.Data;
import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.ARXConfiguration.LDiversityCriterion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test for data transformations
 * 
 * @author Prasser, Kohlmayer
 */
@RunWith(Parameterized.class)
public class TestDataTransformationsFromFileLDiversity extends
        TestDataTransformationsFromFileAbstract {

    @Parameters
    public static Collection<Object[]> cases() {
        return Arrays.asList(new Object[][] {

                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "occupation",
                                     0.04d,
                                     "data/adult.csv",
                                     Metric.NMENTROPY,
                                     228878.2039109519d,
                                     new int[] { 1, 0, 1, 1, 2, 2, 2, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "occupation",
                                     0.04d,
                                     "data/adult.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "occupation",
                                     0.0d,
                                     "data/adult.csv",
                                     Metric.NMENTROPY,
                                     324620.5269918695d,
                                     new int[] { 1, 1, 1, 1, 3, 2, 2, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "occupation",
                                     0.0d,
                                     "data/adult.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "occupation",
                                     0.04d,
                                     "data/adult.csv",
                                     Metric.NMENTROPY,
                                     228878.2039109519d,
                                     new int[] { 1, 0, 1, 1, 2, 2, 2, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "occupation",
                                     0.04d,
                                     "data/adult.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.DMSTAR,
                                     9.123049999E9d,
                                     new int[] { 0, 3, 0, 0, 2, 0, 1, 0 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.DMSTAR,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.NMENTROPY,
                                     3536911.5162082445d,
                                     new int[] { 0, 4, 0, 0, 2, 0, 1, 2 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "Highest level of school completed",
                                     0.0d,
                                     "data/atus.csv",
                                     Metric.DMSTAR,
                                     2.0146560117E10d,
                                     new int[] { 0, 3, 0, 2, 2, 2, 2, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "Highest level of school completed",
                                     0.0d,
                                     "data/atus.csv",
                                     Metric.DMSTAR,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "Highest level of school completed",
                                     0.0d,
                                     "data/atus.csv",
                                     Metric.NMENTROPY,
                                     4912828.240033204d,
                                     new int[] { 0, 4, 0, 2, 2, 2, 2, 2 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "Highest level of school completed",
                                     0.0d,
                                     "data/atus.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.DMSTAR,
                                     9.123049999E9d,
                                     new int[] { 0, 3, 0, 0, 2, 0, 1, 0 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.DMSTAR,
                                     0.0d,
                                     null,
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.NMENTROPY,
                                     3536911.5162082445d,
                                     new int[] { 0, 4, 0, 0, 2, 0, 1, 2 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "Highest level of school completed",
                                     0.04d,
                                     "data/atus.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.DMSTAR,
                                     2823649.0d,
                                     new int[] { 4, 0, 0, 1, 1, 3, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.DMSTAR,
                                     8.8290083E7d,
                                     new int[] { 3, 4, 1, 2, 1, 2, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.ENTROPY,
                                     1244350.703466948d,
                                     new int[] { 2, 4, 0, 1, 0, 3, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.ENTROPY,
                                     1552136.4930241709d,
                                     new int[] { 3, 4, 1, 2, 0, 2, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "RAMNTALL",
                                     0.0d,
                                     "data/cup.csv",
                                     Metric.DMSTAR,
                                     3.01506905E8d,
                                     new int[] { 4, 4, 1, 1, 1, 4, 4 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "RAMNTALL",
                                     0.0d,
                                     "data/cup.csv",
                                     Metric.DMSTAR,
                                     6.19637215E8d,
                                     new int[] { 5, 4, 1, 0, 1, 4, 4 },
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "RAMNTALL",
                                     0.0d,
                                     "data/cup.csv",
                                     Metric.ENTROPY,
                                     1961244.482255955d,
                                     new int[] { 4, 4, 1, 1, 1, 4, 4 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "RAMNTALL",
                                     0.0d,
                                     "data/cup.csv",
                                     Metric.ENTROPY,
                                     2032837.6390798881d,
                                     new int[] { 5, 4, 1, 0, 1, 4, 4 },
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.DMSTAR,
                                     2823649.0d,
                                     new int[] { 4, 0, 0, 1, 1, 3, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.DMSTAR,
                                     8.8290083E7d,
                                     new int[] { 3, 4, 1, 2, 1, 2, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.ENTROPY,
                                     1244350.703466948d,
                                     new int[] { 2, 4, 0, 1, 0, 3, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "RAMNTALL",
                                     0.04d,
                                     "data/cup.csv",
                                     Metric.ENTROPY,
                                     1552136.4930241709d,
                                     new int[] { 3, 4, 1, 2, 0, 2, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.ENTROPY,
                                     590389.0228300439d,
                                     new int[] { 0, 2, 1, 1, 0, 2, 0 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.ENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.NMENTROPY,
                                     629604.8933555635d,
                                     new int[] { 0, 2, 1, 1, 0, 2, 0 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "istatenum",
                                     0.0d,
                                     "data/fars.csv",
                                     Metric.ENTROPY,
                                     1201007.0880104564d,
                                     new int[] { 0, 2, 3, 3, 1, 2, 2 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "istatenum",
                                     0.0d,
                                     "data/fars.csv",
                                     Metric.ENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "istatenum",
                                     0.0d,
                                     "data/fars.csv",
                                     Metric.NMENTROPY,
                                     1201007.0880104564d,
                                     new int[] { 0, 2, 3, 3, 1, 2, 2 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "istatenum",
                                     0.0d,
                                     "data/fars.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.ENTROPY,
                                     590389.0228300439d,
                                     new int[] { 0, 2, 1, 1, 0, 2, 0 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.ENTROPY,
                                     0.0d,
                                     null,
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.DISTINCT,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.NMENTROPY,
                                     629604.8933555635d,
                                     new int[] { 0, 2, 1, 1, 0, 2, 0 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.DISTINCT,
                                     "istatenum",
                                     0.04d,
                                     "data/fars.csv",
                                     Metric.NMENTROPY,
                                     0.0d,
                                     null,
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.DMSTAR,
                                     3.53744964E8d,
                                     new int[] { 0, 0, 0, 1, 3, 0, 0, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.DMSTAR,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.ENTROPY,
                                     8730993.835884217d,
                                     new int[] { 0, 0, 0, 2, 3, 0, 0, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.ENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "EDUC",
                                     0.0d,
                                     "data/ihis.csv",
                                     Metric.DMSTAR,
                                     9.85795222E8d,
                                     new int[] { 0, 0, 0, 3, 3, 2, 0, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "EDUC",
                                     0.0d,
                                     "data/ihis.csv",
                                     Metric.DMSTAR,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "EDUC",
                                     0.0d,
                                     "data/ihis.csv",
                                     Metric.ENTROPY,
                                     1.2258628558792587E7d,
                                     new int[] { 0, 0, 0, 3, 3, 2, 0, 1 },
                                     false) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "EDUC",
                                     0.0d,
                                     "data/ihis.csv",
                                     Metric.ENTROPY,
                                     0.0d,
                                     null,
                                     false) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.RECURSIVE,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.DMSTAR,
                                     3.53744964E8d,
                                     new int[] { 0, 0, 0, 1, 3, 0, 0, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.RECURSIVE,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.DMSTAR,
                                     0.0d,
                                     null,
                                     true) },
                { new TestCaseResult(4.0,
                                     5,
                                     LDiversityCriterion.ENTROPY,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.ENTROPY,
                                     8730993.835884217d,
                                     new int[] { 0, 0, 0, 2, 3, 0, 0, 1 },
                                     true) },
                { new TestCaseResult(4.0,
                                     100,
                                     LDiversityCriterion.ENTROPY,
                                     "EDUC",
                                     0.04d,
                                     "data/ihis.csv",
                                     Metric.ENTROPY,
                                     0.0d,
                                     null,
                                     true) },

        });
    }

    public TestDataTransformationsFromFileLDiversity(final TestCaseResult testCase) {
        super(testCase);
    }

    // OLD:
    // { new TestCaseResult(4, 5, "occupation", 0.04d, "data/adult",
    // Metric.DMSTAR, 5759600.0d, new int[] { 0, 0, 1, 2, 2, 2, 2, 1 }) },
    // { new TestCaseResult(4, 5, "occupation", 0.04d, "data/adult",
    // Metric.ENTROPY, 206321.8355062328d, new int[] { 1, 0, 1, 0, 3, 2, 2, 0 })
    // },

    @Override
    @Test
    public void testTestCases() throws IOException {

        final Data data = createDataObject(testCase);
        final org.deidentifier.arx.metric.Metric<?> metric = createMetric(testCase);

        // Create an instance of the anonymizer
        final ARXAnonymizer anonymizer = new ARXAnonymizer(metric);
        anonymizer.setPracticalMonotonicity(testCase.practical);

        ARXResult result = null;

        // Execute the algorithm
        switch (testCase.lDiversityCriterion) {
        case DISTINCT:
            result = anonymizer.lDiversify(data,
                                           testCase.l,
                                           false,
                                           testCase.relativeMaxOutliers);
            break;

        case ENTROPY:
            result = anonymizer.lDiversify(data,
                                           testCase.l,
                                           true,
                                           testCase.relativeMaxOutliers);
            break;

        case RECURSIVE:
            result = anonymizer.lDiversify(data,
                                           testCase.c,
                                           testCase.l,
                                           testCase.relativeMaxOutliers);
            break;

        }

        // check if no solution possible
        if (testCase.bestResult == null) {
            assertTrue(result.getGlobalOptimum() == null);
        } else {

            assertEquals(testCase.dataset +
                                 "-should: " +
                                 testCase.optimalInformationLoss +
                                 "is: " +
                                 result.getGlobalOptimum()
                                       .getMinimumInformationLoss()
                                       .getValue(),
                         result.getGlobalOptimum()
                               .getMinimumInformationLoss()
                               .getValue(),
                         testCase.optimalInformationLoss);
            assertTrue(testCase.dataset +
                               "-should: " +
                               Arrays.toString(testCase.bestResult) +
                               "is: " +
                               Arrays.toString(result.getGlobalOptimum()
                                                     .getTransformation()),
                       Arrays.equals(result.getGlobalOptimum()
                                           .getTransformation(),
                                     testCase.bestResult));
        }

    }
}