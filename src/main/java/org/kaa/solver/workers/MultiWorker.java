package org.kaa.solver.workers;

import org.kaa.exceptions.WellDoneException;
import org.kaa.model.Figure;
import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;
import org.kaa.solver.IterateVariant;
import org.kaa.solver.Variants;
import org.kaa.storage.MultiThreadStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class MultiWorker extends Worker {

    private static final byte MAX_THREADS_COUNT = 24;
    private int maxPoolCount = 0;

    public MultiWorker(MultiThreadStorage backLog, List<Figure> postures) {
        super(backLog, postures);
    }

    @Override
    protected void iterate(RealSpace variant) {

    }


    //перебор вариантов в несколько потоков
    private void iterateMulti2(Puzzle puzzle) {

        backLog.add(puzzle.getSpace());
        List<Future<Long>> threads = new ArrayList<>();
        try {
            new IterateBacklog().call();
            ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
            for (int i = 0; i < MAX_THREADS_COUNT; i++) {
                threads.add(executor.submit(new IterateBacklog()));
            }

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
        } finally {
            System.out.println("backLog.size:" + backLog.size());
            System.out.println("threads size:" + threads.size());
        }
    }


    //перебор вариантов в несколько потоков
    private void iterateMulti(Puzzle puzzle) {
        Variants variants = new Variants();
        variants.add(puzzle.getSpace());

        int count = 0;
        int branches = 0;

        while (!variants.isEmpty()) {
            //добавляем ещё одну фигуру
            Variants newSpaces = new Variants();
            Variants futureVariants = singleStep(variants);

            checkVariant(newSpaces, futureVariants);

            if (newSpaces.isEmpty()) {
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



    private Variants singleStep(Variants variants) {
        boolean toBacklog = false;

        Variants newSpaces = new Variants();

        for (RealSpace variant : variants) {
            if (variant.size() == 0) {
                throw new WellDoneException(variant);
            }

            if (toBacklog) {
                backLog.add(variant);
                maxBackLogSize = Math.max(maxBackLogSize, backLog.size());
                continue;
            }
            toBacklog = true;

            IterateVariant iterateVariant = new IterateVariant(postures, variant);

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
                throw new WellDoneException(variant);
            }

            if (toBacklog) {
                backLog.add(variant);
                maxBackLogSize = Math.max(maxBackLogSize, backLog.size());
                continue;
            }
            toBacklog = true;

            IterateVariant iterateVariant = new IterateVariant(postures, variant);

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

    private void checkVariant(Variants newSpaces, Variants futures) {
//		removeDuplications(futures);
        if (futures.isEmpty()) return;
        if (newSpaces.isEmpty()) {
            newSpaces.add(futures.getFirst());
            futures.remove(0);
            backLog.addAll(futures);
        } else {
            backLog.addAll(futures);
            maxBackLogSize = Math.max(maxBackLogSize, backLog.size());
        }
    }
}
