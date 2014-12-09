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

import sun.misc.Unsafe;

/**
 * A fast implementation of a two dimensional integer array based
 * on unsafe memory accesses, which implements specialized methods.
 * 
 * Each row is 8 byte (long) aligned, to allow for fast equals() and hashcode().
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class MemoryUnsafe2 implements IMemory {

    /** The base address of the memory field in bytes. */
    private final long   baseAddress;

    /** The size in bytes of one row. */
    private final long   rowSizeInBytes;

    /** The size in longs of one row. */
    private final int    rowSizeInLongs;

    /** The total size of the allocated memory. */
    private final long   size;

    /** The unsafe. */
    private final Unsafe unsafe;

    /** Flag to indicate if the allocated memory has been freed. */
    private boolean      freed;

    /** The number of rows. */
    private final int    numRows;

    /** The number of columns. */
    private final int    numColumns;

    /** The offset address for each colum. */
    private final long[] offsets;

    /**
     * Instantiates a new memory.
     *
     * @param numRows the num rows
     * @param numColumns the num columns
     */
    public MemoryUnsafe2(final int numRows, final int numColumns) {
        this.numColumns = numColumns;
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

        rowSizeInLongs = (int) (Math.ceil(numColumns / 2d));
        rowSizeInBytes = rowSizeInLongs * 8;

        // Allocate
        size = rowSizeInBytes * numRows;
        baseAddress = unsafe.allocateMemory(size);
        unsafe.setMemory(baseAddress, size, (byte) 0);

        offsets = new long[numColumns];
        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = baseAddress + ((i >> 1) * 8) + ((i & 1) << 2);
        }

        freed = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory clone() {
        MemoryUnsafe2 memory = new MemoryUnsafe2(numRows, numColumns);
        unsafe.copyMemory(baseAddress, memory.baseAddress, size);
        return memory;
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
            final long end1 = base1 + rowSizeInBytes;
            long address2 = base2;

            for (long address = base1; address < end1; address += 8) {
                if (t.getLong(address) != t.getLong(address2)) {
                    return false;
                }
                address2 += 8;
            }
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
        final Unsafe t = unsafe;
        final long base1 = baseAddress + (row1 * rowSizeInBytes);
        final long base2 = baseAddress + (row2 * rowSizeInBytes);

        switch (rowSizeInLongs) {
        case 6:
            if ((t.getLong(base1 + 40)) != (t.getLong(base2 + 40))) {
                return false;
            }
        case 5:
            if ((t.getLong(base1 + 32)) != (t.getLong(base2 + 32))) {
                return false;
            }
        case 4:
            if ((t.getLong(base1 + 24)) != (t.getLong(base2 + 24))) {
                return false;
            }
        case 3:
            if ((t.getLong(base1 + 16)) != (t.getLong(base2 + 16))) {
                return false;
            }
        case 2:
            if ((t.getLong(base1 + 8)) != (t.getLong(base2 + 8))) {
                return false;
            }
        case 1:
            if ((t.getLong(base1) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                return false;
            }
            break;
        default:

            if ((t.getLong(base1) & Data.REMOVE_OUTLIER_LONG_MASK) != (t.getLong(base2) & Data.REMOVE_OUTLIER_LONG_MASK)) {
                return false;
            }

            final long end1 = base1 + rowSizeInBytes;
            long address2 = base2 + 8;

            for (long address = base1 + 8; address < end1; address += 8) {
                if ((t.getLong(address)) != (t.getLong(address2))) {
                    return false;
                }
                address2 += 8;
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
        return unsafe.getInt((row * rowSizeInBytes) + offsets[col]);
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
     * @see java.lang.Object#clone()
     */
    @Override
    public IMemory newInstance() {
        return new MemoryUnsafe2(numRows, numColumns);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.deidentifier.arx.framework.data.IMemory#set(int, int, int)
     */
    @Override
    public void set(final int row, final int col, final int val) {
        unsafe.putInt((row * rowSizeInBytes) + offsets[col], val);
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

}
