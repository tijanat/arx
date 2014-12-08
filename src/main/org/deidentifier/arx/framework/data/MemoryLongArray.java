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

/**
 * A wrapper of an int array.
 *
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class MemoryLongArray implements IMemory {

    /** The data. */
    private long[]           data;

    /** Flag to indicate, if efficient equals and compare can be used. */
    private final boolean    use_fast_compare;

    /** The length of the slide for equals and compare in longs. */
    private static final int LENGTH_OF_SLIDE_IN_LONGS = 6;

    /** The number of rows. */
    private final int        numRows;

    /** The number of columns. */
    private final int        numColumns;

    /** The number of longs used for one row. */
    private final int        longsPerRow;

    /** The offset to the long containing the column */
    private final int[]      offsets;

    /** The shifts per column */
    private final byte[]     shifts;

    /** The setMasks */
    private final long[]     setMasks;

    /**
     * Creates a new Memory where all columns are four bytes wide.
     *
     * @param rows the rows
     * @param columns the columns
     */
    public MemoryLongArray(int rows, int columns) {
        this.longsPerRow = (int) (Math.ceil((double) columns / 2d));

        if (longsPerRow > LENGTH_OF_SLIDE_IN_LONGS) {
            use_fast_compare = false;
        } else {
            use_fast_compare = true;
        }

        this.data = new long[longsPerRow * rows];
        this.numColumns = columns;
        this.numRows = rows;

        offsets = new int[numColumns];
        shifts = new byte[numColumns];
        setMasks = new long[numColumns];
        for (int i = 0; i < numColumns; i++) {
            offsets[i] = (int) ((double) i / 2d);
            if ((i & 1) == 0) { // even
                shifts[i] = (byte) 0;
                setMasks[i] = 0xFFFFFFFF00000000L;
            } else {
                shifts[i] = (byte) 32;
                setMasks[i] = 0x00000000FFFFFFFFL;
            }
        }

    }

    private MemoryLongArray(long[] clone, int rows, int columns) {
        longsPerRow = (int) (Math.ceil((double) columns / 2d));

        if (longsPerRow > LENGTH_OF_SLIDE_IN_LONGS) {
            use_fast_compare = false;
        } else {
            use_fast_compare = true;
        }

        this.data = clone;
        this.numColumns = columns;
        this.numRows = rows;

        offsets = new int[numColumns];
        shifts = new byte[numColumns];
        setMasks = new long[numColumns];
        for (int i = 0; i < numColumns; i++) {
            offsets[i] = (int) ((double) i / 2d);
            if ((i & 1) == 0) { // even
                shifts[i] = (byte) 0;
                setMasks[i] = 0xFFFFFFFF00000000L;
            } else {
                shifts[i] = (byte) 32;
                setMasks[i] = 0x00000000FFFFFFFFL;
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory clone() {
        long[] clone = new long[data.length];
        System.arraycopy(data, 0, clone, 0, data.length);
        return new MemoryLongArray(clone, numRows, numColumns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#convert(org.deidentifier.arx.framework.data.IMemory)
     */
    @Override
    public int[][] convert(IMemory memory) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#convert(int[][])
     */
    @Override
    public IMemory convert(int[][] data) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equals(org.deidentifier.arx.framework.data.IMemory, int)
     */
    @Override
    public boolean equals(final IMemory other, final int row) {
        final long[] o = ((MemoryLongArray) other).data;
        final int idx = row * longsPerRow;

        if (use_fast_compare) {
            switch (longsPerRow) {
            case 6:
                if (data[idx + 5] != o[idx + 5]) {
                    return false;
                }
            case 5:
                if (data[idx + 4] != o[idx + 4]) {
                    return false;
                }
            case 4:
                if (data[idx + 3] != o[idx + 3]) {
                    return false;
                }
            case 3:
                if (data[idx + 2] != o[idx + 2]) {
                    return false;
                }
            case 2:
                if (data[idx + 1] != o[idx + 1]) {
                    return false;
                }
            case 1:
                if (data[idx] != o[idx]) {
                    return false;
                }
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }
            return true;
        } else {
            final int end = idx + longsPerRow;
            for (int cIDX = idx; cIDX < end; cIDX++) {
                if (data[cIDX] != o[cIDX]) {
                    return false;
                }
            }
            return true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equals(int, int)
     */
    @Override
    public boolean equals(final int row1, final int row2) {
        final int idx1 = row1 * longsPerRow;
        int idx2 = row2 * longsPerRow;

        if (use_fast_compare) {
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
                throw new RuntimeException("Invalid bytes per row!");
            }
            return true;
        } else {
            final int end = idx1 + longsPerRow;
            for (int cIDX = idx1; cIDX < end; cIDX++) {
                if (data[cIDX] != data[idx2++]) {
                    return false;
                }
            }
            return true;
        }
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

        if (use_fast_compare) {
            switch (longsPerRow) {
            case 6:
                if ((data[idx1 + 5] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2 + 5] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 5:
                if ((data[idx1 + 4] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2 + 4] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 4:
                if ((data[idx1 + 3] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2 + 3] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 3:
                if ((data[idx1 + 2] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2 + 2] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 2:
                if ((data[idx1 + 1] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2 + 1] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 1:
                if ((data[idx1] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }
            return true;
        } else {
            final int end = idx1 + longsPerRow;
            for (int cIDX = idx1; cIDX < end; cIDX++) {
                if ((data[cIDX] & Data.REMOVE_OUTLIER_LONG_MASK) != (data[idx2++] & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            }
            return true;
        }

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
        final int idx = (row * longsPerRow) + offsets[col];
        final int value = (int) (data[idx] >>> shifts[col]);
        return value;
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
        final int cIDX = row * longsPerRow;
        long temp = 1125899906842597L;

        if (use_fast_compare) {
            switch (longsPerRow) {
            case 6:
                temp = (31 * temp) + data[cIDX + 5];
            case 5:
                temp = (31 * temp) + data[cIDX + 4];
            case 4:
                temp = (31 * temp) + data[cIDX + 3];
            case 3:
                temp = (31 * temp) + data[cIDX + 2];
            case 2:
                temp = (31 * temp) + data[cIDX + 1];
            case 1:
                temp = (31 * temp) + data[cIDX];
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }

        } else {
            final int end = cIDX + longsPerRow;
            for (int address = cIDX; address < end; address++) {
                temp = (31 * temp) + data[address];
            }
        }
        return (int) (31 * temp) * (int) (temp >>> 32);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#set(int, int, int)
     */
    @Override
    public void set(final int row, final int col, final int val) {
        final int idx = (row * longsPerRow) + offsets[col];
        // clear previous bits
        data[idx] &= setMasks[col];
        // set new bits
        data[idx] |= (((long) val) << shifts[col]);
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
