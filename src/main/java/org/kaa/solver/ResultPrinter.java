package org.kaa.solver;

import org.kaa.model.Figure;
import org.kaa.model.Point;
import org.kaa.model.Puzzle;
import org.kaa.model.RealSpace;

import java.util.List;
import java.util.Set;

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
			for (Figure part : solution.figures()) {
				order++;
				result.append(String.format("Figure#%s:\t%s", order, buildFigureOutput(part)));
				result.append("\n");
			}
		}
		return result.toString();
	}

	private String buildFigureOutput(Figure part) {
		StringBuffer output = new StringBuffer();
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
