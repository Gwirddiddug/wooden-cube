package org.kaa.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Typhon
 * @since 01.11.2014
 * Представление заполняемой фигуры (полости)
 * Работает только с положительными точками
 */
public abstract class Space implements Serializable {

    protected HashMap<Integer, SpacePoint> points = new HashMap<>();
    protected int cubeSize = 0; //размер куба в котором размещена фигура

    private String name = "unnamed";

    protected Space() {
        name = this.getClass().getSimpleName();
    }

    protected Space(int cubeSize) {
        this();
        this.cubeSize = cubeSize+1;
    }

    public int getSize() {
        return points.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * проверяет попадает ли точка в куб и увеличивает его при необходимости
     */
    private void checkCubeSize(Point point) {
        int maxCoordinate = Integer.max(Integer.max(point.x, point.y), point.z);
        if (maxCoordinate >= cubeSize) {
            cubeSize = maxCoordinate+1;
        }
        recalculatePoints();
    }

    private void recalculatePoints() {
        HashMap<Integer, SpacePoint> newMap = new HashMap<>();
        points.values();
        for (SpacePoint spacePoint : points.values()) {
            int key = getPointKey(spacePoint);
            newMap.put(key, spacePoint);
        }
        points = newMap;
    }

    private int getPointKey(int x, int y, int z) {
        if (x<0 || y<0 || z<0) return -1;
        if (x>=cubeSize || y>=cubeSize || z>=cubeSize) return -1;
        return cubeSize*cubeSize * x + cubeSize * y + z;
    }

    private int getPointKey(Point point) {
        return getPointKey(point.x, point.y, point.z);
    }

    /**
     * check containing point by space
     * @param point point description
     * @return true, if contain
     */
    public boolean hasPoint(Point point) {
        int pointKey = getPointKey(point);
        if (pointKey<0) {
            return false;
        }
        return points.containsKey(pointKey);
    }

    public boolean addPoint(Point point) {
        SpacePoint spacePoint = new SpacePoint(point);
        return addPoint(spacePoint);
    }

    public boolean addPoint(SpacePoint point) {
        int key = getPointKey(point);
        if (key >= 0 && !points.containsKey(key)){
            points.put(key, point);
            return true;
        } else {
//            System.out.println(point);
            return false;
        }
    }

    public void removePoint(Point point) {
        points.remove(getPointKey(point));
    }

    public SpacePoint getPoint(Point point){
        return points.get(getPointKey(point));
    }

    public SpacePoint getPoint(int x, int y, int z) {
        return points.get(getPointKey(x, y, z));
    }

    public Iterator<SpacePoint> iterator() {
        return points.values().iterator();
    }

    protected boolean hasEnoughSpaceFor(Figure figure){
        Iterator<Atom> iterator = figure.atomator();
        boolean enough = true;
        while (iterator.hasNext() && enough) {
            Atom next = iterator.next();
            enough &= hasPoint(next);
        }
        return enough;
    }

    /**
     * @return next free point in space
     */
    public SpacePoint getFreePoint() {
/*        SpacePoint spacePoint = getPoint(0,0,0);
        if (spacePoint!=null) return spacePoint;

        spacePoint = getPoint(0,0,3);
        if (spacePoint!=null) return spacePoint;

        spacePoint = getPoint(0, 1, 4);
        if (spacePoint!=null) return spacePoint;
        spacePoint = getPoint(4, 0, 0);
        if (spacePoint!=null) return spacePoint;
        spacePoint = getPoint(4, 0, 4);
        if (spacePoint!=null) return spacePoint;
        spacePoint = getPoint(4, 1, 0);
        if (spacePoint!=null) return spacePoint;

        spacePoint = getPoint(4, 2, 2);
        if (spacePoint!=null) return spacePoint;
        spacePoint = getPoint(4, 4, 4);
        if (spacePoint!=null) return spacePoint;*/

        for (SpacePoint point : points.values()) {
            if (point.isEmpty()) {
                return point;
            }
        }
        return null;
    }

    public SpacePoint getFirstPoint(){
        if (points.size()>0) {
            Set<Integer> integers = points.keySet();
            return points.get(integers.toArray()[0]);
        }
        return null;
    }


    protected List<SpacePoint> getFreePoints() {
        return points.values().stream().filter(p -> p.isEmpty()).collect(Collectors.toList());
    }

    public int size() {
        return points.size();
    }

    public int countEmpty() {
        return size();
    }

    public int countFilled() {
        return cubeSize*cubeSize*cubeSize-size();
    }

    public boolean equals(Space space) {
        if (space.cubeSize!=cubeSize) return false;
        if (space.size()!=size()) return false;

        for (Integer key : space.points.keySet()) {
            if (!points.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
