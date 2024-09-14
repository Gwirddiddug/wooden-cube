package org.kaa.storage;

import org.kaa.exceptions.OutOfUnitsException;

import java.util.Collection;

public interface CommonStorage<T> {
    T getFirst() throws OutOfUnitsException;

    T getLast();

    boolean add(T unit);

    boolean addAll(Collection<T> units);

    int size();
}
