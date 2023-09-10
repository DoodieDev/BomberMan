package doodieman.bomberman.lobby;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.lobby.menu.TNTInfoMenu;
import doodieman.bomberman.lobby.objects.LobbyPlayer;
import doodieman.bomberman.spawn.SpawnUtil;
import doodieman.bomberman.utils.LocationUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //Try to fix players being invisible, because they first spawn in normal world
        //then get tp'd to gameworld. Very fucked
        new BukkitRunnable() {
            @Override
            public void run() {
                LobbyUtil.getInstance().setLobbyPlayer(player);
            }
        }.runTaskLater(BomberMan.getInstance(),1L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        LobbyUtil.getInstance().removeLobbyMode(player);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        if (event.getNewGameMode() == GameMode.ADVENTURE) return;

        LobbyUtil.getInstance().removeLobbyMode(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);

        LobbyPlayer lobbyPlayer = LobbyUtil.getInstance().getLobbyPlayer(player);
        int slot = player.getInventory().getHeldItemSlot();

        //Toggle waiting in queue
        if (slot == 4) {
            if (!LobbyUtil.getInstance().isSpaceInQueue() && !lobbyPlayer.isInQueue()) {
                player.sendMessage("§cDer er ikke plads til flere spilere!");
                player.sendMessage("§cVent til næste spil starter.");
                return;
            }

            lobbyPlayer.setInQueue(!lobbyPlayer.isInQueue());
            LobbyUtil.getInstance().setLobbyItems(lobbyPlayer);
        }

        if (slot == 8) {
            new TNTInfoMenu(player).open();
        }
    }


    /*
        Simple cancel events
    */

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!LobbyUtil.getInstance().isInLobby(player)) return;
        event.setCancelled(true);
    }



}
