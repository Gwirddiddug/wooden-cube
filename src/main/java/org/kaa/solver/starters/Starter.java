package org.kaa.solver.starters;

import lombok.extern.slf4j.Slf4j;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.solver.ProgressBar;
import org.kaa.storage.MultiThreadStorage;
import org.kaa.storage.RealSpaceStorage;

@Slf4j
public abstract class Starter {

    protected final Puzzle puzzle;
    protected final RealSpaceStorage backlog;
    protected final ProgressBar progressBar;

    public Starter(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.backlog = new MultiThreadStorage();
        this.backlog.add(puzzle.getSpace());
        this.progressBar = new ProgressBar(puzzle);
    }

    public Starter(Puzzle puzzle, RealSpaceStorage backlog) {
        this.puzzle = puzzle;
        this.backlog = backlog;
        this.backlog.add(puzzle.getSpace());
        this.progressBar = new ProgressBar(puzzle);
    }

    protected abstract Solution run();
}
