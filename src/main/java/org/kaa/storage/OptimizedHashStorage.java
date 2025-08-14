package org.kaa.storage;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.OutOfUnitsException;
import org.kaa.model.RealSpace;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OptimizedHashStorage extends RealSpaceStorage {
//    private final LinkedHashSet<RealSpace> items = new LinkedHashSet<>();
    private final HashMap<Integer, LinkedHashSet<RealSpace>> itemsIndex = new HashMap<>();
    private final AtomicInteger maxLevel = new AtomicInteger(0);
    private final AtomicInteger backlogSize = new AtomicInteger(0);

    public OptimizedHashStorage(int spaceSizeInFigures) {
        super();
        initStorage(spaceSizeInFigures);
    }

    public void initStorage(int spaceSizeInFigures) {
        for (int i = 0; i <= spaceSizeInFigures; i++) {
            itemsIndex.put(i, new LinkedHashSet<>());
        }
    }

    @Override
    public synchronized RealSpace getFirst() throws OutOfUnitsException {
        throw new UnsupportedOperationException("getFirst is unused method");
    }

    @Override
    public synchronized RealSpace getLast() {
        log.debug("getLast");

        LinkedHashSet<RealSpace> highestVariants = itemsIndex.get(maxLevel.get());
        while (highestVariants.isEmpty() && maxLevel.get() > 0) {
            highestVariants = itemsIndex.get(maxLevel.decrementAndGet());
        }
        if (highestVariants.isEmpty()) return null;
        RealSpace item = highestVariants.getLast();
        highestVariants.removeLast();
        backlogSize.decrementAndGet();;
        return item;
    }

    @Override
    public synchronized boolean add(RealSpace item) {
        log.debug("add");
        if (isLocked.get()) {
            return false;
        }
        LinkedHashSet<RealSpace> spaces = itemsIndex.get(item.getLevel());
        if (maxLevel.get() < item.getLevel()) {
            maxLevel.set(item.getLevel());
        }
        boolean added = spaces.add(item);
        backlogSize.incrementAndGet();

        return added;
    }

    @Override
    public synchronized boolean addAll(Collection<RealSpace> newItems) {
        log.debug("addAll");
        if (isLocked.get()) {
            return false;
        }
        for (RealSpace item : newItems) {
            add(item);
        }
        return true;
    }

    @Override
    public synchronized int size() {
        return backlogSize.get();
    }

    @Override
    public void clear() {
        isLocked.set(true);
        backlogSize.set(0);
    }
}
