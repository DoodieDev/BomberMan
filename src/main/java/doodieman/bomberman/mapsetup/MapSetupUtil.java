package doodieman.bomberman.mapsetup;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.bomberman.BomberMan;
import doodieman.bomberman.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class MapSetupUtil {

    @Getter
    private static MapSetupUtil instance;

    final MapSetupHandler handler;

    public MapSetupUtil(MapSetupHandler handler) {
        this.handler = handler;

        instance = this;
    }


    public boolean doesMapExist(String mapID) {
        return BomberMan.getInstance().getDataConfig().contains("maps."+mapID);
    }

    public void createMap(String mapID) {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();

        dataConfig.createSection("maps."+mapID);
        ConfigurationSection section = dataConfig.getConfigurationSection("maps."+mapID);

        section.set("active", false);
        section.createSection("spawnpoints");

        BomberMan.getInstance().saveDataConfig();
    }

    public void deleteMap(String mapID) {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();

        dataConfig.set("maps."+mapID,null);

        BomberMan.getInstance().saveDataConfig();
    }

    public ConfigurationSection getMapSection(String mapID) {
        return BomberMan.getInstance().getDataConfig().getConfigurationSection("maps."+mapID);
    }

    public void saveSchematic(String mapID, Location corner1, Location corner2) {
        ConfigurationSection section = this.getMapSection(mapID);

        //Save locations
        section.set("realZeroLocation", LocationUtil.locationToString(corner1));
        section.set("corner1", LocationUtil.vectorToString(LocationUtil.getVectorOffset(corner1,corner1)));
        section.set("corner2", LocationUtil.vectorToString(LocationUtil.getVectorOffset(corner1,corner2)));
        BomberMan.getInstance().saveDataConfig();

        //Save schematic
        String schematicPath = BomberMan.getInstance().getDataFolder() + "/maps/" + mapID + ".schematic";
        World world = corner1.getWorld();
        try {
            File file = new File(schematicPath);

            Vector minCorner = new Vector(corner1.getX(), corner1.getY(), corner1.getZ());
            Vector maxCorner = new Vector(corner2.getX(), corner2.getY(), corner2.getZ());

            CuboidRegion region = new CuboidRegion(new BukkitWorld(world), minCorner, maxCorner);

            Schematic schematic = new Schematic(region);
            schematic.save(file, ClipboardFormat.SCHEMATIC);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void addSpawnPoint(String mapID, String pointID, Location location) {
        ConfigurationSection section = this.getMapSection(mapID);

        Location realZeroLocation = LocationUtil.stringToLocation(section.getString("realZeroLocation"));
        org.bukkit.util.Vector offset = LocationUtil.getVectorOffset(realZeroLocation,location);
        section.set("spawnpoints."+pointID, LocationUtil.vectorToString(offset));

        BomberMan.getInstance().saveDataConfig();
    }

    public void deleteSpawnPoint(String mapID, String pointID) {
        ConfigurationSection section = this.getMapSection(mapID);
        section.set("spawnpoints."+pointID, null);

        BomberMan.getInstance().saveDataConfig();
    }

}
