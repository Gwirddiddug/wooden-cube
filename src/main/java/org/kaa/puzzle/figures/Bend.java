package org.kaa.puzzle.figures;

import org.kaa.model.Atom;
import org.kaa.model.Figure;
import org.kaa.model.Point;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class Bend extends Figure {

	public Bend() {
		addAtom(new Atom(new Point(0, 0, 0)));
		addAtom(new Atom(new Point(0, 1, 0)));
		addAtom(new Atom(new Point(1, 1, 0)));
	}

}
