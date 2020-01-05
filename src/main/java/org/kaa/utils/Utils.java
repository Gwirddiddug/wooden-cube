package org.kaa.utils;

import org.kaa.model.Atom;
import org.kaa.model.Point;

import java.util.Collection;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 24/11/2014
 */
public class Utils {

	public static int getPointKey(int cubeSize, Point point) {
		return getPointKey(cubeSize, point.x, point.y, point.z);
	}


	/**
	 * Формирует числовой код идентифицирующий точку в конкретном пространстве (компактное представление)
	 *
	 * @param cubeSize размер максимального ребра паралллепипида
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static int getPointKey(int cubeSize, int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0) return -1;
		if (x >= cubeSize || y >= cubeSize || z >= cubeSize) return -1;
		return cubeSize * cubeSize * x + cubeSize * y + z;
	}

	/**
	 * Формирует точку с минимальными значеними по всем координатам
	 *
	 * @param points исходный набор точек
	 * @return
	 */
	public static Point getMinPoint(Collection<? extends Point> points) {
		int minX = -1, minY = -1, minZ = -1;

		for (Point point : points) {
			if (minX > point.x || minX == -1) {
				minX = point.x;
			}
			if (minY > point.y || minY == -1) {
				minY = point.y;
			}
			if (minZ > point.z || minZ == -1) {
				minZ = point.z;
			}
		}

		return new Point(minX, minY, minZ);
	}

	/**
	 * Формирует точку с максимальными значеними по всем координатам
	 *
	 * @param points исходный набор точек
	 * @return
	 */
	public static Point getMaxPoint(Collection<? extends Point> points) {

		int maxX = 0, maxY = 0, maxZ = 0;

		for (Point point : points) {
			maxX = maxX < point.x ? point.x : maxX;
			maxY = maxY < point.y ? point.y : maxY;
			maxZ = maxZ < point.z ? point.z : maxZ;
		}

		return new Point(maxX, maxY, maxZ);
	}

	public static Atom getPointByKey(Integer compactPoint, int cubeSize) {
		int x = compactPoint / cubeSize;
		int y = (compactPoint - x) / cubeSize;
		int z = compactPoint - x - y;

		return new Atom(x, y, z);
	}
}
