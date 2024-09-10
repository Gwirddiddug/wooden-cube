package org.kaa.solver;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.WellDoneException;
import org.kaa.model.*;
import org.kaa.storage.BackLog;
import org.kaa.utils.IOUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Typhon
 * @since 23.11.2014
 * содержит всю логику решения
 */
@Slf4j
public class PuzzleSolver implements IPuzzleSolver {
	public static final String SOLUTION_FILE = "solutions.sol";

	public static final int FIXED_MEASURE = 5;
	public static final int SERIALIZATION_PACK_SIZE = 2000;
	public static final int MON_INTERVAL = 25000;
	private static final long POOL_LIMIT = 2000;
	private static final byte MAX_THREADS_COUNT = 24;
	private static final long BACKLOG_LIMIT = 5000;
	private BackLog backLog = new BackLog(BACKLOG_LIMIT, SERIALIZATION_PACK_SIZE, null);

	private long maxPoolCount = 0;
	private long maxBackLogCount = 0;

	private Puzzle puzzle = null;

	private HashSet<Integer> hashes = new HashSet<>();
	private int duplications;

	@Override
	public SolutionInfo solve(Puzzle puzzle) {
		long startTime = System.currentTimeMillis();
		this.puzzle = puzzle;

		IOUtils.clear(SOLUTION_FILE);
		solvePuzzle(this.puzzle);
		showResults(puzzle.getSolutions());

		assert puzzle.getSolutions().size() == 1;
		verifySolution(puzzle.getSolutions().get(0));

		SolutionInfo solutionInfo = new SolutionInfo();
//		log.info("Variants:" + puzzle.getSolutions().size());
		log.info("Total time:" + (System.currentTimeMillis() - startTime));
		log.info("Max size:" + maxPoolCount);
		log.info("Max backlog size:" + maxBackLogCount);
		log.info("POOL LIMIT:" + POOL_LIMIT);
		log.info("Unique variants:" + hashes.size());
		log.info("Duplications:" + duplications);

		return solutionInfo;
	}

	private void verifySolution(Solution solution) {
		Set<Figure> figures = solution.figures();
		HashSet<Integer> unique = new HashSet<>();
		for (Figure figure : figures) {
			Set<Integer> compactAtoms = figure.getCompactAtoms();
			for (Integer compactAtom : compactAtoms) {
				boolean add = unique.add(compactAtom);
				if (!add) {
					throw new RuntimeException("Incorrect solution: " + compactAtom);
				}
			}
		}
		log.info("Correct solution!");
	}

	//итерационный метод: итерация - добавление очередной фигуры
	private void solvePuzzle(Puzzle puzzle) {
		if (!puzzle.validate()) {
			return;
		}

		RealSpace space = puzzle.getSpace();
		backLog = new BackLog(BACKLOG_LIMIT, SERIALIZATION_PACK_SIZE, space.buildClone());
//        executor.start();
		try {
			iterate(puzzle);
//			iterateMulti2(puzzle);
		} catch (WellDoneException e) {
//      System.out.println("Success:\n\t" + e.getSolution().getTextView());
			log.info("Success!");
		}
	}

	//перебор вариантов в несколько потоков
	private void iterateMulti(Puzzle puzzle) {
		Variants variants = new Variants();
		variants.add(puzzle.getSpace());

		int count = 0;
		int branches = 0;

		while (true) {
			//добавляем ещё одну фигуру
			Variants newSpaces = new Variants();
			Variants futureVariants = singleStep(variants);

			checkVariant(newSpaces, futureVariants);

			if (newSpaces.isEmpty()) {
				while (backLog.size() > 0 && newSpaces.size()<MAX_THREADS_COUNT) {
					newSpaces.add(backLog.getLast());
				}
			}

			maxPoolCount = Math.max(maxPoolCount, newSpaces.size());
			if (!newSpaces.isEmpty()) {
				variants = newSpaces;
			} else {
				break; //если нет, новых решений, то сохраняем последние полученные результаты
			}
		}
	}


	//перебор вариантов в несколько потоков
	private void iterateMulti2(Puzzle puzzle) {

		backLog.add(puzzle.getSpace());
		new IterateBacklog().call();
		try {
			ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
			List<Future<Long>> threads = new ArrayList<>();
			for (int i = 0; i < MAX_THREADS_COUNT; i++) {
				threads.add(executor.submit(new IterateBacklog()));
			}
			//если нет, новых решений, то сохраняем последние полученные результаты
            do {
                Iterator<Future<Long>> threadsIterator = threads.iterator();
                while (threadsIterator.hasNext()) {
                    Future<Long> thread = threadsIterator.next();
                    if (thread.isDone()) {
                        System.out.println("Thread complete: " + thread.get());
                        threadsIterator.remove();
                    }
                }
            } while (backLog.size() != 0 || !threads.isEmpty());
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private class IterateBacklog implements Callable<Long> {
		@Override
		public Long call() {
			long count = 0;
			Variants variants = new Variants();
			RealSpace variant = backLog.getLast();
			if (variant == null) {
				return count;
			} else {
				variants.add(variant);
			}

			while (true) {
				//добавляем ещё одну фигуру
				Variants newSpaces = new Variants();
				Variants futureVariants = singleStep(variants);

				checkVariant(newSpaces, futureVariants);

				if (newSpaces.isEmpty()) {
					while (backLog.size() > 0 && newSpaces.size()<MAX_THREADS_COUNT) {
						newSpaces.add(backLog.getLast());
					}
				}

				maxPoolCount = Math.max(maxPoolCount, newSpaces.size());
				if (!newSpaces.isEmpty()) {
					variants = newSpaces;
				} else {
					break; //если нет, новых решений, то сохраняем последние полученные результаты
				}
			}

			return count;
		}
	}

	//перебор вариантов
	private void iterate(Puzzle puzzle) {
		Variants variants = new Variants();
		variants.add(puzzle.getSpace());

		ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS_COUNT);

		int count = 0;
		int branches = 0;


		while (!variants.isEmpty()) {
			//добавляем ещё одну фигуру
			Variants newSpaces = new Variants();
			Variants futureVariants = singleStep(variants);

			checkVariant(newSpaces, futureVariants);

			if (newSpaces.isEmpty()) {
/*				if (++count == MON_INTERVAL) {
					count = 0;
					log.info("branches = " + ++branches * MON_INTERVAL);
				}*/
				if (backLog.size() > 0) {
					newSpaces.add(backLog.getLast());
				}
			}

			maxPoolCount = Math.max(maxPoolCount, newSpaces.size());
			if (!newSpaces.isEmpty()) {
				variants = newSpaces;
			} else {
				break; //если нет, новых решений, то сохраняем последние полученные результаты
			}
		}
	}

	//пытаемся подставить фигуру
	private void checkVariant(Variants newSpaces, Variants futures) {
//		removeDuplications(futures);
		if (newSpaces.isEmpty()) {
			newSpaces.addAll(futures);
		} else {
			backLog.addAll(futures);
			maxBackLogCount = Math.max(maxBackLogCount, backLog.size());
		}
	}

	private void removeDuplications(Variants futures) {
		Iterator<RealSpace> iterator = futures.iterator();
		while (iterator.hasNext()) {
			RealSpace future = iterator.next();
			if (!hashes.add(future.getHash())) {
				iterator.remove();
				duplications++;
//        log.info("Duplicate: " + hash);
			}
		}
	}

/*
	private void checkForDuplication(Variants futures) {
		for (RealSpace future : futures) {
			int hash = future.getHash();
			boolean add = hashes.add(hash);
			if (!add) {
				duplications++;
//        log.info("Duplicate: " + hash);
			}
		}
	}
*/

	/**
	 * выполняет итерацию по переданному списку вариантов
	 */
/*
	private List<Future<Variants>> nextStep(Puzzle puzzle, Variants variants, ExecutorService executor) {
		List<Future<Variants>> futureVariants = new ArrayList<>();

		List<Figure> postures = puzzle.getPostures();
		for (RealSpace variant : variants) {
			if (variant.size() == 0) {
				Solution solution = new Solution(variant);
				puzzle.addSolution(solution);
			} else {
				//создаём объект для выполнения в отдельном потоке
				Callable<Variants> iterateResults = new IterateVariant(postures, variant);
				Future<Variants> iterateVariant = executor.submit(iterateResults);
				futureVariants.add(iterateVariant);
			}
		}
		return futureVariants;
	}
*/


	private Variants singleStep(RealSpace variant) {
		IterateVariant iterateVariant = new IterateVariant(puzzle.getPostures(), variant);
		Variants call = iterateVariant.call();

		for (RealSpace newVariant : call) {
			if (newVariant.size() == 0) {
				puzzle.addSolution(new Solution(variant));
				throw new WellDoneException(variant);
			}
		}

		return call;
	}


	private Variants singleStep(Variants variants) {
		boolean toBacklog = false;

		Variants newSpaces = new Variants();

		for (RealSpace variant : variants) {
			if (variant.size() == 0) {
				puzzle.addSolution(new Solution(variant));
				throw new WellDoneException(variant);
			}

			if (toBacklog) {
				backLog.add(variant);
				maxBackLogCount = Math.max(maxBackLogCount, backLog.size());
				continue;
			}
			toBacklog = true;

			IterateVariant iterateVariant = new IterateVariant(puzzle.getPostures(), variant);

			Variants call = iterateVariant.call();
			newSpaces.addAll(call);
		}

		return newSpaces;
	}



	private Variants singleStepMulti(Variants variants, ExecutorService executor) {
		boolean toBacklog = false;
		Variants newSpaces = new Variants();
		HashSet<Future<Variants>> futures = new HashSet<>();

		for (RealSpace variant : variants) {
			if (variant.size() == 0) {
				puzzle.addSolution(new Solution(variant));
				throw new WellDoneException(variant);
			}

			if (toBacklog) {
				backLog.add(variant);
				maxBackLogCount = Math.max(maxBackLogCount, backLog.size());
				continue;
			}
			toBacklog = true;

			IterateVariant iterateVariant = new IterateVariant(puzzle.getPostures(), variant);

			Future<Variants> submit = executor.submit(iterateVariant);
			futures.add(submit);
		}

		while (!futures.isEmpty()) {
			Iterator<Future<Variants>> iterator = futures.iterator();
			while (iterator.hasNext()) {
				Future<Variants> future = iterator.next();
				if (future.isDone()) {
					try {
						newSpaces.addAll(future.get());
						iterator.remove();
					} catch (InterruptedException | ExecutionException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		return newSpaces;
	}

 /* private Mono<List<Variants>> nextStep2(Puzzle puzzle, List<Figure> postures, Variants variants, ExecutorService executor) {
    List<Future<Variants>> futureVariants = new ArrayList<>();

    for (RealSpace variant : variants) {
      if (variant.size() == 0) {
        Solution solution = new Solution(variant);
        puzzle.addSolution(solution);
      } else {
        //создаём объект для выполнения в отдельном потоке
        Callable<Variants> iterateResults = new IterateVariant(postures, variant);
        Future<Variants> iterateVariant = executor.submit(iterateResults);
        futureVariants.add(iterateVariant);
      }
    }
    return futureVariants;
  }
*/

	public void showResults(List<? extends RealSpace> solutions) {
		ResultPrinter printer = new ResultPrinter(puzzle);
		printer.print(solutions);
	}

/*
	private boolean validate(Puzzle puzzle) {
		if (puzzle.getSpace().size() % puzzle.getFigure().size() > 0) {
			log.info("unsolvable: {} and {}", puzzle.getSpace().size(), puzzle.getFigure().size());
			return false;
		}
		return true;
	}
*/

	private class IterateVariant implements Callable<Variants> {
		private List<Figure> postures;
		private RealSpace solution;

		public IterateVariant(List<Figure> postures, RealSpace solution) {
			this.postures = postures;
			this.solution = solution;
		}

		@Override
		public Variants call() {
			long start = System.currentTimeMillis();

			Variants newSpaces = new Variants();
			for (Figure posture : postures) {
				List<RealSpace> newSolutions = solution.allocateFigure(posture);
				newSpaces.addAll(newSolutions);
			}
//			System.out.println(System.currentTimeMillis() - start);
			return newSpaces;
		}
	}

/*
	private class Variant extends RealSpace {

		private Variant(int cubeSize) {
			super(cubeSize);
		}
	}
*/

/*
	private class Allocator implements Callable<List<RealSpace>> {
		RealSpace solution;
		List<Figure> postures;

		public Allocator(RealSpace solution, List<Figure> postures) {
			this.solution = solution;
			this.postures = postures;
		}

		@Override
		public List<RealSpace> call() {
			List<RealSpace> targetSpaces = new LinkedList<>();
			for (Figure posture : postures) {
				List<RealSpace> newSolutions = solution.buildClone().allocateFigure(posture);
				targetSpaces.addAll(newSolutions);
			}
			return targetSpaces;
		}

	}
*/

	static class Variants extends LinkedList<RealSpace> {
  /*      @Override
        public boolean add(RealSpace space) {
            if (!isIn(space)){
                return super.add(space);
            }
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends RealSpace> c) {

            boolean added = true;
            for (RealSpace realSpace : c) {
                added &= add(realSpace);
            }
            return added;
        }*/
/*
        private boolean isIn(RealSpace space){
            if (space.size()==0) return false;
            for (RealSpace space2 : this) {
                if (space.equals(space2)) {
                    return true;
                }
            }
            return false;
        }*/
	}
}
