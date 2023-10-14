package doodieman.bomberman.ranking;

import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.objects.GamePlayer;
import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import doodieman.bomberman.ranking.enums.EloModifiers;
import doodieman.bomberman.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RankingHandler {

    @Getter
    private static RankingHandler instance;

    public RankingHandler() {
        instance = this;

        for (Player p : Bukkit.getOnlinePlayers())
            RankingUtil.updateRankSubtitle(p);
    }

    public void leaveGame(GamePlayer gamePlayer) {
        this.handleEloResult(gamePlayer);
    }

    public void handleEloResult(GamePlayer gamePlayer) {
        Game game = gamePlayer.getGame();
        Player player = gamePlayer.getPlayer();

        double current_elo = PlayerDataUtil.getPlayerStat(player,PlayerStat.ELO);
        int kills = (int) gamePlayer.getGameStat(PlayerStat.KILLS);
        int max_players = game.getMaxPlayers();
        int placement = gamePlayer.getPlacement();

        double elo_gain = EloModifiers.getGain(current_elo, placement, max_players, kills);
        double new_elo = current_elo + elo_gain;

        if (new_elo < 1000)
            new_elo = 1000;

        String formattedElo = StringUtil.formatNum(Math.round(Math.abs(elo_gain)));
        player.sendMessage("");
        if (elo_gain > 0) {
            player.sendMessage("§eDu modtog §a"+formattedElo+" ELO§e denne runde!");
        } else if (elo_gain == 0) {
            player.sendMessage("§eDu modtog §7"+formattedElo+" ELO§e denne runde!");
        } else {
            player.sendMessage("§eDu mistede §c"+formattedElo+" ELO§e denne runde!");
        }
        player.sendMessage("§eDrab: §f"+kills+" "+(kills == 1 ? "spiller" : "spillere"));
        player.sendMessage("§ePlads: §6#§6§l"+placement+" §eud af §6#§6§l"+max_players);
        player.sendMessage("");

        //PlayerDataUtil.setPlayerStat(player, PlayerStat.ELO, result);
        PlayerDataUtil.setPlayerStat(player,PlayerStat.ELO, new_elo);
        RankingUtil.updateRankSubtitle(player);
    }

}
