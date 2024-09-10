package org.kaa.puzzle;

import org.junit.jupiter.api.Test;
import org.kaa.model.Puzzle;
import org.kaa.solver.PuzzleSolver;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public abstract class PuzzleTestCase {

    protected PuzzleSolver solver;

    @Test
    public final void testPuzzle(){
        prepare();
        process();
        validate();
    }

    protected abstract Puzzle getPuzzle();

    protected void prepare(){
        solver = new PuzzleSolver();
    }

    protected void process(){
        solver.solve(getPuzzle());
    };

    protected abstract void validate();

}
