package org.kaa.solver;

import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;

/**
 *
 */
public class ProgressBar {

    private long iterationTime = System.nanoTime();
    private Puzzle puzzle;
    private int step = 0;

    public ProgressBar(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void printProgress(PuzzleSolver.Variants variants) {
        long timestamp = System.nanoTime();
        String executionTime = String.valueOf(Math.round((timestamp - iterationTime)/1_000f) /1_000f);
//        float progress = countProgress(variants);
        System.out.print(String.format("\nstep#%s(%s ms)", step++, executionTime));

        iterationTime =  System.nanoTime();
    }

    private float countProgress(PuzzleSolver.Variants variants) {
        float summ = 0;
        int total = puzzle.getSpace().size();
        if (total==0) {
            return 1;
        }

        for (RealSpace variant : variants) {
            int filled = total - variant.countEmpty();
            summ += filled / (float)total;
        }
        return summ / variants.size();
    }
}
