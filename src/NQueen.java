/**
 * CS 420.01: Artificial Intelligence
 * Professor: Dr. Fang Tang
 *
 * Programming Assignment #2
 * <N-Queen>
 *
 * Justin Galloway
 *
 * ~NQueen Class~
 * Prints and shows the two separate algorithms.
 * Contains main class.
 */

public class NQueen {
    public static void main(String[] args) {
        // create a new queen board for the Steepest Ascent Hill Climb
        NQueenBoard qBoard = new NQueenBoard();

        System.out.println("Steepest Ascent Hill Climb");
        System.out.println("--------------------------");

        qBoard.printBoard();
        System.out.println("\n---------------------------");

        qBoard.steepestHillClimb();
        System.out.println("\n-----------------------");

        qBoard.printBoard();
        System.out.println();

        // Mutation frequency set at 50%
        GeneticAlgBoard gQBoards = new GeneticAlgBoard(0.5f);

        System.out.println("\n--------------------");
        System.out.println("Genetic Local Search");
        System.out.println("--------------------\n");
        System.out.println("Population - " + GeneticAlgBoard.POPULATION);

        gQBoards.printPopulation();

        gQBoards.geneticSearch();
        System.out.println("\n-----------------------");
        System.out.println("-----------------------\n");
        gQBoards.printSolution();
    }
}
