package org.kaa.storage;

import org.kaa.exceptions.OutOfUnitsException;
import org.kaa.model.RealSpace;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MultiThreadStorage implements CommonStorage<RealSpace> {
    private final ConcurrentLinkedDeque<RealSpace> items = new ConcurrentLinkedDeque<>();

    @Override
    public synchronized RealSpace getFirst() throws OutOfUnitsException {
        RealSpace item = items.getFirst();
        items.removeFirst();
        return item;
    }

    @Override
    public synchronized RealSpace getLast() {
        RealSpace item = items.getLast();
        items.removeLast();
        return item;
    }

    @Override
    public synchronized boolean add(RealSpace item) {
        return items.add(item);
    }

    @Override
    public synchronized boolean addAll(Collection<RealSpace> newItems) {
        return items.addAll(newItems);
    }

    @Override
    public synchronized int size() {
        return items.size();
    }
}
