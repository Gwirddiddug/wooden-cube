package org.kaa.model;

import java.io.Serializable;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представление точки в декартовой системе координат
 */
public class Point implements Serializable {
	public int x = 0, y = 0, z = 0;

	public Point() {
	}

	public Point(int x) {
		this.x = x;
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//вычисляет удаление точки от центра в попугаях
	public int getRadius() {
		return x^2 + y^2 + z^2;
	}

	@Override
	protected Point clone() {
		return new Point(x, y, z);
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", x, y, z);
	}

	public int getIndex(int measure) {
		return measure * measure * (z) + measure * (y) + x;
	}
}
