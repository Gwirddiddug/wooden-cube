package com.kaa.puzzle.figures;

import com.kaa.model.Atom;
import com.kaa.model.Figure;
import com.kaa.model.Point;

/**
 * @author Typhon
 * @since 25.11.2014
 */
public class Zigzag extends Figure {

    public Zigzag() {
        addAtom(new Atom(new Point(0,0,0)));
        addAtom(new Atom(new Point(0,1,0)));
        addAtom(new Atom(new Point(1,1,0)));
        addAtom(new Atom(new Point(1,2,0)));
        addAtom(new Atom(new Point(1,3,0)));
    }
}
