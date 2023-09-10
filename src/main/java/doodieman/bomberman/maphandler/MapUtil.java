package doodieman.bomberman.maphandler;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.maphandler.objects.GameMap;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MapUtil {


    final MapHandler handler;
    @Getter
    private static MapUtil instance;

    public MapUtil(MapHandler handler) {
        this.handler = handler;

        instance = this;
    }

    public ConfigurationSection getMapSection(String mapID) {
        return BomberMan.getInstance().getDataConfig().getConfigurationSection("maps."+mapID);
    }
    public boolean doesMapExist(String mapID) {
        return BomberMan.getInstance().getDataConfig().contains("maps."+mapID);
    }

    public GameMap getMap(String mapID) {
        if (!doesMapExist(mapID))
            return null;
        return new GameMap(mapID);
    }

    public GameMap getCurrentMap() {
        return this.handler.getCurrentMap();
    }

    public Location getCurrentCorner1() {
        return this.handler.getCurrentCorner1().clone();
    }
    public Location getCurrentCorner2() {
        return this.handler.getCurrentCorner2().clone();
    }

    public List<GameMap> getAllMaps() {
        ConfigurationSection section = BomberMan.getInstance().getDataConfig().getConfigurationSection("maps");
        List<GameMap> maps = new ArrayList<>();
        for (String mapID : section.getKeys(false)) {
            maps.add(new GameMap(mapID));
        }
        return maps;
    }

    public List<GameMap> getActiveMaps() {
        return this.getAllMaps()
            .stream()
            .filter(GameMap::isActive)
            .collect(Collectors.toList());
    }

    //Gets all maps where there is enough space for the playercount
    //The maps are sorted by spawnpoints amount, low to high
    public List<GameMap> getPossibleMaps(int playerCount) {
        return this.getActiveMaps()
            .stream()
            .filter(map -> playerCount <= map.getSpawnPoints().size())
            .sorted(Comparator.comparingInt(map -> map.getSpawnPoints().size()))
            .collect(Collectors.toList());
    }

    public GameMap getAppropiateMap(int playerCount) {
        List<GameMap> possibleMaps = this.getPossibleMaps(playerCount);
        List<GameMap> mapPool = new ArrayList<>();
        int targetSpawnLocs = possibleMaps.get(0).getSpawnPoints().size();
        //Add all maps where the spawnLocation size is the same as targetSpawnLocs
        for (GameMap map : possibleMaps) {
            if (map.getSpawnPoints().size() != targetSpawnLocs) continue;
            mapPool.add(map);
        }
        //Get random map out of the mapPool
        int random = ThreadLocalRandom.current().nextInt(mapPool.size());
        return mapPool.get(random);
    }

    public Location getLocationFromOffset(Vector offset) {
        Location location = this.handler.getZeroLocation().clone();
        location.add(offset);
        return location;
    }

    public void switchMap(GameMap newMap) {
        this.handler.removeMapSchematic();
        this.handler.setCurrentMap(newMap);
        this.handler.pasteMapSchematic();
    }

}
