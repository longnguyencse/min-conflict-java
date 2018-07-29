package dev.bk.edu;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class MinConflictsQueensSolver {

    private static class Board {
        Random random = new Random();

        /**
         * The row for each column, For example [3,7,0,4,6,1,5,2] represents
         * <pre>
         *     ..Q.....
         *     .....Q..
         *     .......Q
         *     Q.......
         *     ...Q....
         *     ......Q.
         *     ....Q...
         *     .Q......
         * </pre>
         */
        int[] rows;

        /**
         * Creates a new n x n board and randomly fills it with one
         * queen in each column.
         */
        Board(int n) {
            rows = new int[n];
            scramble();
        }

        /**
         * Randomly fills the board with one queen in each column.
         */
        void scramble() {
            for (int i = 0, n = rows.length; i < n; i++) {
                rows[i] = i;
            }
            for (int i = 0, n = rows.length; i < n; i++) {
                int j = random.nextInt(n);
                int rowToSwap = rows[i];
                rows[i] = rows[j];
                rows[j] = rowToSwap;
            }
        }

        /**
         * Returns the number of queens that conflict with (row,col), not
         * counting the queen in column col.
         */
        int conflicts(int row, int col) {
            int count = 0;
            for (int c = 0; c < rows.length; c++) {
                if (c == col) continue;
                int r = rows[c];
                if (r == row || Math.abs(r-row) == Math.abs(c-col)) count++;
            }
            return count;
        }

        /**
         * Fills the board with a legal arrangement of queens.
         */
        void solve() {
            int moves = 0;

            // This would be a lot faster if we used arrays of ints instead.
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            while (true) {

                // Find nastiest queen
                int maxConflicts = 0;
                candidates.clear();
                for (int c = 0; c < rows.length; c++) {
                     int conflicts = conflicts(rows[c], c);
                    if (conflicts == maxConflicts) {
                        candidates.add(c);
                    } else if (conflicts > maxConflicts) {
                        maxConflicts = conflicts;
                        candidates.clear();
                        candidates.add(c);
                    }
                }

                if (maxConflicts == 0) {
                    // Checked *every* queen and found no conflicts
                    return;
                }

                // Pick a random queen from those that had the most conflicts
                int worstQueenColumn =
                        candidates.get(random.nextInt(candidates.size()));

                // Move her to the place with the least conflicts.
                int minConflicts = rows.length;
                candidates.clear();
                for (int r = 0; r < rows.length; r++) {
                    int conflicts = conflicts(r, worstQueenColumn);
                    if (conflicts == minConflicts) {
                        candidates.add(r);
                    } else if (conflicts < minConflicts) {
                        minConflicts = conflicts;
                        candidates.clear();
                        candidates.add(r);
                    }
                }

                if (!candidates.isEmpty()) {
                    rows[worstQueenColumn] =
                            candidates.get(random.nextInt(candidates.size()));
                }

                moves++;
                if (moves == rows.length * 2) {
                    // Trying too long... start over.
                    scramble();
                    moves = 0;
                }
            }
        }

        /**
         * Prints the board, crudely, to a print stream.
         */
        void print(PrintStream stream) {
            for (int r = 0; r < rows.length; r++) {
                for (int c = 0; c < rows.length; c++) {
                    stream.print(rows[c] == r ? 'Q' : '|');
                }
                stream.println();
            }
        }
    }

    /**
     * Runs the application.
     */
    public static void main(String[] args) {
        Board board = new Board(8);
        long start = System.currentTimeMillis();
        board.solve();
        long stop = System.currentTimeMillis();
        System.out.println("Found in " + ((double)(stop-start))/1000 + "s.");
        board.print(System.out);
    }
}