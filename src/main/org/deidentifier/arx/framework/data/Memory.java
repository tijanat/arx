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

import java.lang.reflect.Field;
import java.util.Arrays;

import sun.misc.Unsafe;

/**
 * A fast implementation of a two dimensional integer array based
 * on unsafe memory accesses, which implements specialized methods.
 * 
 * Fields are either 1 (byte), 2 (short) or 4 (int) bytes in size.
 * Each row is 8 byte (long) aligned, to allow for fast equals() and hashcode().
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class Memory implements IMemory {

    /**
     * Creates an byte array of length columns and fills it with 4.
     *
     * @param columns the columns
     * @return the byte[]
     */
    private static byte[] createIntFieldSizes(int columns) {
        final byte[] fieldSizes = new byte[columns];
        Arrays.fill(fieldSizes, (byte) 31);
        return fieldSizes;
    }

    /** The Constant POWER. */
    private static final int POWER                    = 1;

    /** The Constant NUM_LONGS. */
    private static final int NUM_LONGS                = 1 << POWER;

    /** The length of the slide for equals and compare in longs. */
    private static final int LENGTH_OF_SLIDE_IN_LONGS = 6;

    /** The base address of the memory field in bytes. */
    private final long       baseAddress;

    /** The offset position of the fields. Aligned in long array for efficient caching */
    private final long[]     bytePos;

    /** The size in bytes of one row. */
    private final long       rowSizeInBytes;

    /** The size in longs of one row. */
    private final int        rowSizeInLongs;

    /** The total size of the allocated memory. */
    private final long       size;

    /** The unsafe. */
    private final Unsafe     unsafe;

    /** Flag to indicate, if efficient equals and compare can be used. */
    private final boolean    use_fast_compare;

    /** Flag to indicate if the allocated memory has been freed. */
    private boolean          freed;

    /** The number of rows. */
    private final int        numRows;

    /** The number of columns. */
    private final int        numColumns;

    /** The sizes of the columns in bytes. */
    private final byte[]     columnSizes;

    /**
     * Instantiates a new memory.
     *
     * @param numRows the num rows
     * @param columnSizes the column sizes
     */
    public Memory(final int numRows, final byte[] columnSizes) {

        this.columnSizes = columnSizes;
        numColumns = columnSizes.length;
        this.numRows = numRows;

        // Access unsafe
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (
                NoSuchFieldException
                | SecurityException
                | IllegalAccessException
                | IllegalArgumentException e) {
            throw new RuntimeException("Error accessing unsafe memory!", e);
        }

        // Field properties
        bytePos = new long[columnSizes.length * NUM_LONGS];

        int currentlyUsedBytes = 0;
        int curLong = 1;
        int idx = 0;
        for (int field = 0; field < columnSizes.length; field++) {
            final int currFieldSizeInBits = columnSizes[field];

            // bitpos [0] --> fieldsize
            if (currFieldSizeInBits <= 7) { // Byte
                bytePos[idx] = 1;
            } else if (currFieldSizeInBits <= 15) { // Short
                bytePos[idx] = 2;
            } else if (currFieldSizeInBits <= 31) { // Int
                bytePos[idx] = 4;
            } else {
                throw new RuntimeException("Unexpected field size: " + currFieldSizeInBits);
            }

            // bitpos [1] --> offset
            bytePos[idx + 1] = currentlyUsedBytes;

            currentlyUsedBytes += bytePos[idx];

            // If it doesn't fit in current long, add another
            if (currentlyUsedBytes > (curLong * 8)) {
                curLong++;
            }

            idx += NUM_LONGS;

        }

        // make multiple of 8
        rowSizeInBytes = curLong * 8;
        rowSizeInLongs = (int) (rowSizeInBytes / 8);

        if (rowSizeInBytes > (8 * LENGTH_OF_SLIDE_IN_LONGS)) {
            use_fast_compare = false;
        } else {
            use_fast_compare = true;
        }

        // Allocate
        size = rowSizeInBytes * numRows;
        baseAddress = unsafe.allocateMemory(size);
        unsafe.setMemory(baseAddress, size, (byte) 0);

        freed = false;

        // precompute the offset
        for (int i = 0; i < bytePos.length; i += NUM_LONGS) {
            bytePos[i + 1] += baseAddress;
        }
    }

    /**
     * Creates a new Memory where all columns are four bytes wide.
     *
     * @param rows the rows
     * @param columns the columns
     */
    public Memory(int rows, int columns) {
        this(rows, createIntFieldSizes(columns));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory clone() {
        Memory memory = new Memory(numRows, columnSizes);
        unsafe.copyMemory(baseAddress, memory.baseAddress, size);
        return memory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#convert(org.deidentifier.arx.framework.data.IMemory)
     */
    @Override
    public int[][] convert(IMemory memory) {
        int[][] array = new int[numRows][numColumns];
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                array[row][column] = get(row, column);
            }
        }
        return array;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#convert(int[][])
     */
    @Override
    public IMemory convert(int[][] data) {
        Memory memory = new Memory(data.length, data[0].length);
        for (int row = 0; row < data.length; row++) {
            for (int column = 0; column < data[0].length; column++) {
                memory.set(row, column, data[row][column]);
            }
        }
        return memory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#equals(org.deidentifier.arx.framework.data.IMemory, int)
     */
    @Override
    public boolean equals(final IMemory other, final int row) {
        final Unsafe o = ((Memory) other).unsafe;
        final Unsafe t = unsafe;
        final long base = baseAddress + (row * rowSizeInBytes);

        if (use_fast_compare) {
            switch (rowSizeInLongs) {
            case 6:
                if (t.getLong(base + 40) != o.getLong(base + 40)) {
                    return false;
                }
            case 5:
                if (t.getLong(base + 32) != o.getLong(base + 32)) {
                    return false;
                }
            case 4:
                if (t.getLong(base + 24) != o.getLong(base + 24)) {
                    return false;
                }
            case 3:
                if (t.getLong(base + 16) != o.getLong(base + 16)) {
                    return false;
                }
            case 2:
                if (t.getLong(base + 8) != o.getLong(base + 8)) {
                    return false;
                }
            case 1:
                if (t.getLong(base) != o.getLong(base)) {
                    return false;
                }
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }
            return true;
        } else {
            final long end = base + rowSizeInBytes;
            for (long address = base; address < end; address += 8) {
                if (t.getLong(address) != o.getLong(address)) {
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
        final Unsafe t = unsafe;
        final long base1 = baseAddress + (row1 * rowSizeInBytes);
        final long base2 = baseAddress + (row2 * rowSizeInBytes);

        if (use_fast_compare) {
            switch (rowSizeInLongs) {
            case 6:
                if (t.getLong(base1 + 40) != t.getLong(base2 + 40)) {
                    return false;
                }
            case 5:
                if (t.getLong(base1 + 32) != t.getLong(base2 + 32)) {
                    return false;
                }
            case 4:
                if (t.getLong(base1 + 24) != t.getLong(base2 + 24)) {
                    return false;
                }
            case 3:
                if (t.getLong(base1 + 16) != t.getLong(base2 + 16)) {
                    return false;
                }
            case 2:
                if (t.getLong(base1 + 8) != t.getLong(base2 + 8)) {
                    return false;
                }
            case 1:
                if (t.getLong(base1) != t.getLong(base2)) {
                    return false;
                }
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }
            return true;
        } else {
            final long end1 = base1 + rowSizeInBytes;
            long address2 = base2;

            for (long address = base1; address < end1; address += 8) {
                if (t.getLong(address) != t.getLong(address2)) {
                    return false;
                }
                address += 8;
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
        final Unsafe t = unsafe;
        final long base1 = baseAddress + (row1 * rowSizeInBytes);
        final long base2 = baseAddress + (row2 * rowSizeInBytes);

        if (use_fast_compare) {
            switch (rowSizeInLongs) {
            case 6:
                if ((t.getLong(base1 + 40) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2 + 40) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 5:
                if ((t.getLong(base1 + 32) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2 + 32) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 4:
                if ((t.getLong(base1 + 24) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2 + 24) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 3:
                if ((t.getLong(base1 + 16) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2 + 16) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 2:
                if ((t.getLong(base1 + 8) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2 + 8) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
            case 1:
                if ((t.getLong(base1) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }
            return true;
        } else {
            final long end1 = base1 + rowSizeInBytes;
            long address2 = base2;

            for (long address = base1; address < end1; address += 8) {
                if ((t.getLong(address) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(address2) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                    return false;
                }
                address += 8;
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
        unsafe.freeMemory(baseAddress);
        freed = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#get(int, int)
     */
    @Override
    public int get(final int row, final int col) {
        final int idx = (col << POWER);

        if (bytePos[idx] == 1L) {
            return unsafe.getByte(bytePos[idx + 1] + (row * rowSizeInBytes));
        } else if (bytePos[idx] == 2L) {
            return unsafe.getShort(bytePos[idx + 1] + (row * rowSizeInBytes));
        } else if (bytePos[idx] == 4L) {
            return unsafe.getInt(bytePos[idx + 1] + (row * rowSizeInBytes));
        } else {
            throw new RuntimeException("Invalid field size!");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#getByteSize()
     */
    @Override
    public long getByteSize() {
        return size;
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
        final Unsafe t = unsafe;
        final long base = baseAddress + (row * rowSizeInBytes);
        long temp = 1125899906842597L;

        if (use_fast_compare) {
            switch (rowSizeInLongs) {
            case 6:
                temp = (31 * temp) + t.getLong(base + 40);
            case 5:
                temp = (31 * temp) + t.getLong(base + 32);
            case 4:
                temp = (31 * temp) + t.getLong(base + 24);
            case 3:
                temp = (31 * temp) + t.getLong(base + 16);
            case 2:
                temp = (31 * temp) + t.getLong(base + 8);
            case 1:
                temp = (31 * temp) + t.getLong(base);
                break;
            default:
                throw new RuntimeException("Invalid bytes per row!");
            }

        } else {
            final long end = base + rowSizeInBytes;
            for (long address = base; address < end; address += 8) {
                temp = (31 * temp) + t.getLong(address);
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
        final int idx = (col << POWER);

        if (bytePos[idx] == 1L) {
            unsafe.putByte(bytePos[idx + 1] + (row * rowSizeInBytes), (byte) val);
        } else if (bytePos[idx] == 2L) {
            unsafe.putShort(bytePos[idx + 1] + (row * rowSizeInBytes), (short) val);
        } else if (bytePos[idx] == 4L) {
            unsafe.putInt(bytePos[idx + 1] + (row * rowSizeInBytes), val);
        } else {
            throw new RuntimeException("Invalid field size!");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#swap(int, int)
     */
    @Override
    public void swap(int row1, int row2) {

        long tempAddress = unsafe.allocateMemory(rowSizeInBytes);
        final long base1 = baseAddress + (row1 * rowSizeInBytes);
        final long base2 = baseAddress + (row2 * rowSizeInBytes);

        // Write from row2 to temp
        unsafe.copyMemory(base2, tempAddress, rowSizeInBytes);

        // Write from row1 to row2
        unsafe.copyMemory(base1, base2, rowSizeInBytes);

        // Write from temp to row1
        unsafe.copyMemory(tempAddress, base1, rowSizeInBytes);

        // Free temp memory
        unsafe.freeMemory(tempAddress);

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!freed) {
            free();
        }
    }

    public void printMemory() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                System.out.print(get(i, j));
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
