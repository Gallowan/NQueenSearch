/**
 * CS 420.01: Artificial Intelligence
 * Professor: Dr. Fang Tang
 *
 * Programming Assignment #2
 * <N-Queen>
 *
 * Justin Galloway
 *
 * ~GeneticAlgBoard Class~
 * Solves N-Queen Problem using the Genetic Algorithm.
 * Rate of mutations can be specified, as well as
 * POPULATION field able to be changed and reflect
 * their issued purpose.
 * Learned from an example, I believe it's implemented
 * properly?
 * Also learned how to use arraycopy. Preeetty neat.
 */

import java.util.Random;

public class GeneticAlgBoard {
    
    // Instantiates an array of boards to be the population
    // of genetics for the algorithm
    private NQueenBoard[] qBoards;
    private NQueenBoard bestSol;
    private NQueenBoard secondBestSol;
    // 2D array to create a tournament tree, int for depth
    private NQueenBoard[][] tournamentTree;
    private int treeDepth; // final?
    // Mutation percent value for frequency
    private float mutationPercent; // final?

    // Seeds the algorithm with the size of the population.
    // 2*(whatever integer you choose)
    public static final int POPULATION = 2*7;
    // value to allow the user to see all tournamentTrees of every generation
    // before the optimal solution is found
    // private boolean printPopulationTrees;
    
    // Number of generations it takes to reach the end
    private int goalGen;
    // Random to be used for crossover and mutations
    private Random random;

    // Takes the percent frequency and outputs a search that
    // gives the best possible solution
    public GeneticAlgBoard(float mutatePercent) {
        this.mutationPercent = mutatePercent;
        this.random = new Random();
        this.goalGen = 0;

        // depth = log2(elements) = log(elements) / log(2)
        double dTreeDepth = Math.log(POPULATION) / Math.log(2);
        // tree depth = logn + 1
        this.treeDepth = (int)dTreeDepth + 1;

        this.tournamentTree = new NQueenBoard[this.treeDepth][];

        // Random boards in an array of boards of number population
        qBoards = new NQueenBoard[POPULATION];
        for (int i = 0; i < POPULATION; i++) {
            qBoards[i] = new NQueenBoard();
        }

        // set the printing of the population trees to false
        // user can set to true, and the tournamentTree of each generation
        // will print [IT'S USUALLY A LOT OF TREES!]
        // this.printPopulationTrees = false;
    }

    // Takes the boards with the least collisions and populates
    // until a board with 0 collisions is reached
    public void geneticSearch() {
        do {
            // Tournament selects the two best boards
            this.tourneySelect();
            this.populate();
            this.goalGen += 1;
        } while (!this.bestSol.optimalCheck());
    }

    // Find the best tournament boards using an algorithm
    private void tourneySelect() {
        this.createTournamentTree();

        // if user specified they want to see each tournament tree, print current tree
        // if (this.printPopulationTrees) {
        //    this.printTournamentTree();
        //}

        this.bestSol = this.tournamentTree[this.treeDepth - 1][0];
        this.setSecondBest();
    }

    // Creates a new tournament tree through comparing the
    // boards and their collisions
    private void createTournamentTree() {
        this.tournamentTree[0] = this.qBoards;
        NQueenBoard[] currentRow = this.qBoards;

        for (int i = 1; i < this.treeDepth; i++) {
            int newRowSize = currentRow.length / 2;
            NQueenBoard[] nextRow = new NQueenBoard[newRowSize];

            // Analyze winners of the comparison and set it equal
            // to the current slot in the new row
            for (int j = 0; j < newRowSize; j++) {
                nextRow[j] = this.fitnessFunc(currentRow[j * 2], currentRow[j * 2 + 1]);
            }
            // iterate to the next row
            currentRow = nextRow;
            this.tournamentTree[i] = nextRow;
        }
    }

    // Check the index of the best board
    private void setSecondBest() {
        int bestIndex;
        if (this.tournamentTree[this.treeDepth - 2][0] == this.bestSol) {
            this.secondBestSol = this.tournamentTree[this.treeDepth - 2][1];
            bestIndex = 0;
        }
        else {
            this.secondBestSol = this.tournamentTree[this.treeDepth - 2][0];
            bestIndex = 1;
        }
        for (int i = this.treeDepth - 3; i > -1; i--) {
            if (this.tournamentTree[i][bestIndex * 2] == this.bestSol) {
                this.secondBestSol = this.fitnessFunc(this.secondBestSol, this.tournamentTree[i][bestIndex * 2 + 1]);
                bestIndex *= 2;
            }
            else {
                this.secondBestSol = this.fitnessFunc(this.secondBestSol, this.tournamentTree[i][bestIndex * 2]);
                bestIndex = (bestIndex * 2) + 1;
            }
        }
    }

    // Create new boards using the best and second best as seeds
    private void populate() {
        int[] parentOne = this.bestSol.getInitial();
        int[] parentTwo = this.secondBestSol.getInitial();
        
        boolean splice = true;
        for (int i = 0; i < POPULATION; i++) {
            // Create a new chromosome and a random crossover point
            int[] newChromosome = new int[NQueenBoard.NWidth];
            int cross = random.nextInt(NQueenBoard.NWidth);
            if (splice = true){
                System.arraycopy(parentOne, 0, newChromosome, 0, cross);
                System.arraycopy(parentTwo, cross, newChromosome, cross, NQueenBoard.NWidth - cross);
            }
            else {
                System.arraycopy(parentTwo, 0, newChromosome, 0, cross);
                System.arraycopy(parentOne, cross, newChromosome, cross, NQueenBoard.NWidth - cross);
            }

            // Mutate a random row with a random value based on
            // The mutation percent
            if (random.nextInt(1000) < (this.mutationPercent * 1000)) {
                newChromosome[random.nextInt(NQueenBoard.NWidth)] = random.nextInt(NQueenBoard.NWidth);
            }
            this.qBoards[i] = new NQueenBoard(newChromosome);
            splice = !splice;
        }
    }
    
    // Helper function that returns whichever board
    // has the least amount of collisions
    private NQueenBoard fitnessFunc(NQueenBoard boardOne, NQueenBoard boardTwo) {
        if (boardOne.getCollisionCount() < boardTwo.getCollisionCount()) {
            return boardOne;
        }
        else {
            return boardTwo;
        }
    }

    // Prints population
    public void printPopulation() {
        for (int i = 0; i < qBoards.length; i++) {
            qBoards[i].printBoard();
        }
    }

    // Prints solution board
    public void printSolution() {
        if (this.bestSol != null) {
            this.bestSol.printBoard();
            System.out.println("Generations: " + this.goalGen);
        }
        else {
            System.out.println("No solution found.");
        }
    }

    // Print tournament tree
    private void printTournamentTree() {
        System.out.println("Tournament Tree: ");
        for (int i = 0; i < this.treeDepth; i++) {
            for (int j = 0; j < this.tournamentTree[i].length; j++) {
                System.out.print(this.tournamentTree[i][j].collisionCount + " \n");
            }
        }
    }

}
