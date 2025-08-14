package org.kaa.solver.starters;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.WellDoneException;
import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;
import org.kaa.model.Solution;
import org.kaa.solver.workers.MultiThreadWorker;
import org.kaa.storage.HashStorage;

import java.util.HashSet;

@Slf4j
public class MultiThreadStarter extends Starter {

    private static final int MAX_TREAD_POOL_SIZE = 20;
    private int threadPoolSize = 0;

    public MultiThreadStarter(Puzzle puzzle) {
//        super(puzzle, new OptimizedHashStorage(puzzle.getSpace().size()/puzzle.getFigure().size()));
        super(puzzle, new HashStorage());
//        super(puzzle, new SortedStorage());
    }

    public static Solution start (Puzzle puzzle) {
        MultiThreadStarter instance = new MultiThreadStarter(puzzle);
        return instance.run();
    }

    protected Solution run() {
//		progressBar.start();
        HashSet<Thread> threads = new HashSet<>();

        MultiThreadWorker worker = new MultiThreadWorker(backlog, puzzle.getPostures(), progressBar);
        Thread workerThread = new Thread(worker);
        threads.add(workerThread);

        try {
            workerThread.start();
            while (workerThread.isAlive() && threadPoolSize < MAX_TREAD_POOL_SIZE) {
                if (backlog.size() > 1) {
                    Thread additionalThread = new Thread(new MultiThreadWorker(backlog, puzzle.getPostures(), progressBar));
                    threads.add(additionalThread);
                    additionalThread.start();
                    threadPoolSize = threads.size();
                }
            }

            for (Thread thread : threads) {
                thread.join();
            }
        } catch (WellDoneException e) {
            puzzle.setSolution(e.getSolution());
            log.info("Success!");
            return puzzle.getSolution();

		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            progressBar.stop();
        }
        log.info("MaxBacklogSize: {}", backlog.getMaxBacklogSize());
        RealSpace solution = backlog.getSolution();
        if (solution != null) {
            puzzle.setSolution(new Solution(solution));
            return puzzle.getSolution();
        } else {
            return null;
        }
    }
}
