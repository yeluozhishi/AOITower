## Area Of Interest

    

### 灯塔

    灯塔AOI是把整个地图划分成比较大的格子，每个格子称为一个灯塔，玩家视野一般涉及上下左右4个灯塔
    （之所以不是周边的9个而是4个是因为灯塔必须大于玩家的视野，因此偏向左下方就查左下方那4个格子即可，不用查9个，其他依此类推）。
    地图中会有很多个点，每个点归属于对应的灯塔。当该点上有变更并且需要通知到附近时，可通知到灯塔中的订阅对象。
      优点：
       1. 将多个点上的相同对象统一管理。
       2. 不强依赖每个点。
       3. 减少对象数量，减少内存使用。
       4. 可以根据负载和使用情况，调整灯塔数量。

    
灯塔的地图拆分

        除法都向下取整  
        map 下标  
        x = 13  
        y = 28  
        
        如果tower的大小为4*5
        map上的点转tower下标  
        towerx = mapx / 4  
        towery = mapy / 5  
        
        
获取周围灯塔

      灯塔大小一定要大于视野，这样最多获取四个灯塔的数据。
      当视野区域处于边界时，获取当前tower和跨界的tower。视野区域大小为(3,3)。
         0 1 2 3   4 5  x
       0 1 1 1 1 | 1 1
       1 1 1 1 1 | 1 1
       2 1 1 1 1 | 1 1               
       3 1 1 1 1 | 1 1
       ---------------
       4 1 1 1 1 | 1 1
       y
      
      方法 
      取两次视野四个顶点，比较两次tower。

移动至下一个点的代码：

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

获取该点附近的tower代码：

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
        
        
        
        

### 视野
    地图上铺满point，角色拥有视野，根据点位和视野范围获取周围点上的所有数据。
    具体为视野对象，移动后，变动point，新增point就推送新增point上的对象，删除point就推送移除point上的对象。
    同一地图前端point需要浓密些，后端稀疏些。比如前：1000*1000 后100*100