package org.kaa.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.kaa.model.Atom;
import org.kaa.model.Figure;
import org.kaa.model.Point;
import org.kaa.puzzle.figures.Zigzag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FigureUtilsTest {

    private Figure figure = new Figure();
    private int size = 0;

    @BeforeEach
    public void setUp() throws Exception {
        figure.addAtom(new Atom(new Point(0, 0, 0)));
        figure.addAtom(new Atom(new Point(0, 1, 0)));
        figure.addAtom(new Atom(new Point(1, 1, 0)));
        size = figure.size();
    }

    public void testBuildPostures() throws Exception {

        List<Figure> figures = FigureUtils.buildPostures(figure);

        assertEquals(12, figures.size(), "Incorrect figures count");
        for (Figure figure1 : figures) {
            assertEquals(size, figure1.size(), "Incorrect size");
        }
    }

    public void testBuildPosturesBend(){
        List<Figure> postures = FigureUtils.buildPostures(new Zigzag());

        for (Figure posture : postures) {
            System.out.println(posture.getName());
        }

        Figure bend2 = new Figure();
        bend2.addAtom(new Atom(new Point(0, 0, 0)));
        bend2.addAtom(new Atom(new Point(1, 0, 0)));
        bend2.addAtom(new Atom(new Point(1, 1, 0)));

        Figure bend3 = new Figure();
        bend3.addAtom(new Atom(new Point(0, 0, 0)));
        bend3.addAtom(new Atom(new Point(1, 0, 0)));
        bend3.addAtom(new Atom(new Point(0, 1, 0)));

        Figure bend4 = new Figure();
        bend4.addAtom(new Atom(new Point(0, 1, 0)));
        bend4.addAtom(new Atom(new Point(1, 0, 0)));
        bend4.addAtom(new Atom(new Point(1, 1, 0)));
    }


    private boolean hasEqualFigure(List<Figure> figures, Figure figure) {
        for (Figure figure1 : figures) {
            if (figure.equals(figure1)) {
                return true;
            }
        }
        return false;
    }


    public void testRotateX() {
        Figure newFigure90 = FigureUtils.rotateX(figure);

        assertEquals(size, newFigure90.size());
        Assertions.assertNotNull(newFigure90.getAtom(new Point(0, 0, 0)));
        Assertions.assertNotNull(newFigure90.getAtom(new Point(0, 0, 1)));
        Assertions.assertNotNull(newFigure90.getAtom(new Point(1, 0, 1)));

        Figure newFigure180 = FigureUtils.rotateX(newFigure90);
        assertEquals(size, newFigure180.size());

        assertEquals(size, newFigure180.size());
        Assertions.assertNotNull(newFigure180.getAtom(new Point(0, 1, 0)));
        Assertions.assertNotNull(newFigure180.getAtom(new Point(0, 0, 0)));
        Assertions.assertNotNull(newFigure180.getAtom(new Point(1, 0, 0)));


        Figure newFigure270 = FigureUtils.rotateX(newFigure180);
        assertEquals(size, newFigure270.size());

        assertEquals(size, newFigure270.size());
        Assertions.assertNotNull(newFigure270.getAtom(new Point(0, 0, 1)));
        Assertions.assertNotNull(newFigure270.getAtom(new Point(0, 0, 0)));
        Assertions.assertNotNull(newFigure270.getAtom(new Point(1, 0, 0)));
    }


    public void testRotateX270() {
        Figure newFigure270 = FigureUtils.rotateX270(figure);
        assertEquals(size, newFigure270.size());

        assertEquals(size, newFigure270.size());
        Assertions.assertNotNull(newFigure270.getAtom(new Point(0, 0, 1)));
        Assertions.assertNotNull(newFigure270.getAtom(new Point(0, 0, 0)));
        Assertions.assertNotNull(newFigure270.getAtom(new Point(1, 0, 0)));
    }
}