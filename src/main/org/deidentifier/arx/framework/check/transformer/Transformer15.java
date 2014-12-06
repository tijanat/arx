/*
 * ARX: Powerful Data Anonymization
 * Copyright (C) 2012 - 2014 Florian Kohlmayer, Fabian Prasser
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

package org.deidentifier.arx.framework.check.transformer;

import org.deidentifier.arx.ARXConfiguration.ARXConfigurationInternal;
import org.deidentifier.arx.framework.check.distribution.IntArrayDictionary;
import org.deidentifier.arx.framework.data.GeneralizationHierarchy;
import org.deidentifier.arx.framework.data.IMemory;

/**
 * The class Transformer15.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class Transformer15 extends AbstractTransformer {

    /**
     * Instantiates a new transformer.
     *
     * @param data the data
     * @param hierarchies the hierarchies
     * @param sensitiveValues
     * @param dictionarySensValue
     * @param dictionarySensFreq
     * @param config
     */
    public Transformer15(final IMemory data,
                         final GeneralizationHierarchy[] hierarchies,
                         final IMemory sensitiveValues,
                         final IntArrayDictionary dictionarySensValue,
                         final IntArrayDictionary dictionarySensFreq,
                         final ARXConfigurationInternal config) {
        super(data, hierarchies, sensitiveValues, dictionarySensValue, dictionarySensFreq, config);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.ARX.framework.check.transformer.AbstractTransformer
     * #walkAll()
     */
    @Override
    protected void processAll() {
        for (int i = startIndex; i < stopIndex; i++) {
            // Transform
            buffer.set(i, outindex0, idindex0[data.get(i, index0)][generalizationindex0]);
            buffer.set(i, outindex1, idindex1[data.get(i, index1)][generalizationindex1]);
            buffer.set(i, outindex2, idindex2[data.get(i, index2)][generalizationindex2]);
            buffer.set(i, outindex3, idindex3[data.get(i, index3)][generalizationindex3]);
            buffer.set(i, outindex4, idindex4[data.get(i, index4)][generalizationindex4]);
            buffer.set(i, outindex5, idindex5[data.get(i, index5)][generalizationindex5]);
            buffer.set(i, outindex6, idindex6[data.get(i, index6)][generalizationindex6]);
            buffer.set(i, outindex7, idindex7[data.get(i, index7)][generalizationindex7]);
            buffer.set(i, outindex8, idindex8[data.get(i, index8)][generalizationindex8]);
            buffer.set(i, outindex9, idindex9[data.get(i, index9)][generalizationindex9]);
            buffer.set(i, outindex10, idindex10[data.get(i, index10)][generalizationindex10]);
            buffer.set(i, outindex11, idindex11[data.get(i, index11)][generalizationindex11]);
            buffer.set(i, outindex12, idindex12[data.get(i, index12)][generalizationindex12]);
            buffer.set(i, outindex13, idindex13[data.get(i, index13)][generalizationindex13]);
            buffer.set(i, outindex14, idindex14[data.get(i, index14)][generalizationindex14]);

            // Call
            delegate.callAll(i);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.ARX.framework.check.transformer.AbstractTransformer
     * #walkGroupify ()
     */
    @Override
    protected void processGroupify() {
        int processed = 0;
        while (element != null) {
            // Transform
            buffer.set(element.representant, outindex0, idindex0[data.get(element.representant, index0)][generalizationindex0]);
            buffer.set(element.representant, outindex1, idindex1[data.get(element.representant, index1)][generalizationindex1]);
            buffer.set(element.representant, outindex2, idindex2[data.get(element.representant, index2)][generalizationindex2]);
            buffer.set(element.representant, outindex3, idindex3[data.get(element.representant, index3)][generalizationindex3]);
            buffer.set(element.representant, outindex4, idindex4[data.get(element.representant, index4)][generalizationindex4]);
            buffer.set(element.representant, outindex5, idindex5[data.get(element.representant, index5)][generalizationindex5]);
            buffer.set(element.representant, outindex6, idindex6[data.get(element.representant, index6)][generalizationindex6]);
            buffer.set(element.representant, outindex7, idindex7[data.get(element.representant, index7)][generalizationindex7]);
            buffer.set(element.representant, outindex8, idindex8[data.get(element.representant, index8)][generalizationindex8]);
            buffer.set(element.representant, outindex9, idindex9[data.get(element.representant, index9)][generalizationindex9]);
            buffer.set(element.representant, outindex10, idindex10[data.get(element.representant, index10)][generalizationindex10]);
            buffer.set(element.representant, outindex11, idindex11[data.get(element.representant, index11)][generalizationindex11]);
            buffer.set(element.representant, outindex12, idindex12[data.get(element.representant, index12)][generalizationindex12]);
            buffer.set(element.representant, outindex13, idindex13[data.get(element.representant, index13)][generalizationindex13]);
            buffer.set(element.representant, outindex14, idindex14[data.get(element.representant, index14)][generalizationindex14]);

            // Call
            delegate.callGroupify(element);

            // Next element
            processed++;
            if (processed == numElements) {
                return;
            }
            element = element.nextOrdered;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.ARX.framework.check.transformer.AbstractTransformer
     * #walkSnapshot ()
     */
    @Override
    protected void processSnapshot() {
        startIndex *= ssStepWidth;
        stopIndex *= ssStepWidth;

        for (int i = startIndex; i < stopIndex; i += ssStepWidth) {
            int row = snapshot[i];

            // Transform
            buffer.set(row, outindex0, idindex0[data.get(row, index0)][generalizationindex0]);
            buffer.set(row, outindex1, idindex1[data.get(row, index1)][generalizationindex1]);
            buffer.set(row, outindex2, idindex2[data.get(row, index2)][generalizationindex2]);
            buffer.set(row, outindex3, idindex3[data.get(row, index3)][generalizationindex3]);
            buffer.set(row, outindex4, idindex4[data.get(row, index4)][generalizationindex4]);
            buffer.set(row, outindex5, idindex5[data.get(row, index5)][generalizationindex5]);
            buffer.set(row, outindex6, idindex6[data.get(row, index6)][generalizationindex6]);
            buffer.set(row, outindex7, idindex7[data.get(row, index7)][generalizationindex7]);
            buffer.set(row, outindex8, idindex8[data.get(row, index8)][generalizationindex8]);
            buffer.set(row, outindex9, idindex9[data.get(row, index9)][generalizationindex9]);
            buffer.set(row, outindex10, idindex10[data.get(row, index10)][generalizationindex10]);
            buffer.set(row, outindex11, idindex11[data.get(row, index11)][generalizationindex11]);
            buffer.set(row, outindex12, idindex12[data.get(row, index12)][generalizationindex12]);
            buffer.set(row, outindex13, idindex13[data.get(row, index13)][generalizationindex13]);
            buffer.set(row, outindex14, idindex14[data.get(row, index14)][generalizationindex14]);

            // Call
            delegate.callSnapshot(snapshot, i);
        }
    }
}
