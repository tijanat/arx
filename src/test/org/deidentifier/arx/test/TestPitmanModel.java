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

package org.deidentifier.arx.test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.deidentifier.arx.risk.ModelPitman;
import org.deidentifier.arx.risk.ModelPitman.PitmanNumericMode;
import org.deidentifier.arx.risk.RiskModelEquivalenceClasses;
import org.junit.Test;

/**
 * Test for risk metrics.
 *
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class TestPitmanModel extends TestCase {

    /**
     * Returns the data object for a given dataset
     *
     * @param dataset the dataset
     * @return the data object
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static Data getDataObject(final String dataset) throws IOException {

        final Data data = Data.create(dataset, ';');

        // Read generalization hierachies
        final FilenameFilter hierarchyFilter = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                if (name.matches(dataset.substring(dataset.lastIndexOf("/") + 1, dataset.length() - 4) + "_hierarchy_(.)+.csv")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        final File testDir = new File(dataset.substring(0, dataset.lastIndexOf("/")));
        final File[] genHierFiles = testDir.listFiles(hierarchyFilter);
        final Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");

        for (final File file : genHierFiles) {
            final Matcher matcher = pattern.matcher(file.getName());
            if (matcher.find()) {

                final CSVHierarchyInput hier = new CSVHierarchyInput(file, ';');
                final String attributeName = matcher.group(1);

                // use all found attribute hierarchies as qis
                data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));

            }
        }

        return data;
    }

    /**
     * Returns the power set
     * @param originalSet
     * @return
     */
    private <T> Set<Set<T>> getPowerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
        for (Set<T> set : getPowerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }       
        return sets;
    }
    
    /**
     * Test pitman model using the adult dataset.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testPitmanModel() throws IOException {
        
        Data data = getDataObject("../arx-data/data-junit/adult.csv");
        
        Set<String> qis = data.getDefinition().getQuasiIdentifyingAttributes();
        Set<Set<String>> power = getPowerSet(qis);
        
        long nano1 = 0;
        long nano2 = 0;
        long nano3 = 0;
        
        double[] samplingFractions = new double[]{0.001, 0.01, 0.1};
        for (double samplingFraction : samplingFractions) {
            System.out.println("Sampling fraction: " + samplingFraction);
            for (Set<String> set : power) {
                if (set.isEmpty()) {
                    continue;
                }
                
                ARXPopulationModel populationModel = ARXPopulationModel.create(samplingFraction);
                RiskModelEquivalenceClasses classes =  data.getHandle().getRiskEstimator(populationModel, set).getEquivalenceClassModel();
                
                if (classes.getEquivalenceClasses()[0] == 1) {
                    
                    long time = System.nanoTime();
                    
                    ModelPitman model = new ModelPitman(populationModel, classes, data.getHandle().getNumRows(), 1e-6, 1000, PitmanNumericMode.DEFAULT);
                    double populationUniquenessDefault = model.getNumUniques() / populationModel.getPopulationSize(data.getHandle());
                    
                    nano1 += (System.nanoTime() - time);
                    time = System.nanoTime();
                    
                    model = new ModelPitman(populationModel, classes, data.getHandle().getNumRows(), 1e-6, 1000, PitmanNumericMode.OPTIMIZED);
                    double populationUniquenessOptimized = model.getNumUniques() / populationModel.getPopulationSize(data.getHandle());
                    
                    nano2 += (System.nanoTime() - time);
                    time = System.nanoTime();
                    
                    model = new ModelPitman(populationModel, classes, data.getHandle().getNumRows(), 1e-6, 1000, PitmanNumericMode.OPTIMIZED_WITH_FALLBACK);
                    double populationUniquenessOptimizedWithFallback = model.getNumUniques() / populationModel.getPopulationSize(data.getHandle());
                    
                    nano3 += (System.nanoTime() - time);
                    
                    print(populationUniquenessDefault,
                          populationUniquenessOptimized,
                          populationUniquenessOptimizedWithFallback);
                } else {
                    System.out.println("  No sample uniques. Skipping.");
                }

            }
        }
        
        double time1 = nano1 / 10e9;
        double time2 = nano2 / 10e9;
        double time3 = nano3 / 10e9;
        System.out.println("Default              : " + time1);
        System.out.println("Optimized            : " + time2);
        System.out.println("OptimizedWithFallback: " + time3);
    }
    
    private void print(double d1, double d2, double d3) {
        System.out.format("%10f%10f%10f", d1, d2, d3);
        if (!equals(d1, d2) || !equals(d2, d3)) {
            System.out.println(" [DIFFERENCE]");
        } else {
            System.out.println("");
        }
        
    }
    
    private boolean equals(double d1, double d2) {
        return Math.round(d1 * 1000000d) == Math.round(d2 * 1000000d);
    }
}
