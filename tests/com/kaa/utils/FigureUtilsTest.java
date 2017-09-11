package com.kaa.utils;

import com.kaa.model.Atom;
import com.kaa.model.Figure;
import com.kaa.model.Point;
import com.kaa.puzzle.figures.Zigzag;
import com.kaa.utils.FigureUtils;
import junit.framework.TestCase;

import java.util.List;

public class FigureUtilsTest extends TestCase {

    private Figure figure = new Figure();
    private int size = 0;

    @Override
    public void setUp() throws Exception {
        figure.addAtom(new Atom(new Point(0, 0, 0)));
        figure.addAtom(new Atom(new Point(0, 1, 0)));
        figure.addAtom(new Atom(new Point(1, 1, 0)));
        size = figure.size();
    }

    public void testBuildPostures() throws Exception {

        List<Figure> figures = FigureUtils.buildPostures(figure);

        assertEquals("Incorrect figures count", 12, figures.size());
        for (Figure figure1 : figures) {
            assertEquals("Incorrect size", size, figure1.size());
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
        assertNotNull(newFigure90.getAtom(new Point(0, 0, 0)));
        assertNotNull(newFigure90.getAtom(new Point(0, 0, 1)));
        assertNotNull(newFigure90.getAtom(new Point(1, 0, 1)));

        Figure newFigure180 = FigureUtils.rotateX(newFigure90);
        assertEquals(size, newFigure180.size());

        assertEquals(size, newFigure180.size());
        assertNotNull(newFigure180.getAtom(new Point(0, 1, 0)));
        assertNotNull(newFigure180.getAtom(new Point(0, 0, 0)));
        assertNotNull(newFigure180.getAtom(new Point(1, 0, 0)));


        Figure newFigure270 = FigureUtils.rotateX(newFigure180);
        assertEquals(size, newFigure270.size());

        assertEquals(size, newFigure270.size());
        assertNotNull(newFigure270.getAtom(new Point(0, 0, 1)));
        assertNotNull(newFigure270.getAtom(new Point(0, 0, 0)));
        assertNotNull(newFigure270.getAtom(new Point(1, 0, 0)));
    }

    public void testRotateX270() {
        Figure newFigure270 = FigureUtils.rotateX270(figure);
        assertEquals(size, newFigure270.size());

        assertEquals(size, newFigure270.size());
        assertNotNull(newFigure270.getAtom(new Point(0, 0, 1)));
        assertNotNull(newFigure270.getAtom(new Point(0, 0, 0)));
        assertNotNull(newFigure270.getAtom(new Point(1, 0, 0)));
    }
}