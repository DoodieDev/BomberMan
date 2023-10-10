package doodieman.bomberman.game.objects;

import doodieman.bomberman.playerdata.PlayerStat;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GamePlayer {

    @Getter
    private final Player player;
    final Game game;
    private final Map<PlayerStat, Double> gameStats;

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;

        this.gameStats = new HashMap<>();
    }

    public double getGameStat(PlayerStat stat) {
        return gameStats.getOrDefault(stat,0d);
    }
    public void addGameStat(PlayerStat stat, double amount) {
        double current = this.getGameStat(stat);
        gameStats.put(stat,current + amount);
    }

}
