package test;

import model.GameOfLife;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by remibengtpettersen on 12.02.2016.
 */
public class GameBoardTest {

    GameOfLife gol;

    @Before
    public void setUp() throws Exception {
        gol = new GameOfLife(5);
        gol.setGrid(new boolean[][]{
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, true, true, true, false},
                new boolean[]{false, false, false, false, false},
                new boolean[]{false, false, false, false, false}});
        gol.nextGeneration();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateGameBoard(){
        assertEquals(5, gol.getGrid().length, 0.1d);
        assertEquals(5, gol.getNeighbours().length, 0.1d);
    }

    public void testCountNeighbours(){


    }

    @Test
    public void testAggregateNeighbours(){

        assertArrayEquals(new byte[][]{
                new byte[]{0, 0, 0, 0, 0},
                new byte[]{1, 2, 3, 2, 1},
                new byte[]{1, 1, 2, 1, 1},
                new byte[]{1, 2, 3, 2, 1},
                new byte[]{0, 0, 0, 0, 0}}, gol.getNeighbours());
    }

    @Test
    public void testEvolve(){

    assertArrayEquals(new boolean[][]{
            new boolean[]{false, false, false, false, false},
            new boolean[]{false, false, true, false, false},
            new boolean[]{false, false, true, false, false},
            new boolean[]{false, false, true, false, false},
            new boolean[]{false, false, false, false, false}}, gol.getGrid());

}



}
