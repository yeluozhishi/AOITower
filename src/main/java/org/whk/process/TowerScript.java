package org.whk.process;


import org.whk.VIEW_UPDATE;
import org.whk.entity.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class TowerScript implements ITowerScript {

    private final Logger logger = Logger.getLogger(TowerScript.class.getName());


    public static final TowerScript instance = new TowerScript();

    private TowerScript() {
    }

    @Override
    public boolean addObject(TowerAOI towerAOI, IMapObject obj) {
        if (Objects.isNull(obj)) return false;
        Tower tower = getTower(towerAOI, obj.getPoint());
        if (Objects.isNull(tower)) return false;
        tower.getObjectMap().put(obj.getId(), obj);
        return true;
    }

    @Override
    public boolean removeObject(TowerAOI towerAOI, IMapObject obj) {
        if (Objects.isNull(obj)) return false;
        Tower tower = getTower(towerAOI, obj.getPoint());
        if (Objects.isNull(tower)) return false;
        tower.getObjectMap().remove(obj.getId(), obj);
        return false;
    }

    @Override
    public void addWatcher(Tower tower, IMapObject obj) {
        if (Objects.isNull(obj)) return;
        tower.getObjectMap().put(obj.getId(), obj);
        tower.getWatchers().put(obj.getId(), obj);
    }

    @Override
    public void removeWatcher(Tower tower, IMapObject obj) {
        if (Objects.isNull(obj)) return;
        tower.getObjectMap().remove(obj.getId(), obj);
        tower.getWatchers().remove(obj.getId());
    }

    @Override
    public void initTowerAOI(TowerAOI towerAOI, Topography topography) {
        int maxTowerX = Math.ceilDiv(towerAOI.getMapWidth(), towerAOI.getTowerXSize());
        int maxTowerY = Math.ceilDiv(towerAOI.getMapHeight(), towerAOI.getTowerYSize());

        towerAOI.setMaxTowerX(maxTowerX);
        towerAOI.setMaxTowerY(maxTowerY);

        Tower[][] towers = new Tower[maxTowerX][maxTowerY];
        for (int x = 0; x < maxTowerX; x++) {
            for (int y = 0; y < maxTowerY; y++) {
                Tower tower = new Tower();
                tower.setX(x);
                tower.setY(y);
                towers[x][y] = tower;
            }
        }
        towerAOI.setTowers(towers);

        for (Point[] points : topography.getAllPoint()) {
            for (Point point : points) {
                getTower(towerAOI, point);
            }
        }
    }


    @Override
    public Set<Tower> getNearTower(TowerAOI towerAOI, Point point, Topography topography, int halfWidth, int halfHeight) {
        Set<Tower> towers = new HashSet<>();
        // 视野宽和高一半，用来计算四个顶点
        int leftX = Math.max(point.getX() - halfWidth, 0);
        int rightX = Math.min(point.getX() + halfWidth, topography.getWidth());
        int topY = Math.min(point.getY() + halfHeight, topography.getHeight());
        int bottomY = Math.min(point.getY() - halfHeight, 0);
        // 获取四个顶点
        towers.add(getTower(towerAOI, topography.getAllPoint()[leftX][topY]));
        towers.add(getTower(towerAOI, topography.getAllPoint()[leftX][bottomY]));
        towers.add(getTower(towerAOI, topography.getAllPoint()[rightX][topY]));
        towers.add(getTower(towerAOI, topography.getAllPoint()[rightX][bottomY]));
        return towers;
    }

    public Tower getTower(TowerAOI towerAOI, Point point) {
        if (Objects.nonNull(point.getTower())) return point.getTower();
        int towerX = Math.floorDiv(point.getX(), towerAOI.getTowerXSize());
        int towerY = Math.floorDiv(point.getY(), towerAOI.getTowerYSize());
        if (towerX >= towerAOI.getMaxTowerX() || towerY >= towerAOI.getMaxTowerY()) {
            logger.severe("获取灯塔失败，地图id：%s，x=%d，y=%d".formatted(towerAOI.getSceneId(), point.getX(), point.getY()));
            return null;
        }
        point.setTower(towerAOI.getTowers()[towerX][towerY]);
        return point.getTower();
    }

    @Override
    public void moveToNextPoint(TowerAOI towerAOI, IMapObject obj, Topography topography, Point nextPoint) {
        if (Objects.isNull(nextPoint) || nextPoint.isBlock() || nextPoint == obj.getPoint()) return;
        if (!isNearPoint(obj.getPoint(), nextPoint)) return;
        // 视野宽和高一半
        int halfWidth = obj.getView().getWidth() / 2;
        int halfHeight = obj.getView().getHeight() / 2;
        Set<Tower> oldTowers = getNearTower(towerAOI, obj.getPoint(), topography, halfWidth, halfHeight);
        Set<Tower> newTowers = getNearTower(towerAOI, nextPoint, topography, halfWidth, halfHeight);
        Set<Tower> commonTowers = new HashSet<>(oldTowers);
        commonTowers.retainAll(newTowers);

        oldTowers.removeAll(commonTowers);
        for (Tower oldTower : oldTowers) {
            removeWatcher(oldTower, obj);
            updateView(oldTower, obj, VIEW_UPDATE.REMOVE);
        }

        for (Tower commonTower : commonTowers) {
            updateView(commonTower, obj, VIEW_UPDATE.UPDATE);
        }

        newTowers.removeAll(commonTowers);
        for (Tower newTower : newTowers) {
            addWatcher(newTower, obj);
            updateView(newTower, obj, VIEW_UPDATE.ADD_NEW);
        }
        obj.setPoint(nextPoint);
    }

    public void updateView(Tower tower, IMapObject obj, VIEW_UPDATE viewUpdate) {
        switch (viewUpdate) {
            case REMOVE -> viewRemove(tower, obj);
            case UPDATE -> viewUpdate(tower, obj);
            case ADD_NEW -> viewAddNew(tower, obj);
            default -> throw new IllegalStateException("不支持的枚举%s".formatted(viewUpdate.getDesc()));
        }
    }

    public void viewUpdate(Tower tower, IMapObject obj) {

    }

    public void viewRemove(Tower tower, IMapObject obj) {

    }

    public void viewAddNew(Tower tower, IMapObject obj) {

    }

    public boolean isNearPoint(Point origin, Point targetPoint) {
        if (origin == targetPoint) return false;
        return origin.getX() - targetPoint.getX() <= 1 && origin.getX() - targetPoint.getX() >= -1 &&
                origin.getY() - targetPoint.getY() <= 1 && origin.getY() - targetPoint.getY() >= -1;

    }
}
