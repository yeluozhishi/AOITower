package org.whk.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

@Getter
@Setter
public class DefaultScene extends AbstractScene{

    private Logger logger = Logger.getLogger(DefaultScene.class.getName());

    private String sceneId;

    private MapDef mapDef;

    private Topography topography;

    private TowerAOI towerAOI;

    public DefaultScene(MapDef mapDef) {
        this.mapDef = mapDef;
        sceneId = "%d_%d".formatted(mapDef.getId(), mapDef.getLine());
        topography = new Topography(mapDef);
        towerAOI = new TowerAOI(sceneId, 100, 100, mapDef);
    }

    public void init(){
        topography.init(mapDef);
        towerAOI.init(topography);
    }


    @Override
    public void sceneTick() {
//        logger.info("%s 场景事件处理, 线程：%s".formatted(sceneId, Thread.currentThread().getName()));
    }
}
