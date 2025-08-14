package org.kaa.model;

import lombok.Getter;
import org.kaa.exceptions.FilledSpacePointException;
import org.kaa.solver.ResultPrinter;
import org.kaa.utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Typhon
 * @since 22.11.2014
 * Базовое представление для пространства размещения
 */
@Getter
public class RealSpace extends Space implements Serializable {

	protected Set<CompactFigure> compactFigures = new HashSet<>();
	protected int level = 0;

	public RealSpace(int x, int y, int z) {
		super(x, y, z);
	}

	public RealSpace(RealSpace space) {
		super(space.getAbscissus(), space.getOrdinatus(), space.getApplicata());
		this.compactFigures = space.getCompactFigures();
	}

	public RealSpace(int i) {
		super(i, i, i);
	}

	public RealSpace buildClone() {
		RealSpace clone = new RealSpace(abscissus, ordinatus, applicata);
		clone.points = new HashMap<>(points);
		clone.compactFigures = new HashSet<>(compactFigures);
		return clone;
	}

	@Override
	protected int getPointKey(Point point) {
		return getPointKey(getCubeSize(), point);
	}

	public int getCubeSize() {
		return cubeSize - 1;
	}

	/**
	 * Определяет использовать оптимистичный сценарий заполнения или пессимистичный
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

		//если количество ячеек пространства не кратно количеству ячеек фигуры,
		// то перебор по первой точке не даст максимального заполнения
		//		if (optimisticCase(figure)) {
//		} else {
//			Collection<SpacePoint> points = getFreePoints();
//			for (SpacePoint spacePoint : points) {
//				solutions.addAll(allocateFigure(figure, spacePoint));
//			}
//		}
		return allocateFigure(figure, getFreePoint());
	}

	public void putFigure(Figure figure) {
		putFigure(new RealFigure(figure, this));
	}

	public void putFigure(RealFigure figure) {
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

	public void addCompactFigure(CompactFigure figure) {
		compactFigures.add(figure);
	}

	private void addFigure(RealFigure figure) {
		compactFigures.add(figure.buildCompact());
		level = compactFigures.size();
	}

	private Point getNewZeroPoint(Point space, Point figure) {
		return new Point(space.x - figure.x, space.y - figure.y, space.z - figure.z);
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
	public int getMaxPointKey() {
		int maxKey = 0;
		for (CompactFigure figure : compactFigures) {
			for (int key : figure.compactAtoms) {
				maxKey = Math.max(maxKey, key);
			}
		}
		return maxKey;
	}

	public String getTextView() {
		ResultPrinter printer = new ResultPrinter(this);
		return printer.buildSolutionOutput(this);
	}

	public Integer[][] preSerialize() {
		int figureIndex = 0;
		Integer[][] arrFigures = new Integer[this.compactFigures.size()][];
		for (CompactFigure figure : this.compactFigures) {
			int atomIndex = 0;
			Integer[] arrAtoms = new Integer[figure.getCompactAtoms().size()];
			for (int atom : figure.getCompactAtoms()) {
				arrAtoms[atomIndex++] = atom;
			}
			arrFigures[figureIndex++] = arrAtoms;
		}
		return arrFigures;
	}
}
