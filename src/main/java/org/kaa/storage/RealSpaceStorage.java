package org.kaa.storage;

import lombok.Getter;
import org.kaa.model.RealSpace;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class RealSpaceStorage implements CommonStorage<RealSpace> {
    protected int maxBacklogSize = 0;
    protected RealSpace solution = null;
    protected HashSet<RealSpace> solutions = new HashSet<>();
    protected final AtomicBoolean isLocked = new AtomicBoolean();

    @Override
    public void saveSolution(RealSpace solution) {
        this.solution = solution;
        this.solutions.add(solution);
    }
}
