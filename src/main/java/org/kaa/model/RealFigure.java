package org.kaa.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Figure in specific space
 */
@Getter
@Setter
public class RealFigure extends Figure {

    private final Space space;

    private Set<Integer> compactAtoms = new HashSet<>();

    public RealFigure(Space space) {
        super();
        this.space = space;
    }

    public RealFigure(CompactFigure compactFigure, Space space) {
        this(space);
        compactAtoms = new HashSet<>(compactFigure.getCompactAtoms());
        for (Integer code : compactAtoms) {
            atoms.add(space.getPointByKey(code));
        }
    }

    public RealFigure(Figure figure, RealSpace space) {
        this(space);
        atoms = new ArrayList<>(figure.getAtoms());
        compactAtoms = this.buildCompactAtoms();
    }

    public CompactFigure buildCompact() {
        return new CompactFigure(buildCompactAtoms());
    }

    public Set<Integer> buildCompactAtoms() {
        Set<Integer> compactAtoms = new HashSet<>();
        for (Atom atom : atoms) {
            compactAtoms.add(space.getPointKey(atom));
        }
        return compactAtoms;
    }



    @Override
    public Figure clone() {
        RealFigure cloned = new RealFigure(space);
        cloned.setCompactAtoms(new HashSet<>(compactAtoms));
        return cloned;
    }


    public Atom getPointByKey(Integer key) {
        int spaceX = space.getAbscissus();
        int spaceY = space.getOrdinatus();
        int z = key / (spaceX * spaceY);
        int y = (key - z * spaceX * spaceY) / spaceX;
        int x = key - z * spaceX * spaceY - y * spaceX;

        return new Atom(x, y, z);
    }
}
