package doodieman.bomberman.scoreboard;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.utils.ScoreboardUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler {

    @Getter
    private final List<PlayerScoreboard> scoreboardList;
    @Getter
    private static ScoreboardHandler instance;

    public ScoreboardHandler() {
        this.scoreboardList = new ArrayList<>();
        instance = this;

        //Give all online players a scoreboard
        for (Player player : Bukkit.getOnlinePlayers())
            this.createScoreboard(player);

        this.startUpdater();
    }

    public void startUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<PlayerScoreboard> remove = new ArrayList<>();

                for (PlayerScoreboard scoreboard : scoreboardList) {
                    if (scoreboard.getPlayer() == null || !scoreboard.getPlayer().isOnline()) {
                        remove.add(scoreboard);
                        continue;
                    }
                    scoreboard.updateScoreboard();
                }
                scoreboardList.removeAll(remove);
            }
        }.runTaskTimer(BomberMan.getInstance(),0L,1L);
    }

    public void createScoreboard(Player player) {
        PlayerScoreboard scoreboard = new PlayerScoreboard(player);
        scoreboard.createScoreboard();
        this.scoreboardList.add(scoreboard);
    }


}
