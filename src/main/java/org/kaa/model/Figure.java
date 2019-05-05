package org.kaa.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Typhon
 * @since 01.11.2014
 */
@Getter
public class Figure implements Serializable{

    private List<Atom> atoms = new LinkedList<>();

    //RC - rotations count - how much times figure rotate by any axis
    private int xRC = 0;
    private int yRC = 0;
    private int zRC = 0;

    public int getxRC() {
        return xRC;
    }

    public int getyRC() {
        return yRC;
    }

    public int getzRC() {
        return zRC;
    }

    public void incXRC() {
        this.xRC++;
    }

    public void incYRC() {
        this.yRC++;
    }

    public void incZRC() {
        this.zRC++;
    }

    List<Point> coordinateList() {
        List<Point> list = atoms.stream().map(p->p.getPoint()).collect(Collectors.toList());
        return list;
    }

    public void addAtom(Atom atom) {
        atom.setOrderNumber(atoms.size());
        atoms.add(atom);
    }

    public int size() {
        return atoms.size();
    }

    public Iterator<Atom> atomator(){
        return new atomator();
    }

    public List<Atom> atoms() {
        return atoms;
    }

    private class atomator implements Iterator<Atom> {
        private int currentNumber = 0;

        @Override
        public boolean hasNext() {
            return atoms.size()>currentNumber;
        }

        @Override
        public Atom next() {
            if (!hasNext()) {
                throw new RuntimeException("Reached end of collection!");
            }
            currentNumber++;
            return atoms.get(currentNumber-1);
        }
    }

    @Override
    public Figure clone() {
        Figure clone = new Figure();
        for (Atom atom : atoms) {
            clone.addAtom(atom.clone());
            clone.xRC = xRC;
            clone.yRC = yRC;
            clone.zRC = zRC;
        }
        return clone;
    }

    public Figure clone(boolean empty) {
        Figure clone = clone();
        if (empty) {
            clone.atoms.clear();
        }
        return clone;
    }

    public Figure recalculateForNewZeroPoint(Point zeroPoint) {
        Figure recalculated = clone();
        for (Atom atom:recalculated.atoms){
            atom.getPoint().x += zeroPoint.x;
            atom.getPoint().y += zeroPoint.y;
            atom.getPoint().z += zeroPoint.z;
        }
        return recalculated;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getName() {
        return String.valueOf(xRC) + String.valueOf(yRC) +String.valueOf(zRC);
    }

    public Atom getAtom(Point point) {
        List<Atom> collect = atoms.stream().filter(p -> p.getPoint().x == point.x
                && p.getPoint().y == point.y
                && p.getPoint().z == point.z).collect(Collectors.toList());

        return collect.size()>0?collect.get(0):null;
    }


}

