package org.kaa.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.kaa.solver.PuzzleSolver.FIXED_MEASURE;

@Getter
public class CompactFigure {

	Set<Integer> compactAtoms = new HashSet<>();

	public CompactFigure(Figure figure) {
		List<Atom> atoms = figure.getAtoms();
		for (Atom atom : atoms) {
			addPoint(atom.getPoint());
		}
	}

	public void addPoint(Point point) {
		compactAtoms.add(point.getIndex(FIXED_MEASURE));
	}
}
