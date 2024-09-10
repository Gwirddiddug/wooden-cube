package org.kaa.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SpaceTest {

    public void testPointKey() {
        Space space = new RealSpace(3);
        space.addPoint(new Point(0,1,1));
        space.addPoint(new Point(3,0,2));

        assertEquals("(0, 1, 1)", space.getPoint(0,1,1).toString());
        assertEquals("(3, 0, 2)", space.getPoint(new Point(3,0,2)).toString());
        assertNull(space.getPoint(new Point(4,2,0)));
    }
}