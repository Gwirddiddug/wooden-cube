package org.kaa.model;

import org.kaa.utils.Utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static org.kaa.solver.PuzzleSolver.FIXED_MEASURE;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представление заполняемой фигуры (полости)
 * Работает только с положительными точками
 */
public abstract class Space implements Serializable {

	protected HashMap<Integer, SpacePoint> points = new HashMap<>();
	protected final int cubeSize; //размер куба в котором размещена фигура

	protected Space(int cubeSize) {
		this.cubeSize = cubeSize + 1;
	}

	public int getSize() {
		return points.size();
	}

	protected abstract int getPointKey(Point point);

	/**
	 * check containing point by space
	 *
	 * @param point point description
	 * @return true, if contain
	 */
	public boolean hasPoint(Point point) {
		int pointKey = getPointKey(point);
		if (pointKey < 0) {
			return false;
		}
		return points.containsKey(pointKey);
	}

	public boolean addPoint(int pointKey) {
		return addPoint(new SpacePoint(Utils.getPointByKey(pointKey, FIXED_MEASURE)));
	}

	public boolean addPoint(Point point) {
		return addPoint(new SpacePoint(point));
	}

	public boolean addPoint(SpacePoint point) {
		int key = getPointKey(point);
		if (key >= 0 && !points.containsKey(key)) {
			points.put(key, point);
			return true;
		} else {
			return false;
		}
	}

	public void removePoint(Point point) {
		points.remove(getPointKey(point));
	}

	public SpacePoint getPoint(Point point) {
		return points.get(getPointKey(point));
	}

	public SpacePoint getPoint(int x, int y, int z) {
		return points.get(Utils.getPointKey(cubeSize, x, y, z));
	}

	protected boolean hasEnoughSpaceFor(Figure figure) {
		Iterator<Atom> iterator = figure.atomator();
		boolean enough = true;
		while (iterator.hasNext() && enough) {
			Atom next = iterator.next();
			enough &= hasPoint(next);
		}
		return enough;
	}

	/**
	 * @return next free point in space
	 */
	public SpacePoint getFreePoint() {
		for (SpacePoint point : points.values()) {
			return point;
		}
		return null;
	}

	public SpacePoint getFirstPoint() {
		if (points.size() > 0) {
			Set<Integer> integers = points.keySet();
			return points.get(integers.toArray()[0]);
		}
		return null;
	}


	protected Collection<SpacePoint> getFreePoints() {
//        return points.values().stream().filter(p -> p.isEmpty()).collect(Collectors.toList());
		return points.values();
	}

	public int size() {
		return points.size();
	}

	public int countEmpty() {
		return size();
	}

	public int countFilled() {
		return cubeSize * cubeSize * cubeSize - size();
	}

	public boolean equals(Space space) {
		if (space.cubeSize != cubeSize) return false;
		if (space.size() != size()) return false;

		for (Integer key : space.points.keySet()) {
			if (!points.containsKey(key)) {
				return false;
			}
		}
		return true;
	}

	public int getHash() {
		Set<Integer> keys = points.keySet();
		StringBuilder builder = new StringBuilder();
		for (Integer key : keys) {
			builder.append(key.toString());
		}
		return builder.toString().hashCode();
	}
}
