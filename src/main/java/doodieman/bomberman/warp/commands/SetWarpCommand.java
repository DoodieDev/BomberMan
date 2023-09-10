package doodieman.bomberman.warp.commands;

import doodieman.bomberman.warp.WarpUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("§cBenyt /delwarp <navn>");
            return true;
        }

        String warpID = args[0].toLowerCase();

        WarpUtil.setWarp(warpID, player.getLocation());
        player.sendMessage("§aWarp §2"+warpID+" §aer blevet sat!");
        return true;
    }

}
