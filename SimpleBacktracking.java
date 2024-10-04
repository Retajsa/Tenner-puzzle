/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication29;

import java.util.*;

/**
 *
 * @author noura
 */
class SimpleBacktracking {

    static int ROWS = 3;
    static int COLUMNS = 10;
    static int consistency;
    static int assignments;
    static double totalConsBT = 0;
    static double totalAssignBT = 0;
    static double totalConsFC = 0;
    static double totalAssignFC = 0;
    static double totalConsFCMRV = 0;
    static double totalAssignFCMRV = 0;
    static long startTime = 0;
    static long stopTime = 0;

    public static void main(String[] args) {

        Domin[][] grid;
        Domin[][] grid2 = new Domin[ROWS][COLUMNS];
        Domin[][] grid3 = new Domin[ROWS][COLUMNS];

        boolean[][][] poss = new boolean[ROWS][COLUMNS][10];
        AssignPoss(poss);
     

        for (int gridNumber = 1; gridNumber < 6; gridNumber++) {
            grid = generateGrid();
            DeepCopy(grid, grid2, grid3);
            resetCounters(-1);

            System.out.println("\n---------------------------------------------------------------");
            printInitialState(grid);
            System.out.println("\n by Backtracking grid: " + gridNumber);
            startTime = System.nanoTime();
            if (simpleBacktrack(grid, 0, 0)) {
                stopTime = System.nanoTime();
                print(grid);
            } else {
                System.out.println("No Solution exists");
                stopTime = System.nanoTime();
            }

            System.out.println("time to solve grid " + gridNumber + ": " + (stopTime - startTime));
            System.out.println("Consistency: " + consistency);
            System.out.println("Assignments: " + assignments);
            resetCounters(0);
            System.out.println("\n---------------------------------------------------------------");

            printInitialState(grid2);
            System.out.println("by Forwardchecking grid: " + gridNumber);
            startTime = System.nanoTime();
            if (forwardCheck(grid2, 0, 0, poss)) {
                stopTime = System.nanoTime();
                print(grid2);
            } else {
                System.out.println("No Solution exists");
                stopTime = System.nanoTime();
            }
            AssignPoss(poss);

            System.out.println("time to solve grid " + gridNumber + ": " + (stopTime - startTime));
            System.out.println("Consistency: " + consistency);
            System.out.println("Assignments: " + assignments);
            resetCounters(1);
            System.out.println("\n---------------------------------------------------------------");

            printInitialState(grid3);
            System.out.println("\nby Forwardchecking+MRV grid: " + gridNumber);
            startTime = System.nanoTime();
            if (ForwardcheckingMVR(grid3)) {
                stopTime = System.nanoTime();
                print(grid3);
            } else {
                System.out.println("No Solution exists");
                stopTime = System.nanoTime();
            }
            System.out.println("time to solve grid " + gridNumber + ": " + (stopTime - startTime));
            System.out.println("Consistency: " + consistency);
            System.out.println("Assignments: " + assignments);
            resetCounters(2);
            System.out.println("\n---------------------------------------------------------------");
        }
        System.out.println("Average consistency of backtracking : " + (totalConsBT / 5.0));
        System.out.println("Average assignments of backtracking: " + (totalAssignBT / 5.0));
        System.out.println("Average consistency of FC: " + (totalConsFC / 5.0));
        System.out.println("Average assignments of FC: " + (totalAssignFC / 5.0));
        System.out.println("Average consistency of FC+MRV: " + (totalConsFCMRV / 5.0));
        System.out.println("Average assignments of FC+MRV: " + (totalAssignFCMRV / 5.0));

    }

    static boolean simpleBacktrack(Domin grid[][], int row, int col) {
        if (row == ROWS - 1 && col == COLUMNS) {
            return true;
        }

        if (col == COLUMNS) {
            row++;
            col = 0;
        }

        if (grid[row][col].getValue() != -1) {
            return simpleBacktrack(grid, row, col + 1);
        }

        for (int num = 0; num < 10; num++) {
            if (isValid(grid, row, col, num) && checkColumeTotal(grid, row, col, num)) {
                grid[row][col].setValue(num);
                assignments++;
                if (simpleBacktrack(grid, row, col + 1)) {
                    return true;
                }
            }
            grid[row][col].setValue(-1);

        }
        return false;
    }

    static boolean ForwardcheckingMVR(Domin grid[][]) {//minVar[0]=row , minVar[1]=column
        if (isComplete(grid)) {
            return true;
        }

        int[] minVar = MRV(grid);

        for (int num = 0; num < 10; num++) {
            if (grid[minVar[0]][minVar[1]].getDominValue(num)) {

                if (isValid(grid, minVar[0], minVar[1], num) && checkColumeTotal(grid, minVar[0], minVar[1], num)) {
                    assignments++;
                    grid[minVar[0]][minVar[1]].setValue(num);

                    updateDomin2(grid, minVar[0], minVar[1], false, num);

                    if (ForwardcheckingMVR(grid)) {
                        return true;
                    }
                }

                if (grid[minVar[0]][minVar[1]].getValue() != -1) {
                    updateDomin2(grid, minVar[0], minVar[1], true, num);
                }
                grid[minVar[0]][minVar[1]].setValue(-1);

            }

        }

        return false;
    }

    public static boolean forwardCheck(Domin grid[][], int row, int col, boolean[][][] poss) {
        if (row == ROWS - 1 && col == COLUMNS) {
            return true;
        }

        if (col == COLUMNS) {
            row++;
            col = 0;
        }

        if (grid[row][col].getValue() != -1) {
            return forwardCheck(grid, row, col + 1, poss);
        }

        for (int i = 0; i < COLUMNS; i++) {
            if (poss[row][col][i] && isValid(grid, row, col, i) && checkColumeTotal(grid, row, col, i)) {
                grid[row][col].setValue(i);
                assignments++;

                UpdatePossibilities(grid, row, col, poss, i, false);
                if (forwardCheck(grid, row, col + 1, poss)) {
                    return true;
                }
                
                grid[row][col].setValue(-1);

                UpdatePossibilities(grid, row, col, poss, i, true);

            }

        }

        return false;
    }

    static Domin[][] generateGrid() {
        Random random = new Random();
        boolean solvable;
        Domin[][] grid = new Domin[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                grid[i][j] = new Domin();

            }
        }

        BacktrackingGenerator(grid, 0, 0);
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS - 1; j++) {

                if (j == 0) {
                    grid[ROWS - 1][i].setValue(0);
                }
                if (grid[j][i].getValue() != -1) {
                    grid[ROWS - 1][i].setValue(grid[ROWS - 1][i].getValue() + grid[j][i].getValue());
                }

            }
        }

        boolean AlreadyEmpty = false;
        int randomRow = random.nextInt(ROWS - 1);
        int randomCol = random.nextInt(COLUMNS);
        grid[randomRow][randomCol].setValue(-1);
        int randomEmptySpots = random.nextInt(10) + 5;
        for (int i = 1; i <= randomEmptySpots; i++) {
            if (grid[randomRow][randomCol].getValue() == -1 && AlreadyEmpty) {
                i--;
            }

            randomRow = random.nextInt(ROWS - 1);
            randomCol = random.nextInt(COLUMNS);
            if (grid[randomRow][randomCol].getValue() == -1) {
                AlreadyEmpty = true;
                continue;
            } else {
                AlreadyEmpty = false;
            }
            grid[randomRow][randomCol].setValue(-1);

        }
        return grid;
    }

    static boolean BacktrackingGenerator(Domin grid[][], int row, int col) {
        Random random = new Random();

        if (row == ROWS - 2 && col == COLUMNS) {
            return true;
        }

        if (col == COLUMNS) {
            row++;
            col = 0;
        }

        Domin d = new Domin();

        for (int randomValue = random.nextInt(10); randomValue < 10; randomValue = random.nextInt(10)) {
            if (!d.allChecked()) {
                if (grid[row][col].getDominValue(randomValue) && d.getDominValue(randomValue) == true) {
                    d.setDominValue(randomValue, false);
                } else {
                    continue;
                }

            } else {
                break;
            }

            if (isValid(grid, row, col, randomValue)) {
                grid[row][col].setValue(randomValue);
                //5
                updateDomin(grid, row, col, false, randomValue);
                if (BacktrackingGenerator(grid, row, col + 1)) {
                    return true;
                }

            }
            if (grid[row][col].getValue() != -1) {
                updateDomin(grid, row, col, true, randomValue);
            }

            grid[row][col].setValue(-1);
        }

        return false;
    }

    static boolean isValid(Domin grid[][], int row, int col, int value) {
        for (int i = 0; i < COLUMNS; i++) {
            if (i != col) {
                consistency++;
                if (grid[row][i].getValue() != -1) {

                    if (grid[row][i].getValue() == value) {
                        return false;
                    }
                }
            }
        }

        if (row - 1 != -1) {
            consistency++;
            if (grid[row - 1][col].getValue() != -1 && grid[row - 1][col].getValue() == value) {
                return false;
            }
        }

        if (row + 1 != ROWS - 1) {
            consistency++;
            if (grid[row + 1][col].getValue() != -1 && grid[row + 1][col].getValue() == value) {
                return false;
            }
        }

        if (row + 1 != ROWS - 1 && col + 1 < COLUMNS) {
            consistency++;
            if (grid[row + 1][col + 1].getValue() != -1 && grid[row + 1][col + 1].getValue() == value) {
                return false;
            }
        }

        if (col - 1 != -1 && row - 1 != -1) {
            consistency++;
            if (grid[row - 1][col - 1].getValue() != -1 && grid[row - 1][col - 1].getValue() == value) {
                return false;
            }
        }

        if (row + 1 != ROWS - 1 && col - 1 != -1) {
            consistency++;

            if (grid[row + 1][col - 1].getValue() != -1 && grid[row + 1][col - 1].getValue() == value) {
                return false;
            }
        }

        if (row - 1 != -1 && col + 1 < COLUMNS) {
            consistency++;
            if (grid[row - 1][col + 1].getValue() != -1 && grid[row - 1][col + 1].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    static void updateDomin2(Domin grid[][], int row, int col, boolean removeORadd, int value) {

        for (int j = 0; j < COLUMNS; j++) {
            if (j != col) {
                if (grid[row][j].getValue() == -1) {
                    grid[row][j].setDominValue(value, removeORadd);
                }
            }
        }

        if (row - 1 != -1) {
            if (grid[row - 1][col].getValue() == -1) {
                grid[row - 1][col].setDominValue(value, removeORadd);
            }

        }

        if (row + 1 != ROWS - 1 && grid[row + 1][col].getValue() == -1) {
            grid[row + 1][col].setDominValue(value, removeORadd);
        }

        if (col + 1 < COLUMNS && row + 1 != ROWS - 1 && grid[row + 1][col + 1].getValue() == -1) {
            grid[row + 1][col + 1].setDominValue(value, removeORadd);
        }

        if (row - 1 != -1 && col - 1 != -1 && grid[row - 1][col - 1].getValue() == -1) {
            grid[row - 1][col - 1].setDominValue(value, removeORadd);
        }

        if (row + 1 != ROWS - 1 && col - 1 != -1 && grid[row + 1][col - 1].getValue() == -1) {
            grid[row + 1][col - 1].setDominValue(value, removeORadd);
        }

        if (col + 1 < COLUMNS && row - 1 != -1 && grid[row - 1][col + 1].getValue() == -1) {
            grid[row - 1][col + 1].setDominValue(value, removeORadd);
        }
    }

    static void updateDomin(Domin grid[][], int row, int col, boolean removeORadd, int value) {
        
        for (int j = 0; j < COLUMNS; j++) {
            if (j != col) {
                if (grid[row][j].getValue() != -1) {
                    grid[row][j].setDominValue(value, removeORadd);
                }
            }
        }
        if (row - 1 != -1) {
            if (grid[row - 1][col].getValue() != -1) {
                grid[row - 1][col].setDominValue(value, removeORadd);
            }

        }

        if (row + 1 != ROWS - 1 && grid[row + 1][col].getValue() != -1) {
            grid[row + 1][col].setDominValue(value, removeORadd);
        }

        if (col - 1 != -1 && grid[row][col - 1].getValue() != -1) {
            grid[row][col - 1].setDominValue(value, removeORadd);
        }

        if (col + 1 < COLUMNS && grid[row][col + 1].getValue() != -1) {
            grid[row][col + 1].setDominValue(value, removeORadd);
        }

        if (col + 1 < COLUMNS && row + 1 != ROWS - 1 && grid[row + 1][col + 1].getValue() != -1) {
            grid[row + 1][col + 1].setDominValue(value, removeORadd);
        }

        if (row - 1 != -1 && col - 1 != -1 && grid[row - 1][col - 1].getValue() != -1) {
            grid[row - 1][col - 1].setDominValue(value, removeORadd);
        }

        if (row + 1 != ROWS - 1 && col - 1 != -1 && grid[row + 1][col - 1].getValue() != -1) {
            grid[row + 1][col - 1].setDominValue(value, removeORadd);
        }

        if (col + 1 < COLUMNS && row - 1 != -1 && grid[row - 1][col + 1].getValue() != -1) {
            grid[row - 1][col + 1].setDominValue(value, removeORadd);
        }
    }

    static boolean checkColumeTotal(Domin grid[][], int row, int col, int value) {
        int sum = value;
        int count = 0;
        consistency++;

        for (int i = 0; i < ROWS - 1; i++) {
            if (grid[i][col].getValue() != -1) {
                count++;
                sum += grid[i][col].getValue();
            }
        }
        if (sum > grid[ROWS - 1][col].getValue()) {
            return false;
        }
        if (count == 1 && sum != grid[ROWS - 1][col].getValue()) {
            return false;
        }
        return true;

    }

    static boolean isComplete(Domin grid[][]) {
        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (grid[i][j].getValue() == -1) {
                    return false;
                }
            }
        }
        return true;

    }

    static int[] MRV(Domin grid[][]) {
        int min = 10;
        int[] mindim = new int[2];
        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (grid[i][j].getValue() == -1 && grid[i][j].DominSize() < min) {
                    min = grid[i][j].DominSize();
                    mindim[0] = i;
                    mindim[1] = j;
                }

            }
        }
        return mindim;
    }

    static void DeepCopy(Domin grid[][], Domin[][] copygrid1, Domin[][] copygrid2) {
        try {
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    copygrid1[i][j] = (Domin) grid[i][j].clone();  // Perform deep cloning for each element
                    copygrid2[i][j] = (Domin) grid[i][j].clone();  // Perform deep cloning for each element
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public static void UpdatePossibilities(Domin[][] grid, int row, int col, boolean[][][] possibilities, int num, boolean add) {
        for (int i = 0; i < COLUMNS; i++) {
            possibilities[row][i][num] = add;
        }

        try {
            if (grid[row - 1][col].getValue() == num) {
                possibilities[row - 1][col][num] = add;
            }
        } catch (Exception e) {

        }

        try {
            if (grid[row + 1][col].getValue() == num) {
                possibilities[row + 1][col][num] = add;
            }
        } catch (Exception e) {

        }

        try {
            if (grid[row][col + 1].getValue() == num) {
                possibilities[row][col + 1][num] = add;
            }
        } catch (Exception e) {

        }

        try {
            if (grid[row][col - 1].getValue() == num) {
                possibilities[row][col - 1][num] = add;
            }
        } catch (Exception e) {

        }

        try {
            if (grid[row - 1][col - 1].getValue() == num) {
                possibilities[row - 1][col - 1][num] = add;
            }
        } catch (Exception e) {

        }

        try {
            if (grid[row + 1][col + 1].getValue() == num) {
                possibilities[row + 1][col + 1][num] = add;
            }
        } catch (Exception e) {

        }

        try {
            if (grid[row + 1][col - 1].getValue() == num) {
                possibilities[row + 1][col - 1][num] = add;
            }
        } catch (Exception e) {

        }
        try {
            if (grid[row - 1][col + 1].getValue() == num) {
                possibilities[row - 1][col + 1][num] = add;
            }
        } catch (Exception e) {

        }

    }

    static void print(Domin[][] grid) {
        System.out.println("\n Final State :");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                System.out.printf("%3d ", grid[i][j].getValue());
            }
            System.out.println("");
        }
    }

    static void printInitialState(Domin[][] grid) {
        System.out.println("\n Initial State :");
        for (int i = 0; i < ROWS; i++) {

            for (int j = 0; j < COLUMNS; j++) {
                if (grid[i][j].getValue() != -1) {
                    System.out.printf("%3d ", grid[i][j].getValue());
                } else {
                    System.out.printf("%3s ", " ");
                }
            }
            System.out.println("");
        }
    }

    static void resetCounters(int i) {

        if (i == 0) {
            totalConsBT += consistency;
            totalAssignBT += assignments;

        }
        if (i == 1) {
            totalConsFC += consistency;
            totalAssignFC += assignments;
        }
        if (i == 2) {
            totalConsFCMRV += consistency;
            totalAssignFCMRV += assignments;
        }
        consistency = 0;
        assignments = 0;
    }

    static void AssignPoss(boolean[][][] poss) {
        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                for (int num = 0; num < 10; num++) {
                    poss[i][j][num] = true;
                }
            }
        }
    }

}
