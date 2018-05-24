package com.kaa.puzzle.spaces;

import com.kaa.model.Point;
import com.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class DoubleBend extends RealSpace {

    int xSize=1;
    int ySize=1;
    int zSize=1;

    public DoubleBend() {
        super(2);
        for (int i = 0; i <= xSize; i++) {
            for (int j = 0; j <= ySize; j++) {
                for (int k = 0; k <= zSize; k++) {
                    addPoint(new Point(i, j, k));
                }
            }
        }

        removePoint(new Point(0, 0, 0));
        removePoint(new Point(0, 0, 1));
    }
}
