package doodieman.bomberman.playerdata.command;

import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;

        //Checks own stats
        if (args.length <= 0) {
            this.showPlayerStats(player,player);
            return true;
        }

        String targetString = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetString);
        if (target == null || !target.hasPlayedBefore()) {
            player.sendMessage("§cDen spiller har aldrig været inde før!");
            return true;
        }

        this.showPlayerStats(target,player);

        return true;
    }

    private void showPlayerStats(OfflinePlayer statsPlayer, Player receiver) {

        receiver.sendMessage("§6Viser §6§l"+statsPlayer.getName()+"§6's stats:");
        receiver.sendMessage("");
        for (PlayerStat stat : PlayerStat.values()) {
            receiver.sendMessage("§f- "+ PlayerDataUtil.getPlayerStatFormatted(statsPlayer,stat));
        }

    }

}
