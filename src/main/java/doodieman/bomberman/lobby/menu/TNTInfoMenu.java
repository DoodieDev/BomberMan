package doodieman.bomberman.lobby.menu;

import doodieman.bomberman.game.tnt.CustomTNTType;
import doodieman.bomberman.utils.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class TNTInfoMenu extends GUI {

    public TNTInfoMenu(Player player) {
        super(player, 4, "TNT Informationer");
    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.EXIT_MENU.getItem());


        int slot = 0;
        for (CustomTNTType tntType : CustomTNTType.values()) {
            this.layout.put(slot,tntType.getItem());
            slot++;
        }

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 31) player.closeInventory();
    }

}
