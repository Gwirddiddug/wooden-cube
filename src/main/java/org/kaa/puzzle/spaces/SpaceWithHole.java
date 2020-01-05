package org.kaa.puzzle.spaces;

import org.kaa.model.Point;
import org.kaa.model.RealSpace;

/**
 * @author Typhon
 * @since 23.11.2014
 */
public class SpaceWithHole extends RealSpace {

	int xSize = 4;
	int ySize = 2;
	int zSize = 4;

	public SpaceWithHole() {
		super(4);
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				for (int k = 0; k < zSize; k++) {
					addPoint(new Point(i, j, k));
				}
			}
		}

		removePoint(new Point(1, 0, 1));
		removePoint(new Point(1, 1, 1));
	}
}
