package doodieman.bomberman.ranking;

import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.objects.GamePlayer;
import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import doodieman.bomberman.ranking.enums.EloModifiers;
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

    public void gameStarted(Game game) {

    }

    public void gameFinished(Game game) {

        GamePlayer winner = game.getPlayers().get(0);
        this.handleEloResult(winner,false);
    }

    public void leaveGame(GamePlayer gamePlayer) {
        this.handleEloResult(gamePlayer,false);
    }

    public void handleEloResult(GamePlayer gamePlayer, boolean wonGame) {
        Player player = gamePlayer.getPlayer();

        double current_elo = PlayerDataUtil.getPlayerStat(player,PlayerStat.ELO);
        double kills = gamePlayer.getGameStat(PlayerStat.KILLS);
        double won = wonGame ? 1 : 0;

        double expected_kills = EloModifiers.getExpectedKills(current_elo);
        double expected_wins = EloModifiers.getExpectedWins(current_elo);

        double result_kills = (kills - expected_kills) * EloModifiers.KILL_VALUE.getValue();
        double result_wins = (won - expected_wins) * EloModifiers.WIN_VALUE.getValue();
        double result = current_elo + result_kills + result_wins;

        if (result <= 1000)
            result = 1000;

        PlayerDataUtil.setPlayerStat(player, PlayerStat.ELO, result);
        RankingUtil.updateRankSubtitle(player);
    }

}
