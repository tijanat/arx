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
package org.deidentifier.arx.aggregates;

import java.io.Serializable;
import java.util.Iterator;

import org.deidentifier.arx.DataType;
import org.deidentifier.arx.DataType.DataTypeWithRatioScale;
import org.deidentifier.arx.framework.check.distribution.Distribution;

/**
 * This abstract class represents an microaggregate function.
 *
 * @author Florian Kohlmayer
 * @param <T>
 */
public abstract class MicroaggregateFunction<T> implements Serializable {
    
    public static class ArithmeticMean<T> extends MicroaggregateFunction<T> {
        
        /** SVUID */
        private static final long serialVersionUID = 8108686651029571643L;
        
        protected ArithmeticMean(DataTypeWithRatioScale<T> type) {
            super(type);
        }
        
        @Override
        public String toString() {
            return "Microaggrate function: ArithmeticMean";
        }
        
        @Override
        protected String aggregateInternal(Iterator<T> iter) {
            // TODO: is this the right way to do it?
            T result = null;
            int count = 0;
            while (iter.hasNext()) {
                T value = iter.next();
                if (!DataType.isNull(type.format(value))) {
                    if (result == null) {
                        result = value;
                    } else {
                        result = type.add(result, value);
                    }
                    count++;
                }
            }
            result = type.divide(result, type.parse(String.valueOf(count)));
            return String.valueOf(result);
        }
        
    }
    
    public static class MicroAggregateFunctionBuilder<T> {
        
        /** The data type */
        private final DataType<T> type;
        
        /**
         * Creates a new instance.
         *
         * @param type
         */
        public MicroAggregateFunctionBuilder(DataType<T> type) {
            this.type = type;
        }
        
        /**
         * An aggregate function that returns a the arithmetic mean, if it can be computed, NULL otherwise.
         *
         * @return
         */
        @SuppressWarnings("unchecked")
        public final MicroaggregateFunction<T> createArithmeticMeanFunction() {
            if (!(type instanceof DataTypeWithRatioScale)) {
                throw new IllegalArgumentException("Can not compute arithmetic mean on data types without ratio scale.");
            }
            return new ArithmeticMean<T>((DataTypeWithRatioScale<T>) type);
        }
    }
    
    protected class DistributionIterator implements Iterator<T> {
        
        private final Distribution              values;
        private final DataTypeWithRatioScale<T> type;
        private final String[]                  dictionary;
        private int                             nextBucket;
        private int                             currentFrequency;
        private T                               currentValue;
        private int                             size;
        
        public DistributionIterator(Distribution values, DataTypeWithRatioScale<T> type, String[] dictionary) {
            this.values = values;
            this.type = type;
            this.dictionary = dictionary;
            this.nextBucket = 0;
            this.currentFrequency = 0;
            this.currentValue = null;
            
            // Calculate number of entries for "hasNext()"
            // TODO there has to be a more efficient way!
            if (values.size() > 0) {
                int[] buckets = values.getBuckets();
                for (int i = 0; i < buckets.length; i += 2) {
                    if (buckets[i] != -1) { // bucket not empty
                        size += buckets[i + 1];
                    }
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return nextBucket < size;
        }
        
        @Override
        public T next() {
            if (currentFrequency == 0) {
                int value = values.getBuckets()[nextBucket];
                currentValue = type.parse(dictionary[value]);
                currentFrequency = values.getBuckets()[nextBucket + 1];
                nextBucket += 2;
            }
            currentFrequency--;
            return currentValue;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    /** SVUID */
    private static final long                 serialVersionUID = 331877806010996154L;
    /** The data type. */
    protected final DataTypeWithRatioScale<T> type;
    
    /** The dictionary */
    protected String[]                        dictionary;
    
    /**
     * Constructor.
     * @param type
     */
    protected MicroaggregateFunction(DataTypeWithRatioScale<T> type) {
        this.type = type;
    }
    
    /**
     * Inits the aggregate function and sets the according dictionary.
     * 
     * @param dictionary
     */
    public void init(String[] dictionary) {
        this.dictionary = dictionary;
    }
    
    /**
     * 
     * This function returns an aggregate value.
     * 
     * @param values
     * @return
     */
    public String aggregate(Distribution values) {
        Iterator<T> it = new DistributionIterator(values, type, dictionary);
        String returnValue = aggregateInternal(it);
        return returnValue.intern();
    }
    
    @Override
    public abstract String toString();
    
    /**
     * This internal function returns an aggregate value for the given distribution.
     * 
     * @param values
     * @return
     */
    protected abstract String aggregateInternal(Iterator<T> values);
}
