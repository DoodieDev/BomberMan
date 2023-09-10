package doodieman.bomberman.spawn;

import doodieman.bomberman.maphandler.MapUtil;
import doodieman.bomberman.maphandler.objects.GameMap;
import doodieman.bomberman.utils.LocationUtil;
import org.bukkit.Location;

public class SpawnUtil {

    public static Location getSpawnLocation() {
        MapUtil mapUtil = MapUtil.getInstance();
        GameMap currentMap = mapUtil.getCurrentMap();

        Location corner1 = mapUtil.getLocationFromOffset(currentMap.getCorner1());
        Location corner2 = mapUtil.getLocationFromOffset(currentMap.getCorner2());

        Location center = LocationUtil.getCenterLocation(corner1,corner2);
        center.setY(corner2.getY() + 1);

        return center;
    }

}
