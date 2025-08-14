package org.kaa.solver;

import lombok.extern.slf4j.Slf4j;
import org.kaa.model.*;
import org.kaa.solver.starters.MultiThreadStarter;
import org.kaa.storage.MultiThreadStorage;
import org.kaa.utils.IOUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Typhon
 * @since 23.11.2014
 * содержит всю логику решения
 */
@Slf4j
public class PuzzleSolver implements IPuzzleSolver {
	public static final String SOLUTION_FILE = "solutions.sol";

	public static final int SERIALIZATION_PACK_SIZE = 2000;
	private static final long POOL_LIMIT = 2000;
	private static final byte MAX_THREADS_COUNT = 4;
	private static final long BACKLOG_LIMIT = 5000;
	private MultiThreadStorage backLog = new MultiThreadStorage();

	private long maxPoolCount = 0;
	private long maxBackLogSize = 0;

	private Puzzle puzzle = null;

	private HashSet<Integer> hashes = new HashSet<>();
	private int duplications;
	private ProgressBar progressBar;

	@Override
	public SolutionInfo solve(Puzzle puzzle) {
		long startTime = System.currentTimeMillis();
		this.puzzle = puzzle;

		IOUtils.clear(SOLUTION_FILE);
		solvePuzzle(this.puzzle);
		showResults(puzzle.getSolutions());

		if (puzzle.getSolutions().size() == 1) {
			verifySolution(puzzle.getSolutions().get(0));
		}

		SolutionInfo solutionInfo = new SolutionInfo();
		log.info("Total time:" + (System.currentTimeMillis() - startTime));
		log.info("Max pool size:" + maxPoolCount);
		log.info("Max backlog size:" + maxBackLogSize);
		log.info("POOL LIMIT:" + POOL_LIMIT);
//		log.info("Variants:" + puzzle.getSolutions().size());
//		log.info("Unique variants:" + hashes.size());
//		log.info("Duplications:" + duplications);

		return solutionInfo;
	}

	private void verifySolution(Solution solution) {
		Set<CompactFigure> figures = solution.getCompactFigures();

		HashSet<Integer> unique = new HashSet<>();
		for (CompactFigure figure : figures) {
			for (Integer compactAtom : figure.getCompactAtoms()) {
				boolean add = unique.add(compactAtom);
				if (!add) {
					throw new RuntimeException("Incorrect solution: " + compactAtom);
				}
			}
		}
		log.info("Correct solution!");
	}

	private void solvePuzzle(Puzzle puzzle) {
		if (!puzzle.validate()) {
			return;
		}
		MultiThreadStarter.start(puzzle);
//		SingleThreadStarter.start(puzzle);

	}

	public void showResults(List<? extends RealSpace> solutions) {
		ResultPrinter printer = new ResultPrinter(puzzle);
		printer.print(solutions);
	}


}
