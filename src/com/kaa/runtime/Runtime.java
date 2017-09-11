package com.kaa.runtime;

import com.kaa.model.Puzzle;
import com.kaa.puzzle.figures.Bend;
import com.kaa.puzzle.figures.Square;
import com.kaa.puzzle.figures.Zigzag;
import com.kaa.puzzle.spaces.*;
import com.kaa.solver.PuzzleSolver;

/**
 * @author Typhon
 * @since 24.11.2014
 */
public class Runtime {

    private Puzzle puzzle = new Puzzle();

    public Runtime() {
        puzzle5();
    }

    public Runtime(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void execute(){
        PuzzleSolver solver = new PuzzleSolver();
        solver.solve(puzzle);
    }

    private void puzzle1(){
        puzzle.setSpace(new CornerSpace());
        puzzle.setFigure(new Bend());
    }

    private void puzzle2(){
        puzzle.setSpace(new Cube542());
        puzzle.setFigure(new Zigzag());
    }

    private void puzzle3(){
        puzzle.setSpace(new Cube544());
        puzzle.setFigure(new Square());
    }

    private void puzzle4(){
        puzzle.setSpace(new Cube525());
        puzzle.setFigure(new Zigzag());
    }

    private void puzzle5(){
        puzzle.setSpace(new Cube());
        puzzle.setFigure(new Zigzag());
    }
}
