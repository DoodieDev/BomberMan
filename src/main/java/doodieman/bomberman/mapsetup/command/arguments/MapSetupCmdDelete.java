package doodieman.bomberman.mapsetup.command.arguments;

import doodieman.bomberman.mapsetup.MapSetupUtil;
import org.bukkit.entity.Player;

public class MapSetupCmdDelete {

    public MapSetupCmdDelete(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§c/mapsetup delete <MapID>");
            return;
        }

        String mapID = args[1].toLowerCase();
        MapSetupUtil util = MapSetupUtil.getInstance();

        if (!util.doesMapExist(mapID)) {
            player.sendMessage("§cMap med det ID eksisterer ikke!");
            return;
        }

        util.deleteMap(mapID);

        player.sendMessage("§aMappet er blevet slettet!");

    }

}
