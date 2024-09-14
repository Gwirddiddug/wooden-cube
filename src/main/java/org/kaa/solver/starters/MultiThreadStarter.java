package org.kaa.solver.starters;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.WellDoneException;
import org.kaa.model.Puzzle;
import org.kaa.model.Solution;
import org.kaa.solver.Starter;
import org.kaa.solver.workers.SingleWorker;

@Slf4j
public class MultiThreadStarter extends Starter {

    public MultiThreadStarter(Puzzle puzzle) {
        super(puzzle);
    }

    public static Solution start (Puzzle puzzle) {
        MultiThreadStarter instance = new MultiThreadStarter(puzzle);
        return instance.run();
    }

    protected Solution run() {
//		progressBar.start();
        SingleWorker worker = new SingleWorker(backlog, puzzle.getPostures());
        Thread workerThread = new Thread(worker);
        try {
			workerThread.start();
            workerThread.join();
        } catch (WellDoneException e) {
            puzzle.setSolution(e.getSolution());
            log.info("Success!");
            return puzzle.getSolution();

		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            progressBar.stop();
        }
        return null;
    }
}
