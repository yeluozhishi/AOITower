package org.whk.entity;

import lombok.Getter;
import lombok.Setter;
import org.whk.process.TowerScript;

/**
 * 单个地图的tower管理器
 */
@Getter
@Setter
public class TowerAOI {
    /**
     * 灯塔
     */
    private Tower[][] towers;

    private String sceneId;

    private int mapHeight;

    private int mapWidth;

    private int towerXSize;

    private int towerYSize;


    private int maxTowerX;

    private int maxTowerY;

    public TowerAOI(String sceneId, int towerXSize, int towerYSize, MapDef mapDef) {
        this.sceneId = sceneId;
        this.mapHeight = mapDef.getHeight();
        this.mapWidth = mapDef.getWidth();
        this.towerXSize = towerXSize;
        this.towerYSize = towerYSize;
    }

    public void init(Topography topography) {
        TowerScript.instance.initTowerAOI(this, topography);
    }

}
