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

package org.deidentifier.arx.framework.data;

import java.util.Arrays;

/**
 * A wrapper of an int array.
 *
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class MemoryIntArray implements IMemory {

    /** The data. */
    private int[][] data;

    /**
     * Creates a new Memory where all columns are four bytes wide.
     *
     * @param rows the rows
     * @param columns the columns
     */
    public MemoryIntArray(int rows, int columns) {
        data = new int[rows][];
        for (int i = 0; i < data.length; i++) {
            data[i] = new int[columns];
        }
    }

    /**
     * Instantiates a new memory int array.
     *
     * @param data the data
     */
    private MemoryIntArray(int[][] data) {
        this.data = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory clone() {
        int[][] clone = new int[data.length][];
        for (int i = 0; i < clone.length; i++) {
            clone[i] = Arrays.copyOf(data[i], data[i].length);
        }
        return new MemoryIntArray(clone);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#convert(org.deidentifier.arx.framework.data.IMemory)
     */
    @Override
    public int[][] convert(IMemory memory) {
        return ((MemoryIntArray) memory).data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#convert(int[][])
     */
    @Override
    public IMemory convert(int[][] data) {
        return new MemoryIntArray(data);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equals(org.deidentifier.arx.framework.data.IMemory, int)
     */
    @Override
    public boolean equals(final IMemory other, final int row) {
        return Arrays.equals(((MemoryIntArray) other).data[row], data[row]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equals(int, int)
     */
    @Override
    public boolean equals(final int row1, final int row2) {
        return Arrays.equals(data[row1], data[row2]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equalsIgnoreOutliers(int, int)
     */
    @Override
    public boolean equalsIgnoreOutliers(final int row1, final int row2) {

        int[] r1 = data[row1];
        int[] r2 = data[row2];

        for (int i = 0; i < r1.length; i++) {
            if (r1[i] != (r2[i] & Data.REMOVE_OUTLIER_MASK)) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#free()
     */
    @Override
    public void free() {
        data = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#get(int, int)
     */
    @Override
    public int get(final int row, final int col) {
        return data[row][col];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#getByteSize()
     */
    @Override
    public long getByteSize() {
        throw new RuntimeException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#getNumColumns()
     */
    @Override
    public int getNumColumns() {
        return data[0].length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#getNumRows()
     */
    @Override
    public int getNumRows() {
        return data.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#hashCode(int)
     */
    @Override
    public int hashCode(final int row) {
        return Arrays.hashCode(data[row]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#set(int, int, int)
     */
    @Override
    public void set(final int row, final int col, final int val) {
        data[row][col] = val;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#swap(int, int)
     */
    @Override
    public void swap(int row1, int row2) {
        int[] temp = data[row2];
        data[row2] = data[row1];
        data[row1] = temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (data != null) {
            free();
        }
    }

}
