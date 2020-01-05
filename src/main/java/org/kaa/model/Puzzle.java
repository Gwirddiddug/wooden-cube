package org.kaa.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kaa.solver.PuzzleSolver;
import org.kaa.utils.FigureUtils;
import org.kaa.utils.IOUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Typhon
 * @since 23.11.2014
 * Представление задачи
 */
@Getter
@Setter
@Slf4j
public class Puzzle {

	//заполняемая область
	private RealSpace space = new RealSpace(0);
	//фигура, которой заполняется область
	private Figure figure = new Figure();

	List<Figure> postures = FigureUtils.buildPostures(figure);

	//полученные решения
	private List<Solution> solutions = new LinkedList<>();

	public void setFigure(Figure figure) {
		this.figure = figure;
		this.postures = FigureUtils.buildPostures(figure);
	}

	public void addSolution(Solution solution) {
		solutions.add(solution);
		saveSolutionToFile(solution);
	}

	private void saveSolutionToFile(Solution solution) {
		IOUtils.saveSolution(solution, PuzzleSolver.SOLUTION_FILE);
	}

	public void save() {
		for (Solution solution : solutions) {
			saveSolutionToFile(solution);
		}
	}

	public boolean validate() {
		if (getSpace().size() % getFigure().size() > 0) {
			log.info("unsolvable: {} and {}", getSpace().size(), getFigure().size());
			return false;
		}
		return true;
	}
}
