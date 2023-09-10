package doodieman.bomberman.game;

import doodieman.bomberman.game.tnt.CustomTNTType;
import doodieman.bomberman.maphandler.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!GameUtil.getInstance().isInGame(player)) return;
        GameUtil util = GameUtil.getInstance();
        ItemStack tool = player.getItemInHand();
        Location location = event.getBlockPlaced().getLocation();

        event.setCancelled(true);
        if (!util.isTNTItem(tool)) return;
        if (location.getY() != MapUtil.getInstance().getCurrentMap().getGameLevel()) return;

        CustomTNTType type = util.getTNTTypeFromItem(tool);
        util.placeTnt(type, player,location);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!GameUtil.getInstance().isInGame(player)) return;
        if (player.isOp()) return;
        event.setCancelled(true);
        player.sendMessage("§cDu kan ikke bruge kommandoer inde i spillet!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!GameUtil.getInstance().isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (!GameUtil.getInstance().isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!GameUtil.getInstance().isInGame(player)) return;

        GameUtil.getInstance().getActiveGame().removePlayer(player);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!GameUtil.getInstance().isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClickEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!GameUtil.getInstance().isInGame(player)) return;
        event.setCancelled(true);
    }


}
