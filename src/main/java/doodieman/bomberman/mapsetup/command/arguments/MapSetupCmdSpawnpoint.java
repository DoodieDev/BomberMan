package doodieman.bomberman.mapsetup.command.arguments;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.mapsetup.MapSetupUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MapSetupCmdSpawnpoint {

    //mapsetup spawnpoint <add|delete|list> <MapID> [<SpawnpointID>]
    public MapSetupCmdSpawnpoint(Player player, String[] args) {

        if (args.length < 3) {
            player.sendMessage("§c/mapsetup spawnpoint <add|delete|clear> <MapID> [<SpawnpointID>]");
            return;
        }

        String action = args[1].toLowerCase();
        String mapID = args[2].toLowerCase();

        if (!MapSetupUtil.getInstance().doesMapExist(mapID)) {
            player.sendMessage("§cMap med det ID eksisterer ikke!");
            return;
        }

        switch (action) {
            case "add":
                this.addPoint(player,mapID, args);
                break;

            case "delete":
                this.deletePoint(player,mapID, args);
                break;

            case "clear":
                this.clear(player,mapID, args);
                break;

            default:
                player.sendMessage("§c/mapsetup spawnpoint <add|delete|clear> <MapID> [<SpawnpointID>]");
                break;
        }
    }

    public void addPoint(Player player, String mapID, String[] args) {

        if (args.length < 4) {
            player.sendMessage("§c/mapsetup spawnpoint <add|delete|clear> <MapID> [<SpawnpointID>]");
            return;
        }

        String pointID = args[3].toLowerCase();
        MapSetupUtil.getInstance().addSpawnPoint(mapID,pointID,player.getLocation());

        player.sendMessage("§aSpawnpoint er blevet tilføjet.");
    }

    public void deletePoint(Player player, String mapID, String[] args) {

        if (args.length < 4) {
            player.sendMessage("§c/mapsetup spawnpoint <add|delete|clear> <MapID> [<SpawnpointID>]");
            return;
        }

        String pointID = args[3].toLowerCase();
        MapSetupUtil.getInstance().deleteSpawnPoint(mapID,pointID);

        player.sendMessage("§aSpawnpoint er blevet slettet.");
    }

    public void clear(Player player, String mapID, String[] args) {
        ConfigurationSection section = MapSetupUtil.getInstance().getMapSection(mapID);
        section.createSection("spawnpoints"); //Creating section just clears it.
        BomberMan.getInstance().saveDataConfig();

        player.sendMessage("§aAlle spawnpoints er blevet slettet!");
    }

}
