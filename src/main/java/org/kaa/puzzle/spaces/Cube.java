package org.kaa.puzzle.spaces;

import org.kaa.model.RealSpace;
import org.kaa.model.SpacePoint;

/**
 * @author Typhon
 * @since 01.11.2014
 * Cube description
 */
public class Cube extends RealSpace {
    int zSize=5;
    int xSize=5;
    int ySize=5;

    public Cube() {
        this(5);
        setName(this.getClass().getSimpleName());
    }

    public Cube(int size) {
        super(size);
        int pointsCount = 0;
        zSize = size;
        xSize = size;
        ySize = size;

        //методы оптимизации поиска решения

/*
        addPoint(spacePoint);
        spacePoint = getPoint(1, 0, 3);
        addPoint(spacePoint);
        spacePoint = getPoint(0, 0, 2);
        addPoint(spacePoint);
        spacePoint = getPoint(0, 1, 0);
        addPoint(spacePoint);
*/

        int[] xes = {0, size};
        int[] ys = {0, size};
        int[] zes = {0, size};

        //добавляем сначала угловые точки, как дающие наименьшее количество вариантов
        //должно быть 8
        pointsCount = 0;
        for (int x : xes) {
            for (int y : ys) {
                for (int z : zes) {
                    if (addPoint(new SpacePoint(x, y, z))) pointsCount++;
                }
            }
        }
        System.out.println("corner:" + pointsCount + "/" + 8);

        //добавляем рёберные точки
        //должно быть
        pointsCount = 0;
        for (int x : xes) {
            for (int y : ys) {
                for (int z = 1; z < size - 1; z++) {
                    if (addPoint(new SpacePoint(x, y, z))) pointsCount++;
                    if (addPoint(new SpacePoint(y, z ,x))) pointsCount++;
                    if (addPoint(new SpacePoint(z, x, y))) pointsCount++;
                }
            }
        }
        System.out.println("edge:" + pointsCount + "/" + 36);


        //добавляем боковые точки
        //должно быть 54
        pointsCount = 0;
        for (int x : xes) {
            for (int y = 1; y < size - 1; y++) {
                for (int z = 1; z < size - 1; z++) {
                    if (addPoint(new SpacePoint(x, y, z))) pointsCount++;
                    if (addPoint(new SpacePoint(y, z ,x))) pointsCount++;
                    if (addPoint(new SpacePoint(z, x, y))) pointsCount++;
                }
            }
        }
        System.out.println("side:" + pointsCount + "/" + 54);

        //добавляем центральные точки
        //должно быть 27
        pointsCount = 0;
        for (int x = 1; x < size - 1; x++) {
            for (int y = 1; y < size - 1; y++) {
                for (int z = 1; z < size - 1; z++) {
                    addPoint(new SpacePoint(x, y, z));
                    pointsCount++;
                }
            }
        }
        System.out.println("center:" + pointsCount + "/" + 27);

        if (points.size()!=125) {
            throw new RuntimeException("Incorrect points count!");
        }
    }
}

