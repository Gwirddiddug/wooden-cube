package org.kaa.utils;

import org.kaa.model.Atom;
import org.kaa.model.Figure;
import org.kaa.model.Point;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Typhon
 * @since 25.11.2014
 */
public class FigureUtils {

	private final static Point zeroPoint = new Point(0, 0, 0);

	/**
	 * Формирует список всех вариантов изначальной фигуры полученных вращением по разным осям.
	 *
	 * @return список положений фигуры
	 */
	public static List<Figure> buildPostures(Figure figure) {

		class Postures extends LinkedList<Figure> {
			@Override
			public boolean add(Figure figure) {
				if (!isIn(figure)) {
					return super.add(figure);
				}
				return false;
			}

			@Override
			public boolean addAll(Collection<? extends Figure> c) {
				boolean added = true;
				for (Figure figure : c) {
					added &= add(figure);
				}
				return added;
			}

			private boolean isIn(Figure figure) {
				for (Figure figure1 : this) {
					if (equalsByPoints(figure, figure1)) {
						return true;
					}
				}
				return false;
			}

			private boolean equalsByPoints(Figure figure1, Figure figure2) {
				boolean isEquals;
				isEquals = figure1.atoms().size() == figure2.atoms().size();
				if (!isEquals) return false;
				for (Atom atom : figure1.atoms()) {
					isEquals &= figure2.getAtom(atom.getPoint()) != null;
					if (!isEquals) return false;
				}
				return isEquals;
			}
		}

		Postures allPostures = new Postures();
		//добавляем исходную фигуру
		allPostures.add(figure);
		Figure rt90 = FigureUtils.rotateX(figure);
		Figure rt180 = FigureUtils.rotateX(rt90);
		Figure rt270 = FigureUtils.rotateX(rt180);

		allPostures.add(rt90);
		allPostures.add(rt270);
		allPostures.add(rt180);
		allPostures.add(FigureUtils.rotateY(figure));
		allPostures.add(FigureUtils.rotateY270(figure));

		//вращаем по оси Z все имеющиеся фигуры
		List<Figure> postures = new LinkedList<>();

		for (Figure posture : allPostures) {
			rt90 = FigureUtils.rotateZ(posture);
			postures.add(rt90);
			rt180 = FigureUtils.rotateZ(rt90);
			postures.add(rt180);
			rt270 = FigureUtils.rotateZ(rt180);
			postures.add(rt270);
		}
		allPostures.addAll(postures);
		return allPostures;
	}


	static Figure rotateX270(Figure figure) {
		Figure rt90 = FigureUtils.rotateX(figure);
		Figure rt180 = FigureUtils.rotateX(rt90);
		Figure rt270 = FigureUtils.rotateX(rt180);
		return rt270;//.recalculateForNewZeroPoint(zeroPoint);
	}

	static Figure rotateY270(Figure figure) {
		Figure rt90 = FigureUtils.rotateY(figure);
		Figure rt180 = FigureUtils.rotateY(rt90);
		Figure rt270 = FigureUtils.rotateY(rt180);
		return rt270.recalculateForNewZeroPoint(zeroPoint);
	}

	/**
	 * поворачивает плоскую фигуру на 90 градусов по часовой стрелке вокруг оси Z
	 *
	 * @param figure
	 * @return
	 */
	static Figure rotateZ(Figure figure) {
		Point minPoint = Utils.getMinPoint(figure.atoms());
		Point maxPoint = Utils.getMaxPoint(figure.atoms());

		Figure rotated = figure.clone(true);
		rotated.incZRC();
		for (int z = minPoint.z; z <= maxPoint.z; z++) {
			for (int x = minPoint.x; x <= maxPoint.x; x++) {
				for (int y = minPoint.y; y <= maxPoint.y; y++) {
					if (figure.getAtom(new Point(x, y, z)) != null) {
						rotated.addAtom(new Atom(new Point(y, maxPoint.x - x, z)));
					}
				}
			}
		}
		return rotated.recalculateForNewZeroPoint(zeroPoint);
	}

	/**
	 * поворачивает плоскую фигуру на 90 градусов по часовой стрелке вокруг оси X
	 *
	 * @param figure
	 * @return
	 */
	static Figure rotateX(Figure figure) {
		Point minPoint = Utils.getMinPoint(figure.atoms());
		Point maxPoint = Utils.getMaxPoint(figure.atoms());

		Figure rotated = figure.clone(true);
		rotated.incXRC();
		for (int x = minPoint.x; x <= maxPoint.x; x++) {
			for (int z = minPoint.z; z <= maxPoint.z; z++) {
				for (int y = minPoint.y; y <= maxPoint.y; y++) {
					if (figure.getAtom(new Point(x, y, z)) != null) {
						rotated.addAtom(new Atom(new Point(x, maxPoint.z - z, y)));
					}
				}
			}
		}
//        return rotated.recalculateForNewZeroPoint(zeroPoint);
		return rotated;
	}


	/**
	 * поворачивает плоскую фигуру на 90 градусов по часовой стрелке вокруг оси Y
	 *
	 * @param figure
	 * @return
	 */
	static Figure rotateY(Figure figure) {
		Point minPoint = Utils.getMinPoint(figure.atoms());
		Point maxPoint = Utils.getMaxPoint(figure.atoms());

		Figure rotated = figure.clone(true);
		rotated.incYRC();
		for (int y = minPoint.y; y <= maxPoint.y; y++) {
			for (int z = minPoint.z; z <= maxPoint.z; z++) {
				for (int x = minPoint.x; x <= maxPoint.x; x++) {
					if (figure.getAtom(new Point(x, y, z)) != null) {
						rotated.addAtom(new Atom(new Point(maxPoint.z - z, y, x)));
					}
				}
			}
		}
		return rotated.recalculateForNewZeroPoint(zeroPoint);
	}


}
