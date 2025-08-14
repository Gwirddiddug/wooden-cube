package org.kaa.storage;

import lombok.extern.slf4j.Slf4j;
import org.kaa.exceptions.OutOfUnitsException;
import org.kaa.model.RealSpace;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class SortedStorage extends RealSpaceStorage {
    private final Comparator<? super RealSpace> comparator = new RealSpaceComparator();
    private final ConcurrentSkipListSet<RealSpace> items = new ConcurrentSkipListSet<>(comparator);

    @Override
    public synchronized RealSpace getFirst() throws OutOfUnitsException {
        log.debug("getFirst");
        if (items.isEmpty()) return null;
        RealSpace item = items.getFirst();
        items.removeFirst();
        return item;
    }

    @Override
    public synchronized RealSpace getLast() {
        log.debug("getLast");
        if (items.isEmpty()) return null;
        RealSpace item = items.getLast();
        items.remove(item);
        return item;
    }

    @Override
    public boolean add(RealSpace item) {
        log.debug("add");
        if (isLocked.get()) {
            return false;
        }
        return items.add(item);
    }

    @Override
    public boolean addAll(Collection<RealSpace> newItems) {
        log.debug("addAll");
        if (isLocked.get()) {
            return false;
        }
        return items.addAll(newItems);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public void clear() {
        isLocked.set(true);
        items.clear();
    }

    private static class RealSpaceComparator implements Comparator<RealSpace> {
        @Override
        public int compare(RealSpace o1, RealSpace o2) {
            if (o2.getLevel() == o1.getLevel()) {
                return Integer.compare(o2.getHash(), o1.getHash());
            }
            return Integer.compare(o1.getLevel(), o2.getLevel());
        }
    }
}
