package org.kaa.puzzle.spaces;

import org.kaa.model.Point;
import org.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 01.11.2014
 * Base parallelepiped figure
 */
public class CommonCube extends RealSpace {

    private final int x;
    private final int y;
    private final int z;

    public CommonCube(int xSize, int ySize, int zSize) {
        super(Math.max(Math.max(xSize, ySize), zSize));
        this.x = xSize;
        this.y = ySize;
        this.z = zSize;
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    addPoint(new Point(x, y, z));
                }
            }
        }
        setName(this.getClass().getSimpleName());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}

