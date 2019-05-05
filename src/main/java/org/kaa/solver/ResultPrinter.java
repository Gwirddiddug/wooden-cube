package org.kaa.solver;

import org.kaa.model.*;

/**
 * Форматирует и выводит результаты решения
 */
public class ResultPrinter {

    private final boolean SHOW_FIGURES = true;

    private Puzzle puzzle;

    public ResultPrinter(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public void printSolution(RealSpace solution) {
        if (solution.countEmpty() > 0) {
            System.out.println(String.format("size:%s, empty:%s, filled:%s",
                    solution.size(), solution.countEmpty(), solution.countFilled()));
        }

        if (SHOW_FIGURES) {
            int order = 0;
            for (Figure part : solution.figures()) {
                order++;
                System.out.println(String.format("Figure#%s:\n%s", order, buildFigureOutput(part)));
            }
        }
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

    private String buildPointOutput(Point point) {
        return String.valueOf(point.getIndex(5));
//        return String.format("\n(%s,%s,%s)", point.x+1, point.y+1, point.z+1);
    }


}
