package doodieman.bomberman.mapsetup.command;

import doodieman.bomberman.mapsetup.command.arguments.MapSetupCmdCreate;
import doodieman.bomberman.mapsetup.command.arguments.MapSetupCmdDelete;
import doodieman.bomberman.mapsetup.command.arguments.MapSetupCmdList;
import doodieman.bomberman.mapsetup.command.arguments.MapSetupCmdSaveschematic;
import doodieman.bomberman.mapsetup.command.arguments.MapSetupCmdSetactive;
import doodieman.bomberman.mapsetup.command.arguments.MapSetupCmdSpawnpoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapSetupCommand implements CommandExecutor {

    private void sendHelp(Player player) {
        player.sendMessage("§6/mapsetup create <MapID>");
        player.sendMessage("§6/mapsetup delete <MapID>");
        player.sendMessage("§6/mapsetup list");
        player.sendMessage("§6/mapsetup saveschematic <MapID>");
        player.sendMessage("§6/mapsetup spawnpoint <add|delete|clear> <MapID> [<SpawnpointID>]");
        player.sendMessage("§6/mapsetup setactive <MapID> <true|false>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            this.sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {

            //mapsetup create <MapID>
            case "create":
                new MapSetupCmdCreate(player,args);
                break;

            //mapsetup delete <MapID>
            case "delete":
                new MapSetupCmdDelete(player,args);
                break;

            //mapsetup list
            case "list":
                new MapSetupCmdList(player, args);
                break;

            //mapsetup saveschematic <MapID>
            case "saveschematic":
                new MapSetupCmdSaveschematic(player,args);
                break;

            //mapsetup spawnpoint <add|delete|clear> <MapID> [<SpawnpointID>]
            case "spawnpoint":
                new MapSetupCmdSpawnpoint(player,args);
                break;

            //mapsetup setactive <MapID> <true|false>
            case "setactive":
                new MapSetupCmdSetactive(player,args);
                break;

            default:
                this.sendHelp(player);
                break;

        }


        return true;
    }

}
