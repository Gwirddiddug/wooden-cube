package com.kaa.puzzle.spaces;

import com.kaa.model.Point;
import com.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 25.11.2014
 */
public class Cube525 extends RealSpace {

    public Cube525(int cubeSize) {
        super(cubeSize);
        int xSize = cubeSize;
        int ySize = cubeSize-3;
        int zSize = cubeSize;

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    addPoint(new Point(x, y, z));
                }
            }
        }
    }

    public Cube525() {
        this(5);
    }

}
