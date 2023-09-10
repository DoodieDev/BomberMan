package doodieman.bomberman.playerdata;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.playerdata.objects.PlayerData;
import doodieman.bomberman.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class PlayerDataUtil {

    public static PlayerData getData(OfflinePlayer player) {
        return getHandler().playerData.getOrDefault(player, new PlayerData(player));
    }

    public static PlayerData getData(String uuid) {
        return getData(UUID.fromString(uuid));
    }

    public static PlayerData getData(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return getHandler().playerData.getOrDefault(player, new PlayerData(player));
    }

    public static PlayerDataHandler getHandler() {
        return BomberMan.getInstance().getPlayerDataHandler();
    }

    /*
        PLAYER STATS
    */
    public static double getPlayerStat(OfflinePlayer player, PlayerStat stat) {
        PlayerData data = getData(player);
        FileConfiguration config = data.getFile();
        return config.getDouble("stats."+stat.getId(),stat.getDefaultValue());
    }
    public static String getPlayerStatFormatted(OfflinePlayer player, PlayerStat stat) {
        String formattedValue = StringUtil.formatNum(getPlayerStat(player,stat));
        return stat.getColorCode() + formattedValue +" "+ stat.getFancyText();
    }

    public static void setPlayerStat(OfflinePlayer player, PlayerStat stat, double value) {
        PlayerData data = getData(player);
        FileConfiguration config = data.getFile();
        config.set("stats."+stat.getId(),value);
        data.save();
    }

    public static void addPlayerStat(OfflinePlayer player, PlayerStat stat, double value) {
        setPlayerStat(player,stat,getPlayerStat(player,stat) + value);
    }

    public static void removePlayerStat(OfflinePlayer player, PlayerStat stat, double value) {
        setPlayerStat(player,stat,getPlayerStat(player,stat) - value);
    }

}
