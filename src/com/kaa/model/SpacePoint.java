package com.kaa.model;

import java.io.Serializable;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представляет точку принадлежащую определённой области
 */
public class SpacePoint extends Point implements Serializable{
    private boolean isEmpty = true;

    public SpacePoint(Point point) {
        super(point.x, point.y, point.z);
    }

    public SpacePoint(int x, int y, int z) {
        super(x, y, z);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    @Override
    protected SpacePoint clone() {
        SpacePoint clone = new SpacePoint(super.clone());
        clone.setEmpty(isEmpty);
        return clone;
    }
}
