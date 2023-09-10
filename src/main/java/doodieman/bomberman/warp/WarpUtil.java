package doodieman.bomberman.warp;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarpUtil {

    public static void setWarp(String warpID, Location location) {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        String locationString = LocationUtil.locationToString(location);
        dataConfig.set("warps."+warpID,locationString);
        BomberMan.getInstance().saveDataConfig();
    }

    public static void deleteWarp(String warpID) {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        dataConfig.set("warps."+warpID, null);
        BomberMan.getInstance().saveDataConfig();
    }

    public static boolean doesWarpExist(String warpID) {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        return dataConfig.contains("warps."+warpID);
    }

    public static Location getWarpLocation(String warpID) {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        if (!dataConfig.contains("warps."+warpID))
            return null;
        return LocationUtil.stringToLocation(dataConfig.getString("warps."+warpID));
    }

    public static List<String> getAllWarps() {
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        if (!dataConfig.contains("warps"))
            return new ArrayList<>();
        ConfigurationSection section = dataConfig.getConfigurationSection("warps");
        return new ArrayList<>(section.getKeys(false));
    }

}
