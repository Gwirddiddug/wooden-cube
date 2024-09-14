package org.kaa.puzzle;

import org.junit.jupiter.api.Test;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.puzzle.figures.Bend;
import org.kaa.puzzle.figures.Zigzag;
import org.kaa.puzzle.spaces.CommonCube;
import org.kaa.puzzle.spaces.Cube321;
import org.kaa.solver.PuzzleSolver;
import org.kaa.puzzle.figures.Teewee;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public class BaseTest {

    private final PuzzleSolver solver = new PuzzleSolver();

    @Test
    public void testPuzzle321Bend(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new Cube321());
        puzzle.setFigure(new Bend());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPuzzle552Zigzag(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,5,2));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testTeewee444(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(4,4,4));
        puzzle.setFigure(new Teewee());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

}
