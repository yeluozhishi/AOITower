package org.whk.entity;

/**
 * 地图中的对象
 */
public interface IMapObject {
    long getId();

    Point getPoint();

    void setPoint(Point point);

    View getView();
}
