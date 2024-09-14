package org.kaa.utils;

import org.kaa.model.Point;

import java.util.Collection;

/**
 * @author sbt-kopilov-aa
 * @version 5.0
 * @since 24/11/2014
 */
public class Utils {


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

}
