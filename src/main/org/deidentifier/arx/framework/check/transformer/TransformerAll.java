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
 * The class TransformerAll.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class TransformerAll extends AbstractTransformer {

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
    public TransformerAll(final IMemory data,
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
        final int dimensions = data.getNumColumns();
        for (int i = startIndex; i < stopIndex; i++) {
            for (int d = 0; d < dimensions; d++) {
                final int state = generalization[d];
                buffer.set(i, d, map[d][data.get(i, d)][state]);
            }

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

            for (int d = 0; d < dimensions; d++) {
                final int state = generalization[d];
                buffer.set(element.representant, d, map[d][data.get(element.representant, d)][state]);
            }

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
            for (int d = 0; d < dimensions; d++) {
                final int state = generalization[d];
                buffer.set(row, d, map[d][data.get(row, d)][state]);
            }

            // Call
            delegate.callSnapshot(snapshot, i);
        }
    }
}
