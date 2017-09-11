package com.kaa.puzzle.spaces;

import com.kaa.model.Point;
import com.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class SpaceWithHole extends RealSpace {

    int xSize=4;
    int ySize=2;
    int zSize=4;

    public SpaceWithHole() {
        super(4);
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                for (int k = 0; k < zSize; k++) {
                    addPoint(new Point(i, j, k));
                }
            }
        }

        removePoint(new Point(1, 0, 1));
        removePoint(new Point(1, 1, 1));
    }
}
