package org.kaa.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представление заполняемой фигуры (полости)
 * Работает только с положительными точками
 */
@Getter
public abstract class Space implements Serializable {

	protected HashMap<Integer, SpacePoint> points = new HashMap<>();
	protected final int cubeSize; //размер куба в котором размещена фигура
	protected final int abscissus; //размер по оси X
	protected final int ordinatus; //размер по оси
	protected final int applicata; //размер по оси X

	protected Space(int abscissus, int ordinatus, int applicata) {
		this.cubeSize = Math.max(Math.max(abscissus, ordinatus), applicata);
        this.abscissus = abscissus;
        this.ordinatus = ordinatus;
        this.applicata = applicata;
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
		return points.get(getPointKey(x, y, z));
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


	public int getPointKey(int cubeSize, Point point) {
		return getPointKey(point.x, point.y, point.z);
	}


	/**
	 * Формирует числовой код идентифицирующий точку в конкретном пространстве (компактное представление)
	 *
	 * @param x
	 * @param y
	 * @param z
	 * @return порядковый номер точки в пространстве
	 */
	public int getPointKey(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0) return -1;
		if (x >= abscissus || y >= ordinatus || z >= applicata) return -1;
		return abscissus * ordinatus * z + abscissus * y + x;
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

	public Atom getPointByKey(Integer key) {
        int z = key / (abscissus * ordinatus);
		int y = (key - z * abscissus * ordinatus) / abscissus;
		int x = key - z * abscissus * ordinatus - y * abscissus;

		return new Atom(x, y, z);
	}
}
