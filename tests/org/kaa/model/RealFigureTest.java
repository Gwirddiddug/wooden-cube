package org.kaa.model;

import org.junit.jupiter.api.Test;
import org.kaa.puzzle.figures.Teewee;

import static org.junit.jupiter.api.Assertions.*;

class RealFigureTest {

    @Test
    void getPointByKey() {

        RealSpace space = new RealSpace(2, 3, 2);
        RealFigure realFigure = new RealFigure(new Teewee(), space);

        Atom point = realFigure.getPointByKey(16);

        assertEquals(0, point.x);
        assertEquals(2, point.y);
        assertEquals(2, point.z);



        space = new RealSpace(3, 2, 2);
        realFigure = new RealFigure(new Teewee(), space);

        point = realFigure.getPointByKey(16);

        assertEquals(1, point.x);
        assertEquals(1, point.y);
        assertEquals(2, point.z);
    }
}