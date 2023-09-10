package doodieman.bomberman.mapsetup.command.arguments;

import doodieman.bomberman.BomberMan;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MapSetupCmdList {

    public MapSetupCmdList(Player player, String[] args) {

        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        ConfigurationSection section = dataConfig.getConfigurationSection("maps");

        for (String mapID : section.getKeys(false)) {
            boolean active = section.getBoolean(mapID+".active");
            int spawnPointsAmount = section.getConfigurationSection(mapID+".spawnpoints").getKeys(false).size();

            player.sendMessage("§e- "+mapID+" §7Active:§f"+active+" §7Spawnpoints:§f"+spawnPointsAmount);
        }

    }

}
