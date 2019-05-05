package org.kaa.puzzle;

import org.junit.Test;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.puzzle.figures.Bend;
import org.kaa.puzzle.figures.Zigzag;
import org.kaa.puzzle.spaces.CommonCube;
import org.kaa.puzzle.spaces.Cube321;
import org.kaa.solver.PuzzleSolver;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public class SimpleTest {

    private PuzzleSolver solver = new PuzzleSolver();

    @Test
    public void testPuzzle321Bend(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new Cube321());
        puzzle.setFigure(new Bend());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals("Wrong solutions count", 2, solutions.size());
    }

    @Test
    public void testPuzzle322Bend(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,4,2));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals("Wrong solutions count", 8, solutions.size());
    }
}
