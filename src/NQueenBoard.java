/**
 * CS 420.01: Artificial Intelligence
 * Professor: Dr. Fang Tang
 *
 * Programming Assignment #2
 * <N-Queen>
 *
 * Justin Galloway
 *
 * ~NQueenBoard Class~
 * Instantiates an N-NQueen Board.
 */

import java.util.Random;

public class NQueenBoard {


    // NWidth is the N of the NQueen problem.
    // TotalSize is the size of the board, represented by n*n
    public static final int NWidth = 20;
    public static final int TotalSize = NWidth * NWidth;
    // Holds the state still.
    private StringBuffer boardPrint;

    private Coordinates[] queenCoords;
    private int[] initial;

    // int to hold the current number of collisions
    int collisionCount;

    public NQueenBoard() {
        this.randomize();
    }
    public NQueenBoard(int[] currentPositions) {
        this.setPosition(currentPositions);
    }

    public int[] getInitial() {
        return this.initial;
    }
    public int getCollisionCount() {
        return this.collisionCount;
    }

    // Set queens at specific locations
    public void setBoard(int[] currentPositions) {
        this.initial = currentPositions;
        for (int i = 0; i < NWidth; i++) {
            // new board
            this.queenCoords[i] = new Coordinates(i, initial[i]);
        }
        this.collisionCount();
    }

    //Set specific queen positions to board
    //Used for moves, etc.
    private void setPosition(int[] currentPositions) {
        this.queenCoords = new Coordinates[NWidth];
        this.initial = currentPositions;
        
        for (int i = 0; i < NWidth; i++) {
            // new board
            this.queenCoords[i] = new Coordinates(i, initial[i]);
        }
        this.collisionCount();
    }

    // Randomizes the locations of the queens;
    // Calculates the collisions accordingly
    private void randomize() {
        // Initialize arrays
        Random rand = new Random();
        this.queenCoords = new Coordinates[NWidth];
        this.initial = new int[NWidth];

        // Random queen placements
        for (int i = 0; i < NWidth; i++) {
            this.initial[i] = rand.nextInt(NWidth);
            this.queenCoords[i] = new Coordinates(i, initial[i]);
        }
        this.collisionCount();
    }

    // Resets initial state
    public void reset() {
        this.setBoard(this.initial);
    }

    // Calculate the number of collisions
    private void collisionCount() {
        this.collisionCount = 0;

        // Calculate collisions
        for (int i = 0; i < NWidth; i++) {
            this.queenCoords[i].collisionCalc(queenCoords);
            this.collisionCount += this.queenCoords[i].getCollisions();
        }
        // Cut in half to avoid repeat collisions
        this.collisionCount /= 2;
    }

    // Steepest Hill Climb
    public void steepestHillClimb() {
        int bestMove = -1;
        while (bestMove != 0) {
            Coordinates bestCoords = new Coordinates(0, 0);
            bestCoords.collisionCalc(queenCoords);
            for (int i = 1; i < TotalSize; i++) {
                Coordinates bCoord = new Coordinates(i % NWidth, i / NWidth);
                bCoord.collisionCalc(queenCoords);
                if (bCoord.getBestMove() > bestCoords.getBestMove()) {
                    bestCoords = bCoord;
                }
            }

            bestMove = bestCoords.getBestMove();

            // collisions - the total of best moves
            this.collisionCount -= bestMove;
            // utilize the best moves
            this.queenCoords[bestCoords.getX()].setYPos(bestCoords.getY());
            
            if (bestMove != 0) {
                System.out.println("Steepest Move: (" + bestCoords.getX()
                        + "," + this.queenCoords[bestCoords.getX()].getY() + ") to ("
                        + bestCoords.getX() + "," + bestCoords.getY() + ").");
            }
            else {
                System.out.println("               ^Local Minimum");
            }
            // Recalculate all collisions
            for (int i = 0; i < NWidth; i++) {
                this.queenCoords[i].collisionCalc(queenCoords);
            }
        }
    }

    // Checks if it is an optimal solution
    public boolean optimalCheck() {
        return this.collisionCount == 0;
    }

    // Prints a nice looking N-NQueen Board
    private void createboardPrint() {
        this.boardPrint = new StringBuffer(TotalSize);

        boolean isEven = (NWidth % 2) == 0;
        if (isEven) {
            boolean rowToggle = false;
            for (int i = 0; i < TotalSize; i += NWidth) {
                if (rowToggle) {
                    for (int j = 0; j < NWidth; j++) {
                        this.boardPrint.append('x');
                        j++;
                        this.boardPrint.append(' ');
                    }
                }
                else {
                    for (int j = 0; j < NWidth; j++) {
                        this.boardPrint.append(' ');
                        j++;
                        this.boardPrint.append('x');
                    }
                }
                rowToggle = !rowToggle;
            }
        }

        else {
            for (int i = 0; i < TotalSize; i++) {
                if (i % 2 == 0) {
                    this.boardPrint.append(' ');
                }
                else {
                    this.boardPrint.append('x');
                }
            }
        }

        // Queen denoted by Q
        for (int i = 0; i < NWidth; i++) {
            this.boardPrint.setCharAt(i + (NWidth * this.queenCoords[i].getY()), 'Q');
        }
    }

    // Prints Board
    public void printBoard() {
        this.createboardPrint();

        char[] topBorder = new char[NWidth * 2 + 2];
        topBorder[NWidth * 2] = '\0';
        System.out.print(" ");
        for (int i = 0; i < NWidth * 2 + 1; i++) {
            topBorder[i] = '_';
        }
        System.out.print(topBorder);
        System.out.print("   ");
        System.out.println(topBorder);

        for (int i = 0; i < TotalSize; i += NWidth) {
            System.out.print("||");
            for (int j = 0; j < NWidth; j++) {
                System.out.print(this.boardPrint.charAt(i + j) + "|");
            }

            System.out.print("|");
            System.out.println();
        }
        System.out.println("Collisions: " + this.collisionCount);
    }
}
