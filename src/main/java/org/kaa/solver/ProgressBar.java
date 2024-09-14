package org.kaa.solver;

import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class ProgressBar {

	public static final int OUTPUT_INTERVAL = 5000; //pause in ms
	private long iterationTime = System.nanoTime();
	private final Puzzle puzzle;
	private int step = 0;

	private int progress = 0;
	private final Queue<String> logQueue = new ConcurrentLinkedQueue<>();

	private final Runnable logTask = () -> {
        while (!logQueue.isEmpty()) {
            System.out.println(logQueue.poll());
        }
    };
	private Thread logThread;
    private int spaceCapacity = 1;


    public void addMessage(String message) {
		logQueue.add(message);
	}

	public ProgressBar(Puzzle puzzle) {
		this.puzzle = puzzle;
		RealSpace space = puzzle.getSpace();

		this.spaceCapacity = space.getAbscissus() * space.getOrdinatus() * space.getApplicata() / puzzle.getFigure().size();
	}

	public void printProgress(Variants variants) {
		long timestamp = System.nanoTime();
		String executionTime = String.valueOf(Math.round((timestamp - iterationTime) / 1_000f) / 1_000f);

		System.out.print(String.format("\nstep#%s(%s ms)", step++, executionTime));

		iterationTime = System.nanoTime();
	}

	private float countProgress(Variants variants) {
		float summ = 0;
		int total = puzzle.getSpace().size();
		if (total == 0) {
			return 1;
		}

		for (RealSpace variant : variants) {
			int filled = total - variant.countEmpty();
			summ += filled / (float) total;
		}
		return summ / variants.size();
	}

	public void output() {
		int currentProgress = (progress) * 100 / spaceCapacity;
		System.out.println(currentProgress + "%");
//		logTask.run();
	}

	public void setProgress(int progress) {
		this.progress = Math.max(this.progress, progress);
	}

	public void start() {
		logThread = new Thread(()->{
			while (true) {
				this.output();
				try {
					Thread.yield();
					Thread.sleep(OUTPUT_INTERVAL);
					Thread.yield();
				} catch (InterruptedException e) {
					break;
				}
			}
		});
		logThread.start();
	}

	public void stop() {
		if (logThread!=null && logThread.isAlive()) {
			logThread.interrupt();
		}
	}
}
