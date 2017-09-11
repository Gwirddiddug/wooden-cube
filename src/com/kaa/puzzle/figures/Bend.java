package com.kaa.puzzle.figures;

import com.kaa.model.Atom;
import com.kaa.model.Figure;
import com.kaa.model.Point;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class Bend extends Figure {

    public Bend() {
        addAtom(new Atom(new Point(0,0,0)));
        addAtom(new Atom(new Point(0,1,0)));
        addAtom(new Atom(new Point(1,1,0)));
    }

}
