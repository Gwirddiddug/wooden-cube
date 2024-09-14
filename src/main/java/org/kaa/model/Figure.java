package org.kaa.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Typhon
 * @since 01.11.2014
 */

@Getter
@Setter
public class Figure implements Serializable {

	protected List<Atom> atoms = new LinkedList<>();

	//RC - rotations count - how much times figure rotate by any axis
	private int xRC = 0;
	private int yRC = 0;
	private int zRC = 0;

	public Figure() {
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
		return atoms.stream().map(Atom::getPoint).collect(Collectors.toList());
	}

	public void addAtom(Atom atom) {
		atom.setOrderNumber(atoms.size());
		atoms.add(atom);
//        compactAtoms.add(atom.getPoint().getIndex(FIXED_MEASURE));
	}

	public int size() {
		return atoms.size();
	}

	public Iterator<Atom> atomator() {
		return new Atomator();
	}

	public List<Atom> atoms() {
		return atoms;
	}

	@Override
	public Figure clone() {
		Figure clone = new Figure();
		for (Atom atom : atoms) {
			clone.addAtom(atom.clone());
		}

		clone.xRC = xRC;
		clone.yRC = yRC;
		clone.zRC = zRC;
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
		for (Atom atom : recalculated.atoms) {
			atom.getPoint().x += zeroPoint.x;
			atom.getPoint().y += zeroPoint.y;
			atom.getPoint().z += zeroPoint.z;
		}
		return recalculated;
	}

	/*
	* returns самую близкую к центру точку
	 */
	public Atom getMinAtom() {
		int minRadius = Integer.MAX_VALUE;
		Atom minAtom = null;
		for (Atom atom : atoms) {
			int radius = atom.getRadius();
			if (minRadius > radius) {
				minRadius = radius;
				minAtom = atom;
			}
		}

		return minAtom;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public String getName() {
		return String.valueOf(xRC) + yRC + zRC;
	}

	public Atom getAtom(Point point) {
		List<Atom> collect = atoms.stream().filter(p -> p.getPoint().x == point.x
				&& p.getPoint().y == point.y
				&& p.getPoint().z == point.z).collect(Collectors.toList());

		return !collect.isEmpty() ? collect.get(0) : null;
	}

	private class Atomator implements Iterator<Atom> {
		private int currentNumber = 0;

		@Override
		public boolean hasNext() {
			return atoms.size() > currentNumber;
		}

		@Override
		public Atom next() {
			if (!hasNext()) {
				throw new RuntimeException("Reached end of collection!");
			}
			currentNumber++;
			return atoms.get(currentNumber - 1);
		}
	}
}

