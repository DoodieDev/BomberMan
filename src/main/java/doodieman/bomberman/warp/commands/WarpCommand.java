package doodieman.bomberman.warp.commands;

import doodieman.bomberman.warp.WarpUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class WarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;

        if (args.length < 1) {
            StringJoiner joiner = new StringJoiner(", ");
            for (String text : WarpUtil.getAllWarps())
                joiner.add(text);
            player.sendMessage("§aWarps: §2"+joiner.toString());
            return true;
        }

        String warpID = args[0].toLowerCase();

        if (!WarpUtil.doesWarpExist(warpID)) {
            StringJoiner joiner = new StringJoiner(", ");
            for (String text : WarpUtil.getAllWarps())
                joiner.add(text);
            player.sendMessage("§cWarp §4"+warpID+" §ceksisterer ikke!");
            player.sendMessage("§aWarps: §2"+joiner.toString());
            return true;
        }

        Location location = WarpUtil.getWarpLocation(warpID);
        player.teleport(location);
        player.sendMessage("§aTeleporteret til warp §2"+warpID+"§a!");

        return true;
    }

}
