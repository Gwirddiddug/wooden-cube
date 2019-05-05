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

    public static Atom fromIndex(Integer index) {
        Point point = new Point();
        point.x = index % 5;
        point.y = (index - point.x) / 5 % 5;
        point.z = (index - point.x - point.y * 5) / 25;
        return new Atom(point);
    }

    public Point getPoint() {
        return this;
    }

    public Point point() {
        return this;
    }

    public void setOrderNumber(int orderNumber) {
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
