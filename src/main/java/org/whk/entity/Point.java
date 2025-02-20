package org.whk.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point {
    private int x;

    private int y;

    /**
     * 是否正常点
     */
    private boolean normal;
    /**
     * 是否是可走点 可走点是false
     */
    private boolean block;
    /**
     * 是否安全点
     */
    private boolean safe;
    /**
     * 可变化安全点
     */
    private boolean modifySafe;
    /**
     * 是否出生点
     */
    private boolean born;

    private boolean transmit;
    /**
     * 周围点
     */
    private Point[] nears;

    private Tower tower;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
