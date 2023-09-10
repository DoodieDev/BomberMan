package doodieman.bomberman.game;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.bomberman.BomberMan;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.objects.GamePlayer;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import doodieman.bomberman.lobby.LobbyUtil;
import doodieman.bomberman.maphandler.MapUtil;
import doodieman.bomberman.maphandler.objects.GameMap;
import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import doodieman.bomberman.utils.FunUtil;
import doodieman.bomberman.utils.PacketUtil;
import doodieman.bomberman.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;

public class GameUtil {

    final GameHandler handler;

    @Getter
    private static GameUtil instance;

    public GameUtil(GameHandler handler) {
        this.handler = handler;
        instance = this;
    }

    //Start the game
    public void startGame(GameMap map) {
        Game game = new Game(map);

        MapUtil.getInstance().switchMap(map);
        this.handler.setActiveGame(game);
        game.start();
    }

    //Called when game is ended, and a winner was found
    public void handleWinner(GamePlayer winner) {
        Player player = winner.getPlayer();

        PlayerDataUtil.addPlayerStat(player,PlayerStat.WINS,1);
        double wins = PlayerDataUtil.getPlayerStat(player,PlayerStat.WINS);
        double kills = winner.getGameStat(PlayerStat.KILLS);

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§6Vinderen blev §f"+player.getName()+"§6! ("+ StringUtil.formatNum(wins)+" wins)");
        Bukkit.broadcastMessage("§6"+player.getName()+" dræbte §7"+StringUtil.formatNum(kills)+" spillere §6denne runde!"); //TODO tilføj stats fra spillet
        Bukkit.broadcastMessage("");


        player.playSound(player.getLocation(),Sound.LEVEL_UP,1f,0.7f);
        PacketUtil.sendTitle(player,"§6DU VANDT!","§6Vinder vinder lasagne til aftensmad",0,60,0);
    }

    public void handleKillByWorldborder(Player victim) {
        if (!this.isInGame(victim))
            return;

        //Display messages, particles and titles.
        Bukkit.broadcastMessage("§c"+victim.getName()+" døde af borderen!");
        FunUtil.createBloodParticles(victim.getLocation(),20,60L);
        victim.getWorld().playSound(victim.getLocation(), Sound.DIG_STONE,1f,0.6f);
        PacketUtil.sendTitle(victim,"§cDU DØDE!","§cDræbt af world border",0,60,0);

        victim.setGameMode(GameMode.SPECTATOR);

        //Teleport to lobby after 3 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                LobbyUtil.getInstance().setLobbyPlayer(victim);
            }
        }.runTaskLater(BomberMan.getInstance(),60L);

        //Remove player
        this.getActiveGame().removePlayer(victim);
    }

    public void handleKill(Player attacker, Player victim) {
        if (!this.isInGame(victim))
            return;

        Game game = this.getActiveGame();
        if (!game.isGameActive())
            return;
        World world = victim.getWorld();
        GamePlayer attackerGamePlayer = this.getGamePlayer(attacker,game);

        //Suicide
        if (attacker.equals(victim)) {
            Bukkit.broadcastMessage("§c"+victim.getName()+" begik selvmord!");
            PlayerDataUtil.addPlayerStat(victim,PlayerStat.SUICIDES,1);
        }
        //Attacker and victim are different
        else {
            Bukkit.broadcastMessage("§c"+victim.getName()+" blev dræbt af "+attacker.getName()+"!");
            PlayerDataUtil.addPlayerStat(attacker,PlayerStat.KILLS,1);
            if (attackerGamePlayer != null)
                attackerGamePlayer.addGameStat(PlayerStat.KILLS, 1);
        }

        //Particle and sounds
        FunUtil.createBloodParticles(victim.getLocation(),20,60L);
        world.playSound(victim.getLocation(), Sound.DIG_STONE,1f,0.6f);
        attacker.playSound(attacker.getLocation(),Sound.ORB_PICKUP,1f,1f);

        //Victim death effect
        victim.setGameMode(GameMode.SPECTATOR);
        PacketUtil.sendTitle(victim,"§cDU DØDE!","§cDræbt af §f"+attacker.getName(),0,60,0);

        new BukkitRunnable() {
            @Override
            public void run() {
                LobbyUtil.getInstance().setLobbyPlayer(victim);
            }
        }.runTaskLater(BomberMan.getInstance(),60L);

        //Remove player
        game.removePlayer(victim);
        PlayerDataUtil.addPlayerStat(victim,PlayerStat.DEATHS,1);
    }

    //Make player join the active game
    public void joinGame(Player player) {
        Game game = this.getActiveGame();

        player.setAllowFlight(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.setHealth(20);
        player.setFoodLevel(1);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));

        //Give a default TNT
        player.getInventory().addItem(CustomTNTType.getByID("level1").getItem());

        game.addPlayer(player);
        PlayerDataUtil.addPlayerStat(player,PlayerStat.GAMES_PLAYED,1);
    }

    /*
        NORMAL UTILITIES
    */

    public boolean isInGame(Player player) {
        Game game = this.getActiveGame();
        return game != null && this.getGamePlayer(player,game) != null;
    }

    public Game getActiveGame() {
        return this.handler.getActiveGame();
    }

    public GamePlayer getGamePlayer(Player player, Game game) {
        for (GamePlayer gamePlayer : game.getPlayers()) {
            if (gamePlayer.getPlayer().equals(player))
                return gamePlayer;
        }
        return null;
    }

    /*
        TNT UTILITIES
    */

    //Get a custom TNT instance from the ID
    public CustomTNT getCustomTNT(CustomTNTType type, Game game) {
        Class<? extends CustomTNT> tntClass = type.getTntClass();
        try {
            Constructor<? extends CustomTNT> turretConstructor = tntClass.getConstructor(Game.class);
            return turretConstructor.newInstance(game);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void placeTnt(CustomTNTType type, Player player, Location location) {
        Game game = this.getActiveGame();
        GamePlayer gamePlayer = this.getGamePlayer(player, game);
        CustomTNT tnt = this.getCustomTNT(type, game);
        tnt.place(location);
        tnt.setPlayerWhoPlaced(player);
        game.getPlacedTNTs().add(tnt);
        this.removeTntItems(player,type,1);

        PlayerDataUtil.addPlayerStat(player,PlayerStat.TNTS_PLACED,1);
        gamePlayer.addGameStat(PlayerStat.TNTS_PLACED,1);
    }

    //Check if a itemstack is correct TNT item
    public boolean isTNTItem(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR))
            return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey("customtnt");
    }

    //Get custom tnt type from itemstack
    public CustomTNTType getTNTTypeFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound compound = nbtItem.getCompound("customtnt");
        return CustomTNTType.getByID(compound.getString("type"));
    }

    public void removeTntItems(Player player, CustomTNTType type, int amount) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {

            if (amount <= 0) return;
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            if (!this.isTNTItem(item) || !this.getTNTTypeFromItem(item).equals(type)) continue;

            int itemAmount = item.getAmount();

            //Needs to remove more than in this itemstack
            if (amount >= itemAmount) {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                amount -= itemAmount;

            } else {
                item.setAmount(itemAmount - amount);
                amount = 0;
            }
        }
    }

}
