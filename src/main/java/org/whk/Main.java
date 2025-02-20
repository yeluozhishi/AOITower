package org.whk;

import org.whk.entity.DefaultScene;
import org.whk.entity.MapDef;

public class Main {
    public static void main(String[] args) {
        MapDef mapDef = new MapDef();
        mapDef.setId(101);
        mapDef.setData(101);
        mapDef.setHeight(358);
        mapDef.setWidth(417);
        mapDef.setLine(1);
        DefaultScene scene = new DefaultScene(mapDef);
        scene.init();
        System.out.println(scene);
    }
}