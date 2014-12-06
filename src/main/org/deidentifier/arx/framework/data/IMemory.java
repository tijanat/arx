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
 * Interface to fast memory implementations.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public interface IMemory {

    /**
     * Clones the IMemory.
     *
     * @return the i memory
     */
    public IMemory clone();

    /**
     * Converts a given memory to an integer array.
     * NOTE: This method can be very expensive.
     *
     * @param memory the memory
     * @return the int[][]
     */
    public int[][] convert(IMemory memory);

    /**
     * Converts a given integer array to memory.
     * NOTE: This method can be very expensive.
     *
     * @param data the data
     * @return the i memory
     */
    public IMemory convert(int[][] data);

    /**
     * Compares two rows for equality.
     *
     * @param memory the memory
     * @param row the row
     * @return true, if successful
     */
    public boolean equals(IMemory memory, int row);

    /**
     * Compares two rows for equality.
     *
     * @param row1 the row1
     * @param row2 the row2
     * @return true, if successful
     */
    public boolean equals(int row1, int row2);

    /**
     * Compares two rows for equality, ignoring outlier.
     *
     * @param row1 the row1
     * @param row2 the row2
     * @return true, if successful
     */
    public boolean equalsIgnoreOutliers(int row1, int row2);

    /**
     * Releases the used memory.
     */
    public void free();

    /**
     * Gets the value on position row, col.
     *
     * @param row the row
     * @param col the col
     * @return the int
     */
    public int get(int row, int col);

    /**
     * Returns the size in bytes.
     *
     * @return the byte size
     */
    public long getByteSize();

    /**
     * Returns the number of columns.
     *
     * @return the num columns
     */
    public int getNumColumns();

    /**
     * Returns the number of rows.
     *
     * @return the num rows
     */
    public int getNumRows();

    /**
     * Returns the hashcode for row with given index.
     *
     * @param row the row
     * @return the int
     */
    public int hashCode(int row);

    /**
     * Sets the value val at position row, col.
     *
     * @param row the row
     * @param col the col
     * @param val the val
     */
    public void set(int row, int col, int val);

    /**
     * Swaps the given rows.
     *
     * @param row1 the row1
     * @param row2 the row2
     */
    public void swap(int row1, int row2);

}
