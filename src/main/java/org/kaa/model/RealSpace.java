package org.kaa.model;

import org.kaa.exceptions.FilledSpacePointException;
import org.kaa.solver.ResultPrinter;
import org.kaa.utils.Utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Typhon
 * @since 22.11.2014
 * Базовое представленеи для пространства размещения
 */
public class RealSpace extends Space implements Serializable {

	protected Set<Figure> figures = new HashSet<>();
	protected Set<CompactFigure> compactFigures = new HashSet<>();

	public RealSpace(int cubeSize) {
		super(cubeSize);
	}

	public RealSpace buildClone() {
		RealSpace clone = new RealSpace(getCubeSize());
		clone.points = new HashMap<>(points);
		clone.compactFigures = new HashSet<>(compactFigures);
		return clone;
	}

	@Override
	protected int getPointKey(Point point) {
		return Utils.getPointKey(getCubeSize(), point);
	}

	public int getCubeSize() {
		return cubeSize - 1;
	}

	/**
	 * Определяет использовать ли оптимистичный сценарий заполнения или пессимистичный
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
				RealSpace newSpace = buildClone();
				newSpace.putFigure(adaptedFigure);
				spaces.add(newSpace);
			}
		}
		return spaces;
	}

	/**
	 * allocate figure at next free point
	 *
	 * @param figure allocated figure
	 */
	public List<RealSpace> allocateFigure(Figure figure) {
		List<RealSpace> solutions = new LinkedList<>();

		//если количество ячеек пространства не кратно количеству ячеек фигуры,
		// то перебор по первой точке не даст максимального заполнения
		if (optimisticCase(figure)) {
			List<RealSpace> allocateFigure = allocateFigure(figure, getNextPoint());
			solutions.addAll(allocateFigure);
		} else {
			Collection<SpacePoint> points = getFreePoints();
			for (SpacePoint spacePoint : points) {
				solutions.addAll(allocateFigure(figure, spacePoint));
			}
		}
		return solutions;
	}

	private SpacePoint getNextPoint() {
		return getFreePoint();
//		return getFirstPoint();
	}

	public void putFigure(Figure figure) {
		Iterator<Atom> iterator = figure.atomator();
		while (iterator.hasNext()) {
			Atom next = iterator.next();
			SpacePoint point = getPoint(next.getPoint());
			if (point == null) {
				throw new FilledSpacePointException();
			}
			removePoint(point);
		}
		addFigure(figure);
	}

	private void addFigure(Figure figure) {
		figures.add(figure);
		compactFigures.add(new CompactFigure(figure));
	}

/*
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
*/

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
		return new Point(space.x - figure.x, space.y - figure.y, space.z - figure.z);
	}

	public Set<Figure> figures() {
		if (figures.size() != compactFigures.size()) {
			figures.clear();
			for (CompactFigure compactFigure : compactFigures) {
				figures.add(new Figure(compactFigure, getCubeSize()));
			}
		}
		return figures;
	}

	/**
	 * @return текстовое представление пространства
	 */
	public String getView() {
		return buildStringView();
	}

	private String buildStringView() {
//		String filledSquare = "\u25A0";
//		Point minPoint = getMinPoint();
//		Point maxPoint = getMaxPoint();
		///todo сделать корректное отображение 3D
/*
		StringBuilder coords = new StringBuilder("{");
		for (int y = maxPoint.y; y >= minPoint.y; y--) {
//			String line = "";
			StringBuilder line = new StringBuilder();
			for (int x = minPoint.x; x <= maxPoint.x; x++) {
				for (int z = minPoint.z; z <= maxPoint.z; z++) {
					SpacePoint point = getPoint(x + 1, y + 1, z + 1);
					if (point == null) {
						line.append(" ");
					} else {
						coords.append(point);
						coords.append(",");
						line.append(filledSquare);
					}
				}
			}
//			output.append(line).append("\n");
		}
		if (coords.length() > 1) {
//			coords = coords.substring(1, coords.length() - 1);
			coords.delete(coords.length(), coords.length());
		}
		coords.append("}");*/
		StringBuilder output = new StringBuilder();
		int order = 0;
		for (Figure part : figures()) {
			order++;
			output.append(String.format("\nFigure#%s:\n%s", order, part));
		}
		return output.toString();
	}


	/**
	 * Формирует абстрактную точку с минимальными значеними по всем координатам
	 */
	public Point getMinPoint() {
		return Utils.getMinPoint(points.values());
	}

	/**
	 * Формирует абстрактную точку с максимальными значеними по всем координатам
	 */
	public Point getMaxPoint() {
		return Utils.getMaxPoint(points.values());
	}

	public String getTextView() {
		ResultPrinter printer = new ResultPrinter(this);
		return printer.buildSolutionOutput(this);
	}

	public Integer[][] preSerialize() {
		int figureIndex = 0;
		Integer[][] arrFigures = new Integer[figures().size()][];
		for (Figure figure : figures()) {
			int atomIndex = 0;
			Integer[] arrAtoms = new Integer[figure.atoms().size()];
			for (Atom atom : figure.atoms()) {
				arrAtoms[atomIndex++] = atom.getIndex(getCubeSize());
			}
			arrFigures[figureIndex++] = arrAtoms;
		}
		return arrFigures;
	}
}
