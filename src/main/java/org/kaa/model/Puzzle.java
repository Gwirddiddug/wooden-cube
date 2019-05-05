package org.kaa.model;

import org.kaa.utils.IOUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Typhon
 * @since 23.11.2014
 * Представление задачи
 */
public class Puzzle {

    //заполняемая область
    private RealSpace space = new RealSpace(0);
    //фигура, которой заполняется область
    private Figure figure = new Figure();
    //полученные решения
    private List<Solution> solutions = new LinkedList<>();

    public void setSpace(RealSpace space) {
        this.space = space;
    }

    public RealSpace getSpace() {
        return space;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public Figure getFigure() {
        return figure;
    }

    public void addSolution (Solution solution){
        solutions.add(solution);
        saveSolutionToFile(solution);
    }

    private void saveSolutionToFile(Solution solution) {
        IOUtils.saveSolution(solution, "solutions.sol");
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void save(){
        for (Solution solution : solutions) {
            saveSolutionToFile(solution);
        }
    }

}
