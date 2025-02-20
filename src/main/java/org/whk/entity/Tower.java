package org.whk.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Tower {

    // 所有对象
    private Map<Long, IMapObject> objectMap = new ConcurrentHashMap<>();
    // 订阅对象
    private Map<Long, IMapObject> watchers = new ConcurrentHashMap<>();

    private int x;

    private int y;

    public void clean() {
        objectMap.clear();
        watchers.clear();
    }

}
