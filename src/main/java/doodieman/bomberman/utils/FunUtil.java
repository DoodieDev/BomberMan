package doodieman.bomberman.utils;

import doodieman.bomberman.BomberMan;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FunUtil {

    public static void createBloodParticles(Location location, int bloodItems, long despawnTime) {

        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < bloodItems; i++) {
            ItemStack itemStack = new ItemBuilder(Material.INK_SACK, 1, (short) 1)
                .name("blood"+i)
                .build();
            Item item = location.getWorld().dropItem(location,itemStack);
            item.setPickupDelay(Integer.MAX_VALUE);
            itemList.add(item);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item item : itemList)
                    item.remove();
            }
        }.runTaskLater(BomberMan.getInstance(),despawnTime);
    }

}
