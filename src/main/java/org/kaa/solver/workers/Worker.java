package org.kaa.solver.workers;

import org.kaa.model.Figure;
import org.kaa.model.RealSpace;
import org.kaa.storage.MultiThreadStorage;

import java.util.List;

public abstract class Worker implements Runnable {

    protected final MultiThreadStorage backLog;
    protected final List<Figure> postures;
    protected int maxBackLogSize;

    public Worker(MultiThreadStorage backLog, List<Figure> postures) {
        this.backLog = backLog;
        this.postures = postures;
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
