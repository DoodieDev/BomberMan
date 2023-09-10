package doodieman.bomberman.lobby;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.game.GameHandler;
import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.lobby.objects.LobbyPlayer;
import doodieman.bomberman.maphandler.MapUtil;
import doodieman.bomberman.maphandler.objects.GameMap;
import doodieman.bomberman.spawn.SpawnUtil;
import doodieman.bomberman.utils.LocationUtil;
import doodieman.bomberman.utils.PacketUtil;
import doodieman.bomberman.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyHandler {

    @Getter
    private final Map<Player, LobbyPlayer> lobbyPlayers;
    @Getter
    private final LobbyUtil util;

    @Getter // Queue timer
    private int secondsLeft;

    public LobbyHandler() {
        this.util = new LobbyUtil(this);

        this.secondsLeft = 20;
        this.lobbyPlayers = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new LobbyListener(), BomberMan.getInstance());

        //Make all players lobby players
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL) continue;
            util.setLobbyPlayer(player);
        }

        this.startTimer();
    }

    public void startTimer() {

        new BukkitRunnable() {
            long tick = 0L;

            @Override
            public void run() {
                checkPlayerLocations();

                tick++;

                if (tick % 20 == 0) {

                    //Either queue is inactive, or there is not enough players.
                    if (util.getAmountInQueue() < 2) {
                        for (Player player : lobbyPlayers.keySet())
                            PacketUtil.sendActionBar(player, "§cVenter på spillere..");
                        secondsLeft = 20;
                        return;
                    }
                    if (GameUtil.getInstance().getActiveGame() != null) {
                        for (Player player : lobbyPlayers.keySet())
                            PacketUtil.sendActionBar(player, "§cVenter på det næste spil..");
                        secondsLeft = 20;
                        return;
                    }

                    //Announce the lobby timer has started.
                    if (secondsLeft == 15) {
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("§6Det næste spil starter om §f15 sekunder§6.");
                        Bukkit.broadcastMessage("§6Skynd dig at tilslut med den grønne item!");
                        Bukkit.broadcastMessage("");
                        for (Player player : Bukkit.getOnlinePlayers())
                            player.playSound(player.getLocation(),Sound.NOTE_BASS,1f,0.8f);
                    }

                    //Timer is done. Start the game.
                    if (secondsLeft <= 0) {
                        GameMap map = MapUtil.getInstance().getAppropiateMap(LobbyUtil.getInstance().getAmountInQueue());
                        GameUtil.getInstance().startGame(map);

                        for (LobbyPlayer lobbyPlayer : new ArrayList<>(lobbyPlayers.values())) {
                            Player player = lobbyPlayer.getPlayer();
                            if (!lobbyPlayer.isInQueue()) continue;

                            util.removeLobbyMode(player);
                            GameUtil.getInstance().joinGame(player);
                        }
                        return;
                    }

                    //Display time countdown
                    String timeLeftFormatted = StringUtil.convertToDanishTime(secondsLeft);
                    for (Player player : lobbyPlayers.keySet()) {
                        PacketUtil.sendActionBar(player, "§6Spillet starter om §f"+ timeLeftFormatted +"§6!");
                        player.playSound(player.getLocation(), Sound.CLICK,1f,1f);
                    }

                    secondsLeft -= 1;
                }
            }
        }.runTaskTimer(BomberMan.getInstance(),0L,1L);

    }

    //Check player positions, teleport them if they are not inside the map
    public void checkPlayerLocations() {
        for (Player player : this.lobbyPlayers.keySet()) {
            Location location = player.getLocation();

            if (this.util.isInsideLobbyBoundaries(location)) continue;

            Location newLocation = LocationUtil.moveCloser(location, SpawnUtil.getSpawnLocation(),1.5);
            newLocation.setYaw(player.getLocation().getYaw());
            newLocation.setPitch(player.getLocation().getPitch());
            player.teleport(newLocation);
        }
    }

}
