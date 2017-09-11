package com.kaa.puzzle.spaces;

import com.kaa.model.Point;
import com.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 02.12.2014
 */
public class Cube544 extends RealSpace {

    public Cube544(int cubeSize) {
        super(cubeSize);
        int xSize = 5;
        int ySize = 4;
        int zSize = 4;

        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    addPoint(new Point(x, y, z));
                }
            }
        }
    }

    public Cube544() {
        this(5);
    }
}
