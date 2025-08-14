package org.kaa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class CompactFigure implements Comparable<CompactFigure> {

	Set<Integer> compactAtoms = new HashSet<>();

	public CompactFigure(Set<Integer> atoms) {
		this.compactAtoms = atoms;
	}

	public void addPoint(int key) {
		compactAtoms.add(key);
	}

		@Override
	public int compareTo(CompactFigure o) {
			int smaller = Integer.compare(this.getParrot(), o.getParrot());
            if (smaller != 0) {
				return smaller;
			}
            return Integer.compare(this.getMin(), o.getMin());
        }

	//суммирует индексы точек, чтобы определить какая фигура должна быть раньше в сортировке.
    public int getParrot() {
		return compactAtoms.stream().map(p->p*p).mapToInt(Integer::intValue).sum();
	}

	//ищет точку с минимальным индексом
	private int getMin() {
		int min = Integer.MAX_VALUE;
		for (Integer atom : compactAtoms) {
			min = Math.min(min, atom);
		}
		return min;
	}
}
