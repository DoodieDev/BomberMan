package doodieman.bomberman.simpleevents;

import doodieman.bomberman.utils.LabyModUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class SimpleEventsListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat("§e"+player.getName()+": §f"+event.getMessage());
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockUpdate(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getOnlinePlayers().size() > 20)
            event.setJoinMessage("");
        else
            event.setJoinMessage("§8[§a+§8] §a"+player.getName());

        LabyModUtil.sendCurrentPlayingGamemode(player,true,"§cBombe minigame");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getOnlinePlayers().size() > 20)
            event.setQuitMessage("");
        else
            event.setQuitMessage("§8[§c-§8] §c"+player.getName());
    }


}
