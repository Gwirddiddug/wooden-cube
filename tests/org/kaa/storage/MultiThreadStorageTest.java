package org.kaa.storage;

import org.junit.jupiter.api.Test;
import org.kaa.model.CompactFigure;
import org.kaa.model.RealSpace;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiThreadStorageTest {

    @Test
    void getItems() {
        SortedStorage storage = new SortedStorage();
        RealSpace item1 = new RealSpace(4);
        item1.addCompactFigure(new CompactFigure());
        item1.addCompactFigure(new CompactFigure());
        item1.addCompactFigure(new CompactFigure());
        item1.addCompactFigure(new CompactFigure());
        RealSpace item2 = new RealSpace(2);
        item2.addCompactFigure(new CompactFigure());
        item2.addCompactFigure(new CompactFigure());
        RealSpace item3 = new RealSpace(3);
        item3.addCompactFigure(new CompactFigure());
        item3.addCompactFigure(new CompactFigure());
        item3.addCompactFigure(new CompactFigure());
        storage.add(item2);
        storage.add(item1);
        storage.add(item3);

        Comparator<? super RealSpace> comparator = (Comparator<RealSpace>) (o1, o2) -> -(Integer.compare(o2.getLevel(), o1.getLevel()));
//        assertEquals(1, comparator.compare(item2, item1));
        assertEquals(4, storage.getLast().getCompactFigures().size());
    }
}