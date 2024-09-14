package org.kaa.solver;

import lombok.Getter;
import lombok.Setter;
import org.kaa.model.Figure;
import org.kaa.model.RealSpace;

import java.util.List;
import java.util.concurrent.Callable;

@Getter
@Setter
public class IterateVariant implements Callable<Variants> {
    private List<Figure> postures;
    private RealSpace solution;

    public IterateVariant(List<Figure> postures) {
        this.postures = postures;
    }

    public IterateVariant(List<Figure> postures, RealSpace solution) {
        this.postures = postures;
        this.solution = solution;
    }

    @Override
    public Variants call() {
//			long start = System.currentTimeMillis();

        Variants newSpaces = new Variants();
        for (Figure posture : postures) {
            List<RealSpace> newSolutions = solution.allocateFigure(posture);
            newSpaces.addAll(newSolutions);
        }
//			System.out.println(System.currentTimeMillis() - start);
        return newSpaces;
    }
}
