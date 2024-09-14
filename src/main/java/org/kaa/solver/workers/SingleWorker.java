package org.kaa.solver.workers;

import org.kaa.exceptions.WellDoneException;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.solver.IterateVariant;
import org.kaa.solver.Variants;
import org.kaa.storage.MultiThreadStorage;

import java.util.List;

public class SingleWorker extends Worker {

    private IterateVariant iterateVariant;

    public SingleWorker(MultiThreadStorage backLog, List<Figure> postures) {
        super(backLog, postures);
        iterateVariant = new IterateVariant(postures);
    }

    @Override
    public void run() {
        RealSpace variant = backLog.getLast();
        if (variant != null) {
            iterate(variant);
        }
    }

    //перебор вариантов
    protected void iterate(RealSpace variant) {
        do {
            //добавляем ещё одну фигуру, выбираем первый из вариантов, остальные скидываем в бэклог
            //если зашли в тупик, берём из бэклога
            variant = singleStep(variant);

//          progressBar.setProgress(newSpaces.getFirst().getCompactFigures().size());
        } while (variant != null);
    }

    private RealSpace singleStep(RealSpace variant) {
        if (variant.size() == 0) {
            throw new WellDoneException(variant);
        }

        iterateVariant.setSolution(variant);
        Variants call = iterateVariant.call();

        return checkVariant(call);
    }

    private RealSpace checkVariant(Variants futures) {
        RealSpace theOne;
        if (futures.isEmpty()) {
            theOne = backLog.getLast();
        } else {
            theOne = futures.getFirst();
            futures.remove(0);
            backLog.addAll(futures);
            maxBackLogSize = Math.max(maxBackLogSize, backLog.size());
        }

        return theOne;
    }
}
