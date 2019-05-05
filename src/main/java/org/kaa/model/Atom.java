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
