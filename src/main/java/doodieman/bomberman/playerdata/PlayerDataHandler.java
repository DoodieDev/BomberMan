package doodieman.bomberman.playerdata;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.playerdata.objects.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataHandler implements Listener {

    public Map<OfflinePlayer, PlayerData> playerData = new HashMap<>();

    public PlayerDataHandler() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!playerData.containsKey(player)) {
                playerData.put(player, new PlayerData(player));
            }
        }

        Bukkit.getPluginManager().registerEvents(this, BomberMan.getInstance());
    }

    public PlayerData getData(Player player) {
        if (player.isOnline())
            return playerData.get(player);
        return new PlayerData(player);
    }

    public PlayerData getDataFromUUID(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.isOnline())
            return playerData.get(player);
        return new PlayerData(player);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        OfflinePlayer player = event.getPlayer();
        if (!playerData.containsKey(player))
            playerData.put(player, new PlayerData(player));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        OfflinePlayer player = event.getPlayer();
        if (playerData.containsKey(player)) {
            playerData.get(player).save();
            playerData.remove(player);
        }
    }

}
