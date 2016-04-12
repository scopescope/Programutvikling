package model.rules;

import model.EvolveException;

/**
 * The original Game of Life rule made by John Conway in 1970. Rulestring: B3/S23
 * Created on 12.02.2016.
 * This class provides the default rules for game of life as defined by Conway.
 */
public class ClassicRule extends Rule {

    /**
     * Constructor for the custom rule
     * @param grid reference to the cell grid to be evolved
     * @param neighbours reference to the neighbours grid used during evolution
     */
    public ClassicRule(boolean[][] grid, byte[][] neighbours){

        super(grid, neighbours);

        ruleText = "B3/S23";
    }


    @Override
    public void evolve() throws EvolveException {

        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){

                if (neighbours[x][y] < 0 || neighbours[x][y] > 8) {
                    throw new EvolveException();
                }

                // if a cell has 3 neighbours it wil become alive independent whether it's alive or dead
                else if (neighbours[x][y] == 3)
                    grid[x][y] = true;

                // if a cell has 2 neighbours it should either stay alive or stay dead, else it should die.
                else if (neighbours[x][y] != 2)
                    grid[x][y] = false;

                neighbours[x][y] = 0;
            }
        }
    }
}
