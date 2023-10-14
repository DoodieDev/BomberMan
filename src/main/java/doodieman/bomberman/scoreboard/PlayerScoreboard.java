package doodieman.bomberman.scoreboard;

import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import doodieman.bomberman.ranking.RankingUtil;
import doodieman.bomberman.utils.ScoreboardUtil;
import doodieman.bomberman.utils.StringUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Arrays;
import java.util.List;

public class PlayerScoreboard {

    @Getter
    private final Player player;
    private ScoreboardUtil scoreboard;

    public PlayerScoreboard(Player player) {
        this.player = player;
    }

    public void createScoreboard() {
        this.scoreboard = new ScoreboardUtil("§c§nBOMBE");
        this.scoreboard.send(player);
    }

    public void deleteScoreboard() {
        if (scoreboard == null) return;
        this.scoreboard.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
    }

    public void updateScoreboard() {

        String rankPrefix = RankingUtil.getRank(player).getPrefix();
        String formattedElo = StringUtil.formatNum(Math.round(PlayerDataUtil.getPlayerStat(player, PlayerStat.ELO)));
        List<PlayerStat> stats = Arrays.asList(PlayerStat.WINS, PlayerStat.KILLS, PlayerStat.TNTS_PLACED, PlayerStat.BLOCKS_BROKEN);

        this.scoreboard.add("§1",9);
        this.scoreboard.add("§6Rank: "+rankPrefix,8);
        this.scoreboard.add("§6ELO: §f"+formattedElo,7);
        this.scoreboard.add("§2",6);

        //Show stats
        int score = 5; //Going down
        for (PlayerStat stat : stats) {
            String valueFormatted = StringUtil.formatNum(PlayerDataUtil.getPlayerStat(player,stat));
            this.scoreboard.add(stat.getColorCode()+valueFormatted+" "+stat.getFancyText(),score);
            score--;
        }
        this.scoreboard.add("§3",1);
        this.scoreboard.add("§6Join vores §9/discord§f!",0);

        this.scoreboard.update();
    }

}
