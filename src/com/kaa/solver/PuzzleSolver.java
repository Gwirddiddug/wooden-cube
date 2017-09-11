package com.kaa.solver;

import com.kaa.model.*;
import com.kaa.utils.FigureUtils;
import com.kaa.utils.IOUtils;
import com.kaa.storage.BackLog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Typhon
 * @since 23.11.2014
 * содержит всю логику решения
 */
public class PuzzleSolver {
    public static final String SOLUTION_FILE = "solutions.sol";

    private static final long POOL_LIMIT = 21;
    private static final byte MAX_THREADS_COUNT = 7;

    private static final long BACKLOG_LIMIT = 5000;
    private static final long SERIALIZATION_PACK_SIZE = 1000;
    private BackLog backLog = new BackLog(BACKLOG_LIMIT, SERIALIZATION_PACK_SIZE);

    private long iterationTime = System.currentTimeMillis();
    private long maxPoolCount = 0;
    private long maxBackLogCount = 0;
    private final boolean SHOW_FIGURES = true;

    private Puzzle puzzle = null;

    public SolutionInfo solve(Puzzle puzzle) {
        long startTime = System.currentTimeMillis();
        this.puzzle = puzzle;

        IOUtils.clear(SOLUTION_FILE);
        solvePuzzle(this.puzzle);
        showResults(puzzle.getSolutions());

        SolutionInfo solutionInfo = new SolutionInfo();
        System.out.println("Variants:" + puzzle.getSolutions().size());
        System.out.println("Total time:" + String.valueOf(System.currentTimeMillis() - startTime));
        System.out.println("Max size:" + maxPoolCount);
        System.out.println("Max backlog size:" + maxBackLogCount);
        System.out.println("POOL LIMIT:" + POOL_LIMIT);

        return solutionInfo;
    }

    //итерационный метод: итерация - добавление очередной фигуры
    private void solvePuzzle(Puzzle puzzle) {
        validate(puzzle);

        Figure figure = puzzle.getFigure();
        RealSpace space = puzzle.getSpace();
        List<Figure> postures = FigureUtils.buildPostures(figure);

        backLog = new BackLog(BACKLOG_LIMIT, SERIALIZATION_PACK_SIZE);
        Variants variants = new Variants();
        variants.add(space);


//        executor.start();
        iterate(puzzle, postures, variants);

    }

    //перебор вариантов
    private void iterate(Puzzle puzzle, List<Figure> postures, Variants variants) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
        float progress = 0;
        while (variants.size() > 0) {
            String executionTime = String.valueOf(System.currentTimeMillis() - iterationTime);
            System.out.print(String.format("\nprogress#%s(%s ms)", progress, executionTime));
            iterationTime = System.currentTimeMillis();

            //добавляем ещё одну фигуру
            Variants newSpaces = new Variants();
            List<Future<Variants>> futureVariants = nextStep(puzzle, postures, variants, executor);
            for (Future<Variants> futureVariant : futureVariants) {
                checkVariant(newSpaces, futureVariant);
            }

            while (newSpaces.size() < POOL_LIMIT) {
                if (backLog.size() > 0) {
                    newSpaces.add(backLog.get());
                } else {
                    break;
                }
            }

            if (newSpaces.size() > 0) {
                variants = newSpaces;
            } else {
                break; //если нет, новых решений, то сохраняем последние полученные результаты
            }
        }
        executor.shutdown();
    }

    //пытаемся подставить фигуру
    private void checkVariant(Variants newSpaces, Future<Variants> futureVariant) {
        try {
            Variants futures = futureVariant.get();
            if (futures.size() > 0) {
                if ((newSpaces.size() + futures.size()) < POOL_LIMIT || newSpaces.size()==0) {
                    newSpaces.addAll(futures);
                } else {
                    backLog.addAll(futures);
                    if (maxBackLogCount < backLog.size()) {
                        maxBackLogCount = backLog.size();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * выполняет итерацию по переданному списку вариантов
     * @param puzzle
     * @param postures
     * @param variants
     * @param executor
     * @return
     */
    private List<Future<Variants>> nextStep(Puzzle puzzle, List<Figure> postures, Variants variants, ExecutorService executor) {
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

    private void validate(Puzzle puzzle) {
        if (puzzle.getSpace().size() % puzzle.getFigure().size() > 0) {
            System.out.println(String.format("unsolvable: %s and %s", puzzle.getSpace().size(), puzzle.getFigure().size()));
        }
    }

    public void showResults(List<? extends RealSpace> solutions) {
        System.out.println("\n==========================");
        System.out.println(String.format("Variants(%s):", solutions.size()));

        int index = 0;
        for (RealSpace solution : solutions) {
            index++;
            System.out.println("--------------------------");
            System.out.println(String.format("Variant#%s:", index));

            if (solution.countEmpty() > 0) {
                System.out.println(String.format("size:%s, empty:%s, filled:%s", solution.size(), solution.countEmpty(), solution.countFilled()));
            }

            if (SHOW_FIGURES) {
                int order = 0;
                for (Figure part : solution.figures()) {
                    order++;
                    System.out.println(String.format("Figure#%s:\n%s", order, part.getView()));
                }
            }
        }
    }

    private class IterateVariant implements Callable<Variants> {
        private List<Figure> postures;
        private RealSpace solution;

        public IterateVariant(List<Figure> postures, RealSpace solution) {
            this.postures = postures;
            this.solution = solution;
        }

        @Override
        public Variants call() throws Exception {
            Variants newSpaces = new Variants();
            for (Figure posture : postures) {
                List<RealSpace> newSolutions = solution.clone().allocateFigure(posture);
                newSpaces.addAll(newSolutions);
            }
            return newSpaces;
        }
    }

    private class Variant extends RealSpace {
        private Variant() {
        }

        private Variant(int cubeSize) {
            super(cubeSize);
        }
    }

    private class Allocator implements Callable<List<RealSpace>> {
        RealSpace solution;
        List<Figure> postures;

        public Allocator(RealSpace solution, List<Figure> postures) {
            this.solution = solution;
            this.postures = postures;
        }

        @Override
        public List<RealSpace> call() throws Exception {
            List<RealSpace> targetSpaces = new LinkedList<>();
            for (Figure posture : postures) {
                List<RealSpace> newSolutions = solution.clone().allocateFigure(posture);
                targetSpaces.addAll(newSolutions);
            }
            return targetSpaces;
        }

    }

    private class Variants extends LinkedList<RealSpace> {
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