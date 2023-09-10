package doodieman.bomberman.warp.commands;

import doodieman.bomberman.warp.WarpUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("§cBenyt /setwarp <navn>");
            return true;
        }

        String warpID = args[0].toLowerCase();

        if (!WarpUtil.doesWarpExist(warpID)) {
            player.sendMessage("§cWarp §4"+warpID+" §ceksisterer ikke!");
            return true;
        }

        WarpUtil.deleteWarp(warpID);
        player.sendMessage("§aWarp §2"+warpID+" §aer blevet slettet!");
        return true;
    }

}
