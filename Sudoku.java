package Sudoku;

import java.util.Random;
import java.util.Scanner;

public class Sudoku {
    public static void main(String[] args) {
        int suSize = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Hitu's Sudoku!");
        System.out.println("Pick your Sudoku size:");
        System.out.println("4x4  ;  9x9");
        String size = scanner.next();
        if (size.equalsIgnoreCase("4x4")) {
            suSize = 4;
        } else if (size.equalsIgnoreCase("9x9")) {
            suSize = 9;
        } else {
            System.out.println("Invalid input. Defaulting to 9x9.");
            suSize = 9;
        }

        System.out.println("Pick your level of difficulty:");
        System.out.println("Type 'easy', 'medium' or 'hard'");
        String difficulty = scanner.next();

        // Initialize the Sudoku grid and the solution grid based on the size
        int[][] Su = new int[suSize][suSize];
        int[][] solution = new int[suSize][suSize];

        // Generate the Sudoku puzzle based on difficulty (fills Su and solution)
        generateSudoku(Su, solution, suSize, difficulty);

        // Print the generated Sudoku grid
        System.out.println("Here is your puzzle:");
        printSu(Su, suSize);

        // Let the user solve the puzzle, checking and updating Su as they go
        playSudoku(Su, solution, suSize, scanner);
    }

    // Method to generate a Sudoku puzzle based on difficulty
    public static void generateSudoku(int[][] Su, int[][] solution, int suSize, String difficulty) {
        Random rand = new Random();

        // Generate a fully solved Sudoku grid
        fillGrid(Su, suSize);

        // Save the completed solution before we start removing numbers
        for (int r = 0; r < suSize; r++) {
            for (int c = 0; c < suSize; c++) {
                solution[r][c] = Su[r][c];
            }
        }

        // Now remove elements based on difficulty
        int totalCells = suSize * suSize;
        int numFilled = 0;

        // Determine the number of cells to remove based on difficulty
        difficulty = difficulty.toLowerCase();

        if (difficulty.equals("easy")) {
            numFilled = totalCells / 2;
        } else if (difficulty.equals("medium")) {
            numFilled = (int) (totalCells * 0.4);
        } else if (difficulty.equals("hard")) {
            numFilled = (int) (totalCells * 0.3);
        } else {
            System.out.println("Invalid difficulty. Using easy.");
            numFilled = totalCells / 2;
        }

        // Remove numbers based on the calculated count
        int count = totalCells - numFilled;  // Number of empty cells to leave

        while (count > 0) {
            int row = rand.nextInt(suSize);
            int col = rand.nextInt(suSize);

            // Only remove the element if it's not already empty
            if (Su[row][col] != 0) {
                Su[row][col] = 0;  // Remove the element
                count--;
            }
        }

        System.out.println("Generated Sudoku grid with " + numFilled + " filled elements:");
    }

    // Helper method to fill the grid with a valid Sudoku solution
    public static boolean fillGrid(int[][] Su, int suSize) {
        return solveSudoku(Su, suSize);
    }

    // Helper method to solve the Sudoku (used to fill the grid)
    public static boolean solveSudoku(int[][] Su, int suSize) {
        Random rand = new Random();

        for (int row = 0; row < suSize; row++) {
            for (int col = 0; col < suSize; col++) {
                if (Su[row][col] == 0) { // Find an empty cell

                    // Create an array of numbers 1 through suSize
                    int[] numbers = new int[suSize];
                    for (int i = 0; i < suSize; i++) {
                        numbers[i] = i + 1;
                    }

                    // Shuffle the numbers
                    for (int i = 0; i < suSize; i++) {
                        int j = rand.nextInt(suSize);
                        int temp = numbers[i];
                        numbers[i] = numbers[j];
                        numbers[j] = temp;
                    }

                    // Try each shuffled number
                    for (int i = 0; i < suSize; i++) {
                        int num = numbers[i];

                        if (checkValid(Su, row, col, num, suSize)) {
                            Su[row][col] = num;

                            if (solveSudoku(Su, suSize)) {
                                return true;
                            }

                            Su[row][col] = 0; // Backtrack
                        }
                    }

                    return false; // No valid number found
                }
            }
        }

        return true; // Puzzle if solved
    }

    // Method to check if the Sudoku grid is valid for a specific placement
    public static boolean checkValid(int[][] Su, int row, int column, int num, int suSize) {
        // Check the row for duplicates
        for (int col = 0; col < suSize; col++) {
            if (Su[row][col] == num) {
                return false; // Number is already in the row
            }
        }

        // Check the column for duplicates
        for (int r = 0; r < suSize; r++) {
            if (Su[r][column] == num) {
                return false; // Number is already in the column
            }
        }

        // Determine the size of the subgrid (box)
        int subgridSize = 0;
        if (suSize == 4) {
            subgridSize = 2; // For 4x4 grid, the subgrid is 2x2
        } else if (suSize == 9) {
            subgridSize = 3; // For 9x9 grid, the subgrid is 3x3
        }

        // Check the box for duplicates
        // Find the top-left corner of the subgrid (box)
        int startRow = (row / subgridSize) * subgridSize;
        int startCol = (column / subgridSize) * subgridSize;

        // Loop through the subgrid and check for duplicates
        for (int r = startRow; r < startRow + subgridSize; r++) {
            for (int c = startCol; c < startCol + subgridSize; c++) {
                if (Su[r][c] == num) {
                    return false; // Number is already in the subgrid
                }
            }
        }

        // If no conflicts were found, the move is valid
        return true;
    }

    // Method that runs the interactive solving loop: user guesses, we check
    // against the solution, and Su gets updated with correct answers.
    public static void playSudoku(int[][] Su, int[][] solution, int suSize, Scanner scanner) {
        System.out.println();
        System.out.println("Enter your answer in the form row,column,number  (example: 3,5,9)");
        System.out.println("Rows and columns start at 1. Type 'quit' at any time to stop.");
        System.out.println();

        while (!isSolved(Su, suSize)) {
            System.out.print("Your move: ");
            String input = scanner.next();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing! Here is the puzzle as you left it:");
                printSu(Su, suSize);
                return;
            }

            String[] parts = input.split(",");
            if (parts.length != 3) {
                System.out.println("That's not the right format. Use row,column,number like 3,5,9.");
                continue;
            }

            int row, col, num;
            try {
                row = Integer.parseInt(parts[0].trim()) - 1;
                col = Integer.parseInt(parts[1].trim()) - 1;
                num = Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException e) {
                System.out.println("That's not the right format. Use row,column,number like 3,5,9.");
                continue;
            }

            if (row < 0 || row >= suSize || col < 0 || col >= suSize || num < 1 || num > suSize) {
                System.out.println("Those numbers are out of range for this " + suSize + "x" + suSize + " grid.");
                continue;
            }

            if (Su[row][col] != 0) {
                System.out.println("That cell is already filled in. Pick an empty one.");
                continue;
            }

            if (solution[row][col] == num) {
                Su[row][col] = num;
                System.out.println("Correct!");
                printSu(Su, suSize);
            } else {
                System.out.println("That's not right, try again.");
            }
        }

        System.out.println("Congratulations! You solved the puzzle!");
    }

    // Checks whether every cell in Su has been filled in
    public static boolean isSolved(int[][] Su, int suSize) {
        for (int row = 0; row < suSize; row++) {
            for (int col = 0; col < suSize; col++) {
                if (Su[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to print the Sudoku grid
    public static void printSu(int[][] Su, int suSize) {
        int rows = Su.length;
        int columns = Su[0].length;

        // Print the top border
        for (int i = 0; i < columns; i++) {
            System.out.print("-----");
        }
        System.out.println("-");

        // Iterate through each row
        for (int i = 0; i < rows; i++) {
            System.out.print("|");  // Start of the row

            // Print each column in the row
            for (int j = 0; j < columns; j++) {
                if (Su[i][j] == 0) {
                    System.out.print("    ");  // Print spaces for zero values
                } else {
                    System.out.print(String.format(" %2d ", Su[i][j]));  // Print the number with padding
                }
                System.out.print("|");  // Column separator
            }
            System.out.println();

            // Print a separator line after the row
            for (int k = 0; k < columns; k++) {
                System.out.print("-----");
            }
            System.out.println("-");  // Final separator line after the columns
        }
    }
}