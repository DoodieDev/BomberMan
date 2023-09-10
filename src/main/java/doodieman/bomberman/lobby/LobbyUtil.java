package doodieman.bomberman.lobby;

import doodieman.bomberman.lobby.objects.LobbyPlayer;
import doodieman.bomberman.maphandler.MapUtil;
import doodieman.bomberman.maphandler.objects.GameMap;
import doodieman.bomberman.spawn.SpawnUtil;
import doodieman.bomberman.utils.ItemBuilder;
import doodieman.bomberman.utils.LocationUtil;
import doodieman.bomberman.utils.SkullCreator;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class LobbyUtil {

    final LobbyHandler handler;
    @Getter
    private static LobbyUtil instance;

    public LobbyUtil(LobbyHandler handler) {
        this.handler = handler;
        instance = this;
    }

    public void setLobbyPlayer(Player player) {
        LobbyPlayer lobbyPlayer = new LobbyPlayer(player);
        this.handler.getLobbyPlayers().put(player, lobbyPlayer);

        /*
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(player);
        }

         */

        player.teleport(SpawnUtil.getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getActivePotionEffects().clear();
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setAllowFlight(true);

        /*
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }

         */

        this.setLobbyItems(lobbyPlayer);
    }

    public void removeLobbyMode(Player player) {
        this.handler.getLobbyPlayers().remove(player);
        player.getInventory().clear();
    }

    public void setLobbyItems(LobbyPlayer lobbyPlayer) {

        //Queue item
        if (!lobbyPlayer.isInQueue()) {
            ItemBuilder queueItem = new ItemBuilder(Material.INK_SACK, 1, (short) 10);
            queueItem.makeGlowing();
            queueItem.name("§aTilslut kø §7(Klik)");

            lobbyPlayer.getPlayer().getInventory().setItem(4,queueItem.build());

        } else {
            ItemBuilder queueItem = new ItemBuilder(Material.INK_SACK, 1, (short) 1);
            queueItem.makeGlowing();

            queueItem.name("§cForlad kø §7(Klik)");
            lobbyPlayer.getPlayer().getInventory().setItem(4,queueItem.build());
        }

        //TNT item
        ItemBuilder tntInfo = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg4ZmI2YjJiM2VmYTZhYjI5OWY5ZjRlN2Q4YzFhMWQ3MzM4NzUzYmRmOGZlZjgxMDc1ZjIxNTU5NDNiYzY5In19fQ=="));
        tntInfo.name("§c§nTNT Information§r §7(Klik)");
        lobbyPlayer.getPlayer().getInventory().setItem(8,tntInfo.build());
    }

    public boolean isInLobby(Player player) {
        return this.handler.getLobbyPlayers().containsKey(player);
    }

    public boolean isSpaceInQueue() {
        int newAmount = this.getAmountInQueue() + 1;
        GameMap map = MapUtil.getInstance().getAppropiateMap(newAmount);
        return map != null;
    }

    public LobbyPlayer getLobbyPlayer(Player player) {
        return this.handler.getLobbyPlayers().get(player);
    }

    public boolean isInsideLobbyBoundaries(Location location) {
        Location corner1 = MapUtil.getInstance().getCurrentCorner1();
        Location corner2 = MapUtil.getInstance().getCurrentCorner2();
        corner1.setY(corner2.getY() + 1);
        corner2.setY(256);

        return LocationUtil.isInBoundaries(location, corner1,corner2);
    }

    public int getAmountInQueue() {
        return (int) this.handler.getLobbyPlayers().values()
            .stream()
            .filter(LobbyPlayer::isInQueue).count();
    }

}
