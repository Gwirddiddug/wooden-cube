package com.kaa.puzzle.spaces;

import com.kaa.model.Point;
import com.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class SimpleSpace extends RealSpace {

    int xSize=3;
    int ySize=2;
    int zSize=2;

    public SimpleSpace() {
        super(3);
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                for (int k = 0; k < zSize; k++) {
                    addPoint(new Point(i, j, k));
                }
            }
        }

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize-2; j++) {
//                addPoint(new Point(i+4, j, 0));
            }
        }
    }
}
