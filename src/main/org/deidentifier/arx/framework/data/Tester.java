package org.deidentifier.arx.framework.data;

import java.util.Random;

public class Tester {

    private static final int ROWS    = 7;
    private static final int COLUMNS = 3;
    private static final int REPEATS = 10000;

    public static void main(String[] args) {
        Random rnd = new Random();

        for (int k = 0; k < REPEATS; k++) {

            System.out.println("RUN: " + (k + 1));

            int[][] data = new int[ROWS][COLUMNS];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = rnd.nextInt(5);
                }
            }

            IMemory memory = new Memory(ROWS, COLUMNS);

            // Fill
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    memory.set(i, j, data[i][j]);
                }
            }

            // Compare
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    if (data[i][j] != memory.get(i, j)) {
                        throw new RuntimeException("Missmatch");
                    }
                }
            }
        }
    }

}
