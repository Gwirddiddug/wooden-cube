package org.kaa.solver;

import org.kaa.model.Puzzle;
import org.kaa.model.SolutionInfo;

public interface IPuzzleSolver {
	SolutionInfo solve(Puzzle puzzle);
}
