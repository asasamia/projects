package conwaygame;
import java.util.ArrayList;
import java.util.HashSet;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        
        StdIn.setFile(file);
        int rows = StdIn.readInt();
        int cols = StdIn.readInt();
        grid = new boolean[rows][cols];

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                boolean cellState = StdIn.readBoolean();
                if (cellState == ALIVE){
                    grid[i][j] = ALIVE;
                }
            }
        }

    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        boolean cellStatus = grid[row][col];
        return cellStatus; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        boolean living = false;
        int count1 = 0;
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == ALIVE){
                    count1++;
                }
            }
        }
        if (count1 > 0){
            living = true;
        }
        return living; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        int AliveNeighbors = 0;
        int checkRow = 0;
        int checkCol = 0;

        for (int i = 1; i > -2; i--){
            for (int j = 1; j > -2; j--){

                checkRow = row - i;
                checkCol = col - j;

                if (i == 0 && j == 0){
                    continue;
                }
                if (checkRow > grid.length-1){
                    checkRow = 0;
                }
                if (checkCol > grid[0].length-1){
                    checkCol = 0;
                }
                if (checkRow < 0){
                    checkRow = grid.length-1;
                }
                if (checkCol < 0){
                    checkCol = grid[0].length-1;
                }
                if(grid[checkRow][checkCol]){
                    AliveNeighbors++;
                }

            }
        }
        return AliveNeighbors;
    }


    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                int neighbors = numOfAliveNeighbors(i, j);
                if (grid[i][j] == ALIVE && neighbors <= 1){
                    newGrid[i][j] = DEAD;
                }
                else if (grid[i][j] == ALIVE && (neighbors == 2 || neighbors == 3)){
                    newGrid[i][j] = grid[i][j];
                }
                else if (grid[i][j] == DEAD && neighbors == 3){
                    newGrid[i][j] = ALIVE;
                }
                else if (grid[i][j] == ALIVE && neighbors >=4){
                    newGrid[i][j] = DEAD;
                }
            }
        }
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        grid = computeNewGrid();
        totalAliveCells = 0;
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == ALIVE){
                    totalAliveCells++;
                } 
            }
        }
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        for (int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        WeightedQuickUnionUF communities = new WeightedQuickUnionUF(grid.length, grid[0].length);
        int checkRow = 0;
        int checkCol = 0;
        int unique = 0;

        for (int a = 0; a < grid.length; a++){
            for (int b = 0; b < grid[0].length; b++){
                if (grid[a][b]){
                    for (int i = 1; i > -2; i--){
                        for (int j = 1; j > -2; j--){
            
                            checkRow = a - i;
                            checkCol = b - j;
            
                            if (i == 0 && j == 0){
                                continue;
                            }
                            if (checkRow > grid.length-1){
                                checkRow = 0;
                            }
                            if (checkCol > grid[0].length-1){
                                checkCol = 0;
                            }
                            if (checkRow < 0){
                                checkRow = grid.length-1;
                            }
                            if (checkCol < 0){
                                checkCol = grid[0].length-1;
                            }
                            if(grid[checkRow][checkCol]){
                                communities.union(a, b, checkRow, checkCol);
                            }
            
                        }
                }
                
            }
        }
        }

        
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == ALIVE){
                    totalAliveCells++;
                } 
            }
        }

        HashSet<Integer> cells = new HashSet<>(totalAliveCells);
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j]){
                    cells.add(communities.find(i,j));
                }
            }
        }
        unique = cells.size();    

        return unique; // update this line, provided so that code compiles
    }
}
