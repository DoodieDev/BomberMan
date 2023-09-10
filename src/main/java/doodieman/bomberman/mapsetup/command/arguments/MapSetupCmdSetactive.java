package doodieman.bomberman.mapsetup.command.arguments;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.mapsetup.MapSetupUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MapSetupCmdSetactive {

    public MapSetupCmdSetactive(Player player, String[] args) {

        if (args.length < 3) {
            player.sendMessage("§c/mapsetup setactive <MapID> <true|false>");
            return;
        }

        String mapID = args[1].toLowerCase();
        MapSetupUtil util = MapSetupUtil.getInstance();

        if (!util.doesMapExist(mapID)) {
            player.sendMessage("§cMap med det ID eksisterer ikke!");
            return;
        }

        boolean newValue;
        try {
            newValue = Boolean.parseBoolean(args[2]);
        } catch (Exception e) {
            player.sendMessage("§c/mapsetup setactive <MapID> <true|false>");
            return;
        }

        ConfigurationSection section = MapSetupUtil.getInstance().getMapSection(mapID);
        section.set("active",newValue);

        BomberMan.getInstance().saveDataConfig();

        player.sendMessage("§aOpdateret mappets active status!");
    }

}
