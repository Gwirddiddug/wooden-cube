package org.kaa.puzzle;

import org.kaa.model.Puzzle;
import org.kaa.solver.PuzzleSolver;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public abstract class PuzzleTestCase extends TestCase {

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
