package org.kaa.solver;

import lombok.extern.slf4j.Slf4j;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.storage.MultiThreadStorage;

@Slf4j
public abstract class Starter {

    protected final Puzzle puzzle;
    protected final MultiThreadStorage backlog;
    protected final ProgressBar progressBar;

    public Starter(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.backlog = new MultiThreadStorage();
        this.backlog.add(puzzle.getSpace());
        this.progressBar = new ProgressBar(puzzle);
    }

    protected abstract Solution run();
}
