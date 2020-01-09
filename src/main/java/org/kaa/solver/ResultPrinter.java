package org.kaa.solver;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import org.kaa.model.Figure;
import org.kaa.model.Point;
import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Форматирует и выводит результаты решения
 */
public class ResultPrinter {

	private final boolean SHOW_FIGURES = true;
	private int measure = 5;

	public ResultPrinter(RealSpace space) {
		measure = space.getCubeSize();
	}

	public ResultPrinter(Puzzle puzzle) {
		measure = puzzle.getSpace().getCubeSize();
	}

	public void printSolution(RealSpace solution) {
		System.out.println(buildSolutionOutput(solution));
	}

	public void print(List<? extends RealSpace> solutions) {
		System.out.println("\n==========================");
		System.out.println(String.format("Variants(%s):", solutions.size()));

		int index = 0;
		for (RealSpace solution : solutions) {
			System.out.println("--------------------------");
			System.out.println(String.format("Variant#%s:", ++index));
			printSolution(solution);
		}
		System.out.println();
	}


	public String buildSolutionOutput(RealSpace solution) {
		StringBuilder result = new StringBuilder();

		if (solution.countEmpty() > 0) {
			result.append(String.format("size:%s, empty:%s, filled:%s",
					solution.size(), solution.countEmpty(), solution.countFilled()));
			result.append("\n");
		}

		if (SHOW_FIGURES) {
			int order = 0;
			Set<Figure> figures = solution.figures();

			TreeSet<CompactFigure> sortedFigures = figures.stream().map(CompactFigure::new).collect(Collectors.toCollection(TreeSet::new));

			for (CompactFigure figure : sortedFigures) {
				order++;
				result.append(String.format("Figure#%s:\t%s", order, buildFigureOutput(figure)));
				result.append("\n");
			}
		}
		return result.toString();
	}

	@Getter
	private class CompactFigure implements Comparable<CompactFigure> {
		TreeSet<Integer> compactAtoms = new TreeSet<>();

		public CompactFigure(@NotNull Figure figure) {
			compactAtoms.addAll(figure.getCompactAtoms());
		}

		@Override
		public int compareTo(CompactFigure o) {
			return Integer.compare(this.compactAtoms.first(), o.getCompactAtoms().first());
		}
	}



	private String buildFigureOutput(CompactFigure figure) {
		String collected = figure.getCompactAtoms().stream().map(String::valueOf).collect(Collectors.joining(", "));
		return "{" + collected + "}";
	}


	private String buildFigureOutput(Figure part) {
		StringBuilder output = new StringBuilder();
		output.append("{");

		Set<Integer> compactAtoms = part.getCompactAtoms();
		for (Integer compactAtom : compactAtoms) {
			output.append(output.length() > 1 ? ", " : "");
			output.append(compactAtom);
		}

		output.append("}");
		return part.getName() + output;
	}

	public String buildPointOutput(Point point) {
		return String.valueOf(point.getIndex(measure));
//        return String.format("\n(%s,%s,%s)", point.x+1, point.y+1, point.z+1);
	}


}
