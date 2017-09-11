package com.kaa.puzzle.figures;

import com.kaa.model.Atom;
import com.kaa.model.Figure;
import com.kaa.model.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class Square extends Figure{
    public Square() {
        addAtom(new Atom(new Point(0,0,0)));
        addAtom(new Atom(new Point(0,1,0)));
        addAtom(new Atom(new Point(1,0,0)));
        addAtom(new Atom(new Point(1,1,0)));
    }
}
