package doodieman.bomberman.game.loot;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class ItemChance {

    @Getter
    private final ItemStack item;
    @Getter
    private final double chance;

    public ItemChance(ItemStack item, double chance) {
        this.item = item;
        this.chance = chance;
    }

}
