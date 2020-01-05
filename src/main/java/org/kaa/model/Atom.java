package org.kaa.model;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представление одного кубика
 */
public class Atom extends Point {
	private int orderNumber = 0;

	public Atom(Point point) {
		super(point.x, point.y, point.z);
	}

	public Atom(int x, int y, int z) {
		super(x, y, z);
	}

	public static Atom fromIndex(Integer index, int measure) {
		Point point = new Point();
		point.x = index % measure;
		point.y = (index - point.x) / measure % measure;
		point.z = (index - point.x - point.y * measure) / measure / measure;
		return new Atom(point);
	}

	public Point getPoint() {
		return this;
	}

	public Point point() {
		return this;
	}

	void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return "Atom{" +
				"point=" + super.toString() +
				", orderNumber=" + orderNumber +
				'}';
	}

	@Override
	protected Atom clone() {
		Atom atom = new Atom(this);
		atom.setOrderNumber(orderNumber);
		return atom;
	}
}
