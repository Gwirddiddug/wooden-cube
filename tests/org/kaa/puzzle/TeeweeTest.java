package org.kaa.puzzle;

import org.junit.jupiter.api.Test;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.puzzle.figures.Teewee;
import org.kaa.puzzle.spaces.CommonCube;
import org.kaa.solver.PuzzleSolver;
import org.kaa.solver.ResultPrinter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public class TeeweeTest {

    private final PuzzleSolver solver = new PuzzleSolver();

    @Test
    public void testTeewee444(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(4,4,6));
        puzzle.setFigure(new Teewee());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testPostures() {
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(4,4,4));
        puzzle.setFigure(new Teewee());

//        assertEquals(6, puzzle.getPostures().size());

        ResultPrinter printer = new ResultPrinter(puzzle);
        printer.printFigures(puzzle.getPostures());
    }


    @Test
    public void testTeewee446(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(4,4,6));
        puzzle.setFigure(new Teewee());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testTeewee663(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(6,6,3));
        puzzle.setFigure(new Teewee());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }

    @Test
    public void testTeewee666(){
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new CommonCube(6,6,6));
        puzzle.setFigure(new Teewee());

        solver.solve(puzzle);

        List<Solution> solutions = puzzle.getSolutions();
        assertEquals(1, solutions.size(), "Wrong solutions count");
    }
}
