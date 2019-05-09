package org.kaa.solver;

import org.kaa.model.*;

/**
 * Форматирует и выводит результаты решения
 */
public class ResultPrinter {

    private int measure = 5;
    private final boolean SHOW_FIGURES = true;

    public ResultPrinter(RealSpace space) {
        measure = space.getCubeSize();
    }

    public ResultPrinter(Puzzle puzzle) {
        measure = puzzle.getSpace().getCubeSize();
    }

    public void printSolution(RealSpace solution) {
        System.out.println(buildSolutionOutput(solution));
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
        for (Atom atom : part.atoms()) {
            Point point = atom.getPoint();
            output.append(output.length() > 1 ? ", " : "");
            output.append(buildPointOutput(point));
        }
        output.append("}");
        return part.getName() + output;
    }

    public String buildPointOutput(Point point) {
        return String.valueOf(point.getIndex(measure));
//        return String.format("\n(%s,%s,%s)", point.x+1, point.y+1, point.z+1);
    }


}
