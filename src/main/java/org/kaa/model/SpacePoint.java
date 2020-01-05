package org.kaa.model;

import java.io.Serializable;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представляет точку принадлежащую определённой области
 */
public class SpacePoint extends Point implements Serializable {

	public SpacePoint(Point point) {
		super(point.x, point.y, point.z);
	}

	public SpacePoint(int x, int y, int z) {
		super(x, y, z);
	}

	@Override
	protected SpacePoint clone() {
		return new SpacePoint(super.clone());
	}
}
