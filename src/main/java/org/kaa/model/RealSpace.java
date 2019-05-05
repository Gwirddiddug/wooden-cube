package org.kaa.model;

import org.kaa.utils.Utils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Typhon
 * @since 22.11.2014
 * Базовое класс для пространств размещения
 */
public class RealSpace extends Space implements Serializable {

    protected List<Figure> figures = new LinkedList<>();

    public RealSpace(int cubeSize) {
        super(cubeSize);
    }

    public RealSpace() {
    }

    @Override
    public RealSpace clone() {
        RealSpace clone = new RealSpace(cubeSize);
        Iterator<SpacePoint> iterator = iterator();
        while (iterator.hasNext()) {
            clone.addPoint(iterator.next().clone());
        }
        for (Figure figure : figures) {
            clone.figures.add(figure.clone());
        }
        return clone;
    }

    /**
     * Определяет использовать ли оптимистичный сценарий заполнения или пессимистичный
     * @param figure
     * @return
     */
    private boolean optimisticCase(Figure figure) {
        return size() % figure.size() == 0;
    }

    public List<RealSpace> allocateFigure(Figure figure, SpacePoint point) {
        List<RealSpace> spaces = new LinkedList<>();

        Iterator<Atom> iterator = figure.atomator();
        while (iterator.hasNext()) {
            Atom next = iterator.next();
            Point zeroPoint = getNewZeroPoint(point, next.getPoint());
            Figure adaptedFigure = figure.recalculateForNewZeroPoint(zeroPoint);
            if (hasEnoughSpaceFor(adaptedFigure)) {
                RealSpace newSpace = clone();
                newSpace.putFigure(adaptedFigure);
                spaces.add(newSpace);
            }
        }
        return spaces;
    }

    /**
     * allocate figure at next free point
     * @param figure allocated figure
     */
    public List<RealSpace> allocateFigure(Figure figure) {
        List<RealSpace> solutions = new LinkedList<>();

        //если количество ячеек пространства не кратно количеству ячеек фигуры,
        // то перебор по первой точке не даст максимального заполнения
        if (optimisticCase(figure)) {
//            List<RealSpace> allocateFigure = allocateFigure(figure, getFirstPoint());
            List<RealSpace> allocateFigure = allocateFigure(figure, getNextPoint());
            solutions.addAll(allocateFigure);
        } else {
            List<SpacePoint> points = getFreePoints();
            for (SpacePoint spacePoint : points) {
                solutions.addAll(allocateFigure(figure, spacePoint));
            }
        }

        return solutions;

    }

    private SpacePoint getNextPoint() {
        return getFreePoint();
    }


    private void putFigure(Figure figure) {
        Iterator<Atom> iterator = figure.atomator();
        while (iterator.hasNext()) {
            Atom next = iterator.next();
            SpacePoint point = getPoint(next.getPoint());
            if (point==null || !point.isEmpty()) {
                throw new RuntimeException("Space point not Empty!");
            }
            point.setEmpty(false);
            removePoint(point);
        }
        figures.add(figure);
    }

    public boolean isAllocatableFigure(Figure figure) {
        List<SpacePoint> points = new LinkedList<>();
        if (optimisticCase(figure)) {
            points.add(getFreePoint());
        } else {
            points = getFreePoints();
        }

        for (SpacePoint spacePoint : points) {
            if (isAllocatableFigure(figure, spacePoint)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAllocatableFigure(Figure figure, SpacePoint point) {
        Iterator<Atom> iterator = figure.atomator();
        while (iterator.hasNext()) {
            Atom next = iterator.next();
            Point zeroPoint = getNewZeroPoint(point, next);
            Figure adaptedFigure = figure.recalculateForNewZeroPoint(zeroPoint);

            if (hasEnoughSpaceFor(adaptedFigure)) {
                return true;
            }
        }
        return false;
    }

    private Point getNewZeroPoint(Point space, Point figure) {
        Point zero = new Point(space.x - figure.x, space.y - figure.y, space.z - figure.z);
        return zero;
    }

    public List<Figure> figures() {
        return figures;
    }

    /**
     *
     * @return текстовое представление пространства
     */
    public String getView() {
        return buildStringView();
    }

    private String buildStringView() {
        String emptySquare = "\u25A1";
        String filledSquare = "\u25A0";

        Point minPoint = getMinPoint();
        Point maxPoint = getMaxPoint();

        ///todo сделать корректное отображение 3D
        String coords = "{";
        String output = "";
        for (int y = maxPoint.y; y >= minPoint.y; y--) {
            String line = "";
            for (int x = minPoint.x; x <= maxPoint.x; x++) {
                for (int z = minPoint.z; z <= maxPoint.z; z++) {
                    SpacePoint point = getPoint(x+1, y+1, z+1);
                    if (point == null) {
                        line += " ";
                    } else {
                        coords += point;
                        coords += ",";
                        line += point.isEmpty() ? emptySquare : filledSquare;
                    }
                }
            }
            output += line + "\n";
        }
        if (coords.length()>1){
            coords = coords.substring(1, coords.length() - 1);
        }
        coords += "}";

        output = "";
        int order = 0;
        for (Figure part: figures) {
            order++;
            output +=(String.format("\nFigure#%s:\n%s", order, part));
        }
        return output;
    }


    /**
     * Формирует абстрактную точку с минимальными значеними по всем координатам
     * @return
     */
    public Point getMinPoint() {
        return Utils.getMinPoint(points.values());
    }

    /**
     * Формирует абстрактную точку с максимальными значеними по всем координатам
     * @return
     */
    public Point getMaxPoint() {
        return Utils.getMaxPoint(points.values());
    }


    public String getTextView() {
        String textView = "";

        textView += "\nРазмер куба:" + cubeSize + "\n";
        textView += "Точки:\n";
        int i = 1;
        for (SpacePoint spacePoint : points.values()) {
            textView += String.format("\nPoint#%s: %s", i++, spacePoint) ;
        }

        textView += "Фигуры:\n";
        i = 0;
        for (Figure figure : figures) {
            textView += String.format("\nFigure#%s: %s", i++, figure) ;
        }

        return textView;
    }
}
