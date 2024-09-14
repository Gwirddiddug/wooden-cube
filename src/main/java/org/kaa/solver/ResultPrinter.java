package org.kaa.solver;

import org.kaa.model.*;

import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Форматирует и выводит результаты решения
 */
public class ResultPrinter {

	private final boolean SHOW_FIGURES = true;
	private final RealSpace space;

	public ResultPrinter(RealSpace space) {
		this.space = space;
	}

	public ResultPrinter(Puzzle puzzle) {
		this.space = puzzle.getSpace();
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
			int index = 0;
			TreeSet<CompactFigure> sortedFigures = new TreeSet<>(solution.getCompactFigures());

			for (CompactFigure figure : sortedFigures) {

				index++;
				result.append(String.format("Figure#%s:\t%s", index, buildFigureOutput(figure)));
				result.append("\n");
			}
		}
		return result.toString();
	}

	public void printFigures(Iterable<Figure> figures) {
		int index = 0;

		TreeSet<CompactFigure> sortedFigures = new TreeSet<>();
		for (Figure figure : figures) {
			sortedFigures.add(new RealFigure(figure, space).buildCompact());
		}
		StringWriter result = new StringWriter();
		for (CompactFigure figure : sortedFigures) {
			index++;
			result.append(String.format("Figure#%s:\t%s", index, buildFigureOutput(figure)));
			result.append("\n");
		}
		System.out.println(result);
	}


	private String buildFigureOutput(CompactFigure figure) {
		String collected = figure.getCompactAtoms().stream().map(String::valueOf).collect(Collectors.joining(", "));
		return "{" + collected + "}";
	}


	private String buildFigureOutput(RealFigure part) {
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

}
