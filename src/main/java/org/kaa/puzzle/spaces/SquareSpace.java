package org.kaa.puzzle.spaces;

import org.kaa.model.Point;
import org.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class SquareSpace extends RealSpace {

    public SquareSpace() {
        super(4);
        int xSize=2;
        int ySize=4;
        int zSize=1;

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                for (int k = 0; k < zSize; k++) {
                    addPoint(new Point(i, j, k));
                }
            }
        }
    }
}
