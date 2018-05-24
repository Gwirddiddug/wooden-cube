package com.kaa.puzzle;

import com.kaa.model.Puzzle;
import com.kaa.puzzle.figures.Bend;
import com.kaa.puzzle.spaces.DoubleBend;

/**
 * Created by kopylov-aa on 25.09.2016.
 */
public class SimpleTest extends PuzzleTestCase {
    public Puzzle getPuzzle() {
        Puzzle puzzle = new Puzzle();
        puzzle.setSpace(new DoubleBend());
        puzzle.setFigure(new Bend());
        return puzzle;
    }

    @Override
    protected void validate() {

    }
}
