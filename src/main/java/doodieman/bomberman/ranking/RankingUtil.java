package doodieman.bomberman.ranking;

import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import doodieman.bomberman.ranking.enums.Rank;
import doodieman.bomberman.utils.LabyModUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RankingUtil {


    public static Rank getRank(Player player) {
        double elo = getElo(player);
        return Rank.getRank(elo);
    }

    public static double getElo(Player player) {
        return PlayerDataUtil.getPlayerStat(player, PlayerStat.ELO);
    }

    public static void updateRankSubtitle(Player player) {
        String rankPrefix = RankingUtil.getRank(player).getPrefix();
        LabyModUtil.setSubtitle(new ArrayList<>(Bukkit.getOnlinePlayers()), player,rankPrefix,1.3d);
    }

    public static void updateRankSubtitleFor(Player player, Player receiver) {
        String rankPrefix = RankingUtil.getRank(player).getPrefix();
        LabyModUtil.setSubtitle(receiver,player, rankPrefix, 1.3d);
    }


}
