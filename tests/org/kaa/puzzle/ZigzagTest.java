package org.kaa.puzzle;

import org.junit.jupiter.api.Test;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.puzzle.figures.Zigzag;
import org.kaa.puzzle.spaces.CommonCube;
import org.kaa.solver.PuzzleSolver;
import org.kaa.solver.ResultPrinter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public class ZigzagTest {

    private final PuzzleSolver solver = new PuzzleSolver();

    @Test
    public void testPuzzle(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,5,2));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPuzzle544(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,4,4));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPuzzle553(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,5,3));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(0, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPuzzle552(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,5,2));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPuzzle554(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,5,4));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPuzzle555(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,5,5));
        puzzle.setFigure(new Zigzag());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPostures() {
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(5,4,2));
        puzzle.setFigure(new Zigzag());

//        assertEquals(6, puzzle.getPostures().size());

        ResultPrinter printer = new ResultPrinter(puzzle);
        printer.printFigures(puzzle.getPostures());
    }

}
