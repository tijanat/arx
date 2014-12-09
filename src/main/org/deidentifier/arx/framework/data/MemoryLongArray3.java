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
public class MemoryLongArray3 implements IMemory {

    /** The data. */
    private long[]    data;

    /** The number of rows. */
    private final int numRows;

    /** The number of columns. */
    private final int numColumns;

    /** Longs per row. */
    private final int longsPerRow;

    /**
     * Creates a new Memory where all columns are four bytes wide.
     *
     * @param rows the rows
     * @param columns the columns
     */
    public MemoryLongArray3(int rows, int columns) {
        longsPerRow = (int) (Math.ceil(columns / 2d));
        data = new long[longsPerRow * rows];
        numColumns = columns;
        numRows = rows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory clone() {
        MemoryLongArray3 m = new MemoryLongArray3(numRows, numColumns);
        m.data = Arrays.copyOf(data, data.length);
        return m;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equals(int, int)
     */
    @Override
    public boolean equals(final int row1, final int row2) {
        final int idx1 = row1 * longsPerRow;
        final int idx2 = row2 * longsPerRow;

        switch (longsPerRow) {
        case 6:
            if (data[idx1 + 5] != data[idx2 + 5]) {
                return false;
            }
        case 5:
            if (data[idx1 + 4] != data[idx2 + 4]) {
                return false;
            }
        case 4:
            if (data[idx1 + 3] != data[idx2 + 3]) {
                return false;
            }
        case 3:
            if (data[idx1 + 2] != data[idx2 + 2]) {
                return false;
            }
        case 2:
            if (data[idx1 + 1] != data[idx2 + 1]) {
                return false;
            }
        case 1:
            if (data[idx1] != data[idx2]) {
                return false;
            }
            break;
        default:
            // final int end = idx1 + longsPerRow;
            // final int offset = idx1 - idx2;
            // for (int cIDX = idx1; cIDX < end; cIDX++) {
            // if (data[cIDX] != data[cIDX + offset]) {
            // return false;
            // }
            // }
            // return true;
            throw new RuntimeException("Invalid bytes per row!");
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equalsIgnoreOutliers(int, int)
     */
    @Override
    public boolean equalsIgnoreOutliers(final int row1, final int row2) {
        final int idx1 = row1 * longsPerRow;
        int idx2 = row2 * longsPerRow;

        switch (longsPerRow) {
        case 6:
            if ((data[idx1 + 5]) != (data[idx2 + 5])) {
                return false;
            }
        case 5:
            if ((data[idx1 + 4]) != (data[idx2 + 4])) {
                return false;
            }
        case 4:
            if ((data[idx1 + 3]) != (data[idx2 + 3])) {
                return false;
            }
        case 3:
            if ((data[idx1 + 2]) != (data[idx2 + 2])) {
                return false;
            }
        case 2:
            if ((data[idx1 + 1]) != (data[idx2 + 1])) {
                return false;
            }
        case 1:
            if ((data[idx1] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                return false;
            }
            break;
        default:
            // final int end = idx1 + longsPerRow;
            // for (int cIDX = idx1; cIDX < end; cIDX++) {
            // if ((data[cIDX] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2++] & Data.REMOVE_OUTLIER_LONG_MASK)) {
            // return false;
            // }
            // }
            // return true;
            throw new RuntimeException("Invalid bytes per row!");
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
        return (int) (data[(row * longsPerRow) + (col >> 1)] >>> (32 - ((col & 1) << 5)));
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
        return numColumns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#getNumRows()
     */
    @Override
    public int getNumRows() {
        return numRows;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#hashCode(int)
     */
    @Override
    public int hashCode(final int row) {
        long temp = 1125899906842597L;
        final int idx = row * longsPerRow;

        switch (longsPerRow) {
        case 6:
            temp = (31 * temp) + data[idx + 5];
        case 5:
            temp = (31 * temp) + data[idx + 4];
        case 4:
            temp = (31 * temp) + data[idx + 3];
        case 3:
            temp = (31 * temp) + data[idx + 2];
        case 2:
            temp = (31 * temp) + data[idx + 1];
        case 1:
            temp = (31 * temp) + data[idx];
            break;
        default:
            // TODO
            throw new RuntimeException("Invalid bytes per row!");
        }
        return (int) (31 * temp) * (int) (temp >>> 32);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory newInstance() {
        return new MemoryLongArray3(numRows, numColumns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#set(int, int, int)
     */
    @Override
    public void set(final int row, final int col, final int val) {

        final int idx = (row * longsPerRow) + (col >> 1);
        final int shift = 32 - ((col & 1) << 5);
        final long value = (long) val << shift;
        final long mask = ~(0x00000000FFFFFFFFL << shift);
        data[idx] = (data[idx] & mask) | value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#swap(int, int)
     */
    @Override
    public void swap(int row1, int row2) {

        final int idx1 = row1 * longsPerRow;
        final int idx2 = row2 * longsPerRow;
        long[] temp = new long[longsPerRow];

        // save in temp
        System.arraycopy(data, idx1, temp, 0, longsPerRow);

        // row2 <-- row1
        System.arraycopy(data, idx1, data, idx2, longsPerRow);

        // row1 <-- temp
        System.arraycopy(temp, 0, data, idx1, longsPerRow);
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
