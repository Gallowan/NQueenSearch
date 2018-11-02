/**
 * CS 420.01: Artificial Intelligence
 * Professor: Dr. Fang Tang
 *
 * Programming Assignment #2
 * <N-Queen>
 *
 * Justin Galloway
 *
 * ~Coordinates Class~
 * This class contains coordinate information about the
 * board, including positions in relation to each queen
 * (horizontal, vertical, diagonal) and watches for things
 * such as collisions.
 */

public class Coordinates {
    private int x, y;
    private int diagPos, diagNeg;

    private int collisions;
    private int potential;

    //Getters
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getCollisions() {
        return this.collisions;
    }
    public int getBestMove() {
        return this.potential;
    }

    // Set x and y to specified coordinates, calculate
    // their slope, set collision count to 0.
    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this.slopeCalc();
        collisions = 0;
    }

    // Set the y position, calculate slope and set
    // Collision count to 0.
    public void setYPos(int yPos) {
        this.y = yPos;
        this.slopeCalc();
        collisions = 0;
    }

    // Uses the x and y coordinates to update diagonal values
    private void slopeCalc() {
        diagPos = this.x + this.y;
        diagNeg = this.x - this.y;
    }

    // Increases number of collisions based on if their on the
    // same line, but not the same space, then calculate potential
    // of the move using the collision count.
    public void collisionCalc(Coordinates[] coords) {
        this.collisions = 0;
        // Iterate through possible Queens
        for (int i = 0; i < NQueenBoard.NWidth; i++) {
            if ((this.y == coords[i].y || this.diagPos == coords[i].diagPos
                    || this.diagNeg == coords[i].diagNeg) && this.x != i) {
                this.collisions++;
            }
        }
        this.potential = coords[this.x].getCollisions() - this.collisions;
    }
}
