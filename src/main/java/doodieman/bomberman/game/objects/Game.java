package doodieman.bomberman.game.objects;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.game.GameHandler;
import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.lobby.LobbyUtil;
import doodieman.bomberman.maphandler.MapUtil;
import doodieman.bomberman.maphandler.objects.GameMap;
import doodieman.bomberman.ranking.RankingHandler;
import doodieman.bomberman.spawn.SpawnUtil;
import doodieman.bomberman.utils.LocationUtil;
import doodieman.bomberman.utils.PacketUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    final GameMap map;
    @Getter
    private boolean gameActive;
    @Getter
    private final List<GamePlayer> players;
    @Getter
    private final List<Vector> freeSpawnPoints;
    @Getter
    private final List<CustomTNT> placedTNTs;
    @Getter
    private final double worldBorderMaxRadius;
    @Getter
    private double currentWorldBorderRadius;
    @Getter
    private final Location center;
    @Getter @Setter
    private int maxPlayers;

    public Game(GameMap map) {
        this.map = map;
        this.players = new ArrayList<>();
        this.gameActive = false;
        this.freeSpawnPoints = new ArrayList<>(map.getSpawnPoints());
        this.placedTNTs = new ArrayList<>();
        this.center = map.getCenterLocation();
        this.center.setY(map.getSpawnPoints().get(0).getY() + 0.1);
        this.maxPlayers = 0;
        this.worldBorderMaxRadius = MapUtil.getInstance().getLocationFromOffset(map.getCorner1()).distance(map.getCenterLocation());
        this.currentWorldBorderRadius = worldBorderMaxRadius;
    }

    //Add a player to the game
    public void addPlayer(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player,this);

        //Spawn at a random SpawnPoint
        int randomIndex = ThreadLocalRandom.current().nextInt(freeSpawnPoints.size());
        Vector spawnVector = this.freeSpawnPoints.get(randomIndex);
        Location location = MapUtil.getInstance().getLocationFromOffset(spawnVector);
        location.setYaw((float)LocationUtil.getAngleToLocation(location, SpawnUtil.getSpawnLocation()) - 90f);
        player.teleport(location);
        this.freeSpawnPoints.remove(randomIndex);
        //Add
        this.players.add(gamePlayer);
        this.maxPlayers++;
    }

    //Remove a player from the game... eg, kill the player
    public void removePlayer(Player player) {
        GamePlayer gamePlayer = GameUtil.getInstance().getGamePlayer(player,this);
        if (gamePlayer == null)
            return;
        gamePlayer.setPlacement(this.getPlayers().size());
        this.players.remove(gamePlayer);

        RankingHandler.getInstance().leaveGame(gamePlayer);

        //Only one player left, handle the winner
        if (this.players.size() < 2 && gameActive) {
            this.gameActive = false;

            GamePlayer winner = this.players.get(0);
            GameUtil.getInstance().handleWinner(winner);

            //Stop the game after 5 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    stop();
                }
            }.runTaskLater(BomberMan.getInstance(),60L);

        }
    }

    //Starts the game
    public void start() {
        this.gameActive = true;
        this.startShrinking();
    }

    //Stops the game
    public void stop() {
        this.gameActive = false;

        //Remove active fusing tnts
        for (CustomTNT tnt : this.placedTNTs) {
            tnt.destroyArmorStand();
            tnt.getFuseRunnable().cancel();
        }


        //Remove players
        for (GamePlayer gamePlayer : new ArrayList<>(this.players)) {
            Player player = gamePlayer.getPlayer();
            this.removePlayer(player);
            LobbyUtil.getInstance().setLobbyPlayer(player);
        }


        GameHandler.getInstance().setActiveGame(null);
    }

    //Start shrinking the world border
    public void startShrinking() {
        if (!gameActive) return;

        long shrinkingTime = 3 * 60L * 20L; //3 minutes
        Location center = map.getCenterLocation();
        center.setY(map.getSpawnPoints().get(0).getY() + 0.1);

        new BukkitRunnable() {

            long tick = 1L;
            double angleOffset = 0;

            @Override
            public void run() {

                //The shrinking stops
                if (!gameActive) {
                    this.cancel();
                    return;
                }

                //Shrink the radius
                currentWorldBorderRadius = worldBorderMaxRadius - (((double) tick / shrinkingTime) * worldBorderMaxRadius);

                //Display the border particles
                int displayPoints = 200;
                for (int point = 0; point < displayPoints; point++) {
                    double angle = (point * (360f / displayPoints)) + angleOffset;
                    Location location = LocationUtil.getLocationInCircle(center,angle,currentWorldBorderRadius);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (int i = 0; i < 2; i++) {
                            Location offsetLocation = location.clone().add(0,i * 0.3,0);
                            PacketUtil.sendRedstoneParticle(player,offsetLocation, Color.fromRGB(189, 11, 183));
                        }
                    }
                }
                this.angleOffset += 1;

                //Handle players outside the border
               Game.this.handleBorderPlayers();

                tick += 5L;
            }
        }.runTaskTimer(BomberMan.getInstance(),0L, 5L);
    }

    //Get all players outside the border
    public void handleBorderPlayers() {
        double nearDistance = 3;
        Location centerLocation = this.getCenter();
        for (GamePlayer gamePlayer : new ArrayList<>(this.getPlayers())) {
            Player player = gamePlayer.getPlayer();
            double radius = player.getLocation().distance(centerLocation);

            //Is outside the border
            if (radius >= currentWorldBorderRadius) {
                //Damage the player
                LocationUtil.pushPlayerTowards(player,centerLocation,0.2);
                double newHealth = player.getHealth() - 1;
                if (newHealth <= 1)
                    GameUtil.getInstance().handleKillByWorldborder(player);
                else
                    player.setHealth(player.getHealth() - 1);
            }
            //Is close to the border
            else if ((radius + nearDistance) >= currentWorldBorderRadius) {
                PacketUtil.sendTitle(player,"§4§l⚠","§cPas på worldborderen!",0,5,2);
                player.playSound(player.getLocation(), Sound.NOTE_BASS,1f,0.8f);
            }
        }
    }
}
