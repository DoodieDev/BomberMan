package doodieman.bomberman.maphandler.objects;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.maphandler.MapUtil;
import doodieman.bomberman.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    @Getter
    private final String mapID;

    //Data from the saved config
    @Getter
    private boolean active;
    @Getter
    private Vector corner1;
    @Getter
    private Vector corner2;
    @Getter
    private List<Vector> spawnPoints;

    public GameMap(String mapID) {
        this.mapID = mapID;
        this.loadData();
    }

    private void loadData() {
        ConfigurationSection section = MapUtil.getInstance().getMapSection(mapID);

        this.active = section.getBoolean("active");
        this.corner1 = LocationUtil.stringToVector(section.getString("corner1"));
        this.corner2 = LocationUtil.stringToVector(section.getString("corner2"));

        //Load spawnpoints
        this.spawnPoints = new ArrayList<>();
        for (String key: section.getConfigurationSection("spawnpoints").getKeys(false))
            spawnPoints.add(LocationUtil.stringToVector(section.getString("spawnpoints."+key)));
    }

    public String getSchematicPath() {
        return BomberMan.getInstance().getDataFolder() + "/maps/"+mapID+".schematic";
    }

    //Get the game Y level. Its the same as spawnpoints.
    public int getGameLevel() {
        return this.getSpawnPoints().get(0).getBlockY();
    }

    public Location getCenterLocation() {
        MapUtil util = MapUtil.getInstance();
        Location corner1Location = util.getLocationFromOffset(this.corner1);
        Location corner2Location = util.getLocationFromOffset(this.corner2);
        Location center = LocationUtil.getCenterLocation(corner1Location,corner2Location);
        return center.add(0.5, 0, 0.5);
    }

}
