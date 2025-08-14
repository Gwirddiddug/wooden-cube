package org.kaa.solver;

import lombok.extern.slf4j.Slf4j;
import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
@Slf4j
public class ProgressBar {

	public static final int OUTPUT_INTERVAL = 1000; //pause in ms
	private final Puzzle puzzle;
	private long totalStepCount = 1;
	private long stepCount = 0;

	private int spaceCapacity = 1;
	private int progress = 0;
	private int postures = 0;

	private Thread logThread;
	private final Queue<String> logQueue = new ConcurrentLinkedQueue<>();


	public ProgressBar(Puzzle puzzle) {
		this.puzzle = puzzle;
		RealSpace space = puzzle.getSpace();
		this.postures = puzzle.getPostures().size();
		this.spaceCapacity = space.getAbscissus() * space.getOrdinatus() * space.getApplicata() /100;
	}


	public void output() {
		long currentProgress = progress / spaceCapacity;
		log.info("{}%\t{}\t{}", currentProgress, stepCount, totalStepCount);
//		logTask.run(); 231328620
	}

	public void setProgress(RealSpace variant) {
		this.progress = variant.getMaxPointKey();
		stepCount++;
	}

	public void setProgress(int progress) {
		this.progress = Math.max(this.progress, progress);
		stepCount++;
	}

	public void start() {
		logThread = new Thread(()->{
			while (true) {
				this.output();
				try {
					for (int i = 0; i < 10; i++) {
						Thread.yield();
						Thread.sleep(OUTPUT_INTERVAL/10);
						Thread.yield();
					}
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


	private final Runnable logTask = () -> {
		while (!logQueue.isEmpty()) {
			System.out.println(logQueue.poll());
		}
	};


	public void addMessage(String message) {
		logQueue.add(message);
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
}
