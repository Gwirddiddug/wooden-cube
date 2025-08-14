package org.kaa.storage;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.OutOfUnitsException;
import org.kaa.model.RealSpace;

import java.util.*;

@Slf4j
public class HashStorage extends RealSpaceStorage {
    private final LinkedHashSet<RealSpace> items = new LinkedHashSet<>();

//    private final ConcurrentLinkedDeque<RealSpace> items = new ConcurrentLinkedDeque<>();

    @Override
    public synchronized RealSpace getFirst() throws OutOfUnitsException {
        log.debug("getFirst");
        RealSpace item = items.getFirst();
        items.removeFirst();
        return item;
    }

    @Override
    public synchronized RealSpace getLast() {
        log.debug("getLast");
        if (items.isEmpty()) return null;
        RealSpace item = items.getLast();
        items.removeLast();
        return item;
    }

    @Override
    public synchronized boolean add(RealSpace item) {
        log.debug("add");
        if (isLocked.get()) {
            return false;
        }
        boolean added = items.add(item);
        maxBacklogSize = Math.max(maxBacklogSize, items.size());
        return added;
    }

    @Override
    public synchronized boolean addAll(Collection<RealSpace> newItems) {
        log.debug("addAll");
        if (isLocked.get()) {
            return false;
        }
        boolean added = items.addAll(newItems);
        maxBacklogSize = Math.max(maxBacklogSize, items.size());
        return added;
    }

    @Override
    public synchronized int size() {
        return items.size();
    }


    @Override
    public void clear() {
        isLocked.set(true);
        items.clear();
    }
}
