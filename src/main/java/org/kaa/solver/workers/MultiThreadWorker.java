package org.kaa.solver.workers;

import lombok.extern.slf4j.Slf4j;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.model.Solution;
import org.kaa.solver.IterateVariant;
import org.kaa.solver.ProgressBar;
import org.kaa.solver.Variants;
import org.kaa.storage.RealSpaceStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
public class MultiThreadWorker extends Worker {

    private final IterateVariant iterateVariant;
    private Solution solution = null;
    private LinkedHashSet<RealSpace> cache = new LinkedHashSet<>();

    public MultiThreadWorker(RealSpaceStorage backLog, List<Figure> postures, ProgressBar progressBar) {
        super(backLog, postures, progressBar);
        iterateVariant = new IterateVariant(postures);
    }

    @Override
    public void run() {
        RealSpace variant = backLog.getLast();
        if (variant != null) {
            log.info("Thread started");
            iterate(variant);
        } else {
            log.info("Thread rejected");
        }
    }

    //перебор вариантов
    protected void iterate(RealSpace variant) {
        do {
//            progressBar.setProgress(variant.getLevel());
            //добавляем ещё одну фигуру, выбираем первый из вариантов, остальные скидываем в бэклог
            //если зашли в тупик, берём из бэклога
            variant = singleStep(variant);
        } while (variant != null);
        log.warn("Fail");
    }

    private RealSpace singleStep(RealSpace variant) {
        if (variant.size() == 0) {
            successComplete(variant);
            return null;
        }

        Variants variants = iterateVariant.allocate(variant);
        return checkVariant(variants);
    }

    private void successComplete(RealSpace variant) {
        solution = new Solution(variant);
        backLog.clear();
        backLog.saveSolution(variant);
        log.info("Job is done");
    }

    private RealSpace checkVariant(Variants futures) {
        RealSpace theOne;
        if (futures.isEmpty()) {
            theOne = backLog.getLast();
        } else {
            theOne = futures.getFirst();
            futures.removeFirst();
            backLog.addAll(futures);
//        maxBackLogSize = Math.max(maxBackLogSize, backLog.size());
        }

        return theOne;
    }

}
