package org.kaa.solver.starters;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.WellDoneException;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.solver.workers.SingleWorker;

@Slf4j
public class SingleThreadStarter extends Starter {

    public SingleThreadStarter(Puzzle puzzle) {
        super(puzzle);
    }

    public static Solution start (Puzzle puzzle) {
        SingleThreadStarter instance = new SingleThreadStarter(puzzle);
        return instance.run();
    }

    protected Solution run() {
//		progressBar.start();
        try {
            SingleWorker worker = new SingleWorker(backlog, puzzle.getPostures());
            worker.run();
        } catch (WellDoneException e) {
            puzzle.setSolution(e.getSolution());
            log.info("Success!");
            return puzzle.getSolution();
        } finally {
            progressBar.stop();
        }
        return null;
    }
}
