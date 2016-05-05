package model;

import model.rules.RuleParser;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pair programming
 *
 * Game class with a game board with dynamic size.
 * The game board will expand or shrink to fit around the pattern
 */
public class DynamicGameOfLife extends GameOfLife{

    // game board
    private ArrayList<ArrayList<AtomicBoolean>> grid;
    private ArrayList<ArrayList<AtomicInteger>> neighbours;

    //region start up

    /**
     * StaticGameOfLife Constructor. Sets the classic Conway rule (B3/S23) as default rule.
     */
    public DynamicGameOfLife() {

        createGameBoard();
        setRule(RuleParser.CLASSIC_RULESTRING);
    }

    /**
     * Creates the boolean 2D Array to keep track of dead and live cells, and the 2D byte-
     * array to keep track of the neighbour count to the corresponding cells in the other array
     */
    private void createGameBoard() {

        grid = new ArrayList<>();
        neighbours = new ArrayList<>();

        grid.add(new ArrayList<>());
        neighbours.add(new ArrayList<>());
        grid.get(0).add(new AtomicBoolean(false));
        neighbours.get(0).add(new AtomicInteger(0));
    }
    //endregion

    //region next generation

    @Override
    public void nextGeneration() {

        fitBoardToPattern();
        super.nextGeneration();
    }

    /**
     * Will expand or shrink the game board to fit around the pattern
     */
    public void fitBoardToPattern() {

        int [] bBox = getBoundingBox();
        if (bBox[0] > 1){
            decreaseXLeft(bBox[0] - 1);
            bBox[1] -= bBox[0] - 1;
        }
        else if(bBox[0] == 0){
            increaseXLeft(1);
            bBox[1] += 1;
        }
        if (bBox[1] < grid.size() - 2){
            decreaseXRight(grid.size() - bBox[1] - 2);
        }
        else if(bBox[1] == grid.size() - 1){
            increaseXRight(1);
        }

        if (bBox[2] > 1){
            decreaseYTop(bBox[2] - 1);
            bBox[3] -= bBox[2] - 1;
        }
        else if (bBox[2] == 0){
            increaseYTop(1);
            bBox[3] += 1;
        }
        if (bBox[3] < grid.get(0).size() - 2){
            decreaseYBottom(grid.get(0).size() - bBox[3] - 2);
        }
        else if(bBox[3] == grid.get(0).size() - 1){
            increaseYBottom(1);
        }
    }

    //endregion

    //region dynamic board handling

    /**
     * Will add a number of columns to the right side of the game board
     *
     * @param diffX Number of columns
     */
    public void increaseXRight(int diffX) {

        for (int i = 0; i < diffX; i++){

            grid.add(new ArrayList<>());
            neighbours.add(new ArrayList<>());

            for (int j = 0; j < grid.get(0).size(); j++) {

                grid.get(grid.size() - 1).add(new AtomicBoolean(false));
                neighbours.get(grid.size() - 1).add(new AtomicInteger(0));
            }
        }
    }

    /**
     * Will add a number of rows to the bottom side of the game board
     *
     * @param diffY Number of rows
     */
    public void increaseYBottom(int diffY){

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){

                grid.get(i).add(new AtomicBoolean(false));
                neighbours.get(i).add(new AtomicInteger(0));
            }
        }
    }

    /**
     * Will add a number of columns to the left side of the game board
     *
     * @param diffX Number of columns
     */
    public void increaseXLeft(int diffX) {

        cellOffsetX += diffX;

        for (int i = 0; i < diffX; i++){

            grid.add(0, new ArrayList<>());
            neighbours.add(0, new ArrayList<>());

            for (int j = 0; j < grid.get(1).size(); j++) {

                grid.get(0).add(new AtomicBoolean(false));
                neighbours.get(0).add(new AtomicInteger(0));
            }
        }
    }

    /**
     * Will add a number of rows to the top side of the game board
     *
     * @param diffY Number of rows
     */
    public void increaseYTop(int diffY){

        cellOffsetY += diffY;

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){

                grid.get(i).add(0, new AtomicBoolean(false));
                neighbours.get(i).add(0, new AtomicInteger(0));
            }
        }
    }

    /**
     * Will remove a number of columns from the right side of the game board
     *
     * @param diffX Number of columns
     */
    public void decreaseXRight(int diffX) {

        for (int i = 0; i < diffX; i++){

            grid.remove(grid.size() - 1);
            neighbours.remove(neighbours.size() - 1);
        }
    }

    /**
     * Will remove a number of row from the bottom side of the game board
     *
     * @param diffY Number of rows
     */
    public void decreaseYBottom(int diffY){

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){

                grid.get(i).remove(grid.get(i).size() - 1);
                neighbours.get(i).remove(neighbours.get(i).size() - 1);
            }
        }
    }

    /**
     * Will remove a number of columns from the left side of the game board
     *
     * @param diffX Number of columns
     */
    public void decreaseXLeft(int diffX) {

        cellOffsetX -= diffX;

        for (int i = 0; i < diffX; i++){

            grid.remove(0);
            neighbours.remove(0);
        }
    }

    /**
     * Will remove a number of row from the top side of the game board
     * @param diffY Number of rows
     */
    public void decreaseYTop(int diffY){

        cellOffsetY -= diffY;

        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < diffY; j++){

                grid.get(i).remove(0);
                neighbours.get(i).remove(0);
            }
        }
    }

    //endregion

    //region getters

    /**
     * Getter for neighbour-2D-array
     *
     * @return The neighbour-2D-array
     */
    public ArrayList<ArrayList<AtomicInteger>> getNeighbours() {
        return neighbours;
    }

    /**
     * Getter for the cell-2D-array
     *
     * @return The cell-2D-array
     */
    public ArrayList<ArrayList<AtomicBoolean>> getGrid() {
        return grid;
    }

    @Override
    public int getGridWidth(){ return grid.size(); }

    @Override
    public int getGridHeight(){ return grid.get(0).size(); }

    @Override
    public int getNeighboursAt(int x, int y){
        return neighbours.get(x).get(y).get();
    }

    @Override
    public boolean isCellAlive(int x, int y){

        if(grid == null)
            return false;

        try{
            return  grid.get(x).get(y).get();
        }
        catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * Clones the DynamicGameOfLife object
     * @return the cloned DynamicGameOfLife object
     */
    @Override
    public DynamicGameOfLife clone() {

        DynamicGameOfLife gameOfLife = new DynamicGameOfLife();
        gameOfLife.deepCopyOnSet(grid);
        gameOfLife.setRule(getRule().toString());
        gameOfLife.setCellCount(cellCount.get());

        return gameOfLife;
    }

    /**
     * Deep copies the grid and sets it.
     * @param grid the grid to be deep copied and set.
     */
    private void deepCopyOnSet(ArrayList<ArrayList<AtomicBoolean>> grid) {

        neighbours.clear();
        cellOffsetX = 0;
        cellOffsetY = 0;
        this.grid.clear();

        for (int x = 0; x < grid.size(); x++) {

            this.grid.add(new ArrayList<>());
            neighbours.add(new ArrayList<>());

            for (int y = 0; y < grid.get(x).size(); y++) {

                this.grid.get(x).add(new AtomicBoolean(grid.get(x).get(y).get()));
                neighbours.get(x).add(new AtomicInteger(0));
            }
        }
    }
    //endregion

    //region setters

    /**
     * Sets the cell grid to be used
     *
     * @param grid Cell grid
     */
    public void setGrid(ArrayList<ArrayList<AtomicBoolean>> grid) {
        this.grid = grid;
    }

    @Override
    public void setCellAlive(int x, int y) {

        if(!isCellAlive(x,y)) {

            try {
                grid.get(x).get(y).set(true);
            }
            catch (IndexOutOfBoundsException e) {

                int diffX = x - getGridWidth() + 1;
                if(diffX > 0)
                    increaseXRight(diffX);

                int diffY = y - getGridHeight() + 1;
                if(diffY > 0)
                    increaseYBottom(diffY);

                grid.get(x).get(y).set(true);
            }

            cellCount.incrementAndGet();
        }
    }

    @Override
    public void setCellDead(int x, int y) {

        if(isCellAlive(x,y)) {

            grid.get(x).get(y).set(false);
            cellCount.decrementAndGet();
        }
    }

    @Override
    public void clearGrid() {

        grid.clear();
        neighbours.clear();

        grid.add(new ArrayList<>());
        neighbours.add(new ArrayList<>());

        grid.get(0).add(new AtomicBoolean(false));
        neighbours.get(0).add(new AtomicInteger(0));

        cellCount.set(0);
    }

    @Override
    protected void incrementNeighboursAt(int x, int y){ neighbours.get(x).get(y).incrementAndGet(); }

    @Override
    public void resetNeighboursAt(int x, int y){
        neighbours.get(x).get(y).set(0);
    }


    public boolean[][] extractPattern() {
        getBoundingBox();

        boolean[][] pattern = new boolean[getBoundingBox()[1] - getBoundingBox()[0]][getBoundingBox()[1] - getBoundingBox()[0]];

        for (int i = 0; i < getBoundingBox()[1] - getBoundingBox()[0]; i++) {
            for (int j = 0; j < getBoundingBox()[1] - getBoundingBox()[0]; j++) {
                if (isCellAlive(i + getBoundingBox()[1] - getBoundingBox()[0], j + getBoundingBox()[1] - getBoundingBox()[0])) {
                    pattern[i][j] = isCellAlive(i + getBoundingBox()[1] - getBoundingBox()[0], j + getBoundingBox()[1] - getBoundingBox()[0]);
                }
            }
        }

        return pattern;
    }

    public String getPatternFromGrid(boolean[][] pattern) {
        return rleConverter(pattern);
    }

    private String rleConverter(boolean[][] pattern) {
        // initialize string builder for containing string - main string
        StringBuilder lineBuilder = new StringBuilder();

        // will contain alive positions - temporary container
        StringBuilder alive = new StringBuilder();

        // will contain dead positions - temporary container
        StringBuilder dead = new StringBuilder();

        // iterates column by column in pattern
        for (int i = 0; i < pattern.length; i++) {

            // initialize counters for alive and dead positions
            int aliveCounter = 0;
            int deadCounter = 0;

            // iterates row by row in pattern
            for (int j = 0; j < pattern[0].length; j++) {

                // check if position is true or false
                if (pattern[i][j]) {

                    // increment count for alive positions
                    aliveCounter++;

                    // reset dead count
                    deadCounter = 0;

                    // empty string containing dead elements
                    dead.delete(0, dead.length());

                    // appending 'o' for occupied
                    alive.append("o");

                    // check if number of alive positions detected, in sequence, is greater then 1
                    if (alive.length() > 1) {

                        // check if alive count is greater then two
                        if (aliveCounter > 2) {

                            // remove the two last characters from string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in alive with new number of alive positions in sequence
                            alive.replace(0, alive.length(), aliveCounter + "o");
                        }
                        else {
                            // remove last character string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in alive with number of alive positions in sequence
                            alive.replace(0, alive.length(), aliveCounter + "o");
                        }
                    }

                    // appends alive string to main string
                    lineBuilder.append(alive);

                } else {
                    // increment dead count
                    deadCounter++;

                    // reset alive count
                    aliveCounter = 0;

                    // empty string containing alive elements
                    alive.delete(0, dead.length());

                    // check that pattern row position is not end of row
                    if (j != pattern[0].length - 1) {
                        // append 'b' for empty
                        dead.append("b");
                    }

                    // check if dead positions detected, in sequence, is greater then 1
                    if (dead.length() > 1) {

                        // check if alive count is greater then two
                        if (deadCounter > 2) {

                            // remove the two last characters from string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in dead with new number of alive positions in sequence
                            dead.replace(0, dead.length(), deadCounter + "b");
                        }
                        else {
                            // remove last character string
                            lineBuilder.deleteCharAt(lineBuilder.length() - 1);

                            // replace content in alive with number of alive positions in sequence
                            dead.replace(0, dead.length(), deadCounter + "b");
                        }
                    }

                    // check if position is end of line
                    if (j != pattern[0].length - 1) {
                        // append dead string to main string
                        lineBuilder.append(dead);
                    }

                    // check if row contains only empty positions
                    if (deadCounter == pattern[0].length) {
                        // add line of empty positions
                        lineBuilder.append(dead);
                    }
                }
            }

            // delete alive and dead string - prepare for new row
            alive.delete(0, alive.length());
            dead.delete(0, dead.length());

            // check for end of pattern - last row reached
            if (i == pattern.length - 1) {

                // append at end of pattern according to RLE-format
                lineBuilder.append("!");
            }
            else {
                // indicates new pattern row - according to RLE-format
                lineBuilder.append("$");
            }
        }

        // return string of pattern in RLE-format
        return lineBuilder.toString();
    }

    public void injectPatternAt(int x, int y, boolean[][] pattern) {
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                if (pattern[i][j]) {
                    setCellAlive(i + y, j + x);
                }
            }
        }
    }
    //endregion
}
