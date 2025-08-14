package org.kaa.solver.workers;

import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.solver.ProgressBar;
import org.kaa.storage.CommonStorage;

import java.util.List;

public abstract class Worker implements Runnable {

    protected final CommonStorage<RealSpace> backLog;
    protected final List<Figure> postures;
    protected final ProgressBar progressBar;
    protected int maxBackLogSize;
    protected final int threshold;

    public Worker(CommonStorage<RealSpace> backLog, List<Figure> postures) {
        this(backLog, postures, null);
    }

    public Worker(CommonStorage<RealSpace> backLog, List<Figure> postures, ProgressBar progressBar) {
        this.backLog = backLog;
        this.postures = postures;
        this.progressBar = progressBar;
        if (!postures.isEmpty()) {
            this.threshold = postures.getFirst().size()/2;
        } else {
            this.threshold = 255;
        }
    }

    @Override
    public void run() {
        RealSpace variant = backLog.getLast();
        if (variant != null) {
            iterate(variant);
        }
    }

    protected abstract void iterate(RealSpace variant);
}
