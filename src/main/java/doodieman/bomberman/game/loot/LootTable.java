package doodieman.bomberman.game.loot;

import doodieman.bomberman.game.tnt.CustomTNTType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum LootTable {

    NORMAL_BLOCKS(
        "defaultblocks",

        Arrays.asList(
            new MaterialData(1, (byte) 0),
            new MaterialData(5, (byte) 1)
        ),

        Arrays.asList(
            new ItemChance(CustomTNTType.TNT_LEVEL_1.getItem(), 10),
            new ItemChance(CustomTNTType.TNT_LEVEL_2.getItem(), 10),
            new ItemChance(CustomTNTType.TNT_LEVEL_3.getItem(), 7.5),
            new ItemChance(CustomTNTType.TNT_LEVEL_4.getItem(), 5),

            new ItemChance(CustomTNTType.TNT_BANGBANG.getItem(), 5),
            new ItemChance(CustomTNTType.TNT_HARALDNYBORG.getItem(), 4),
            new ItemChance(CustomTNTType.TNT_NATURLIG.getItem(), 3),

            new ItemChance(new ItemStack(Material.AIR), 55.5)
        )
    ),

    GOLD_ORE(
        "gold",

        Collections.singletonList(
            new MaterialData(14, (byte) 0)
        ),

        Arrays.asList(
            new ItemChance(CustomTNTType.TNT_BANGBANG.getItem(), 35),
            new ItemChance(CustomTNTType.TNT_HARALDNYBORG.getItem(), 40),
            new ItemChance(CustomTNTType.TNT_NATURLIG.getItem(), 25)
        )
    ),

    EMERALD_ORE(
        "emerald",

        Collections.singletonList(
            new MaterialData(129, (byte) 0)
        ),

        Arrays.asList(
            new ItemChance(CustomTNTType.TNT_NATURLIG.getItem(), 35),
            new ItemChance(CustomTNTType.TNT_SMUT.getItem(), 40),
            new ItemChance(CustomTNTType.TNT_SEJ.getItem(), 25)
        )
    ),

    DIAMOND_ORE(
        "diamond",

        Collections.singletonList(
            new MaterialData(56, (byte) 0)
        ),

        Arrays.asList(
            new ItemChance(CustomTNTType.TNT_SEJ.getItem(), 35),
            new ItemChance(CustomTNTType.TNT_NTN.getItem(), 40),
            new ItemChance(CustomTNTType.TNT_SKADESTUE.getItem(), 25)
        )
    );

    @Getter
    private final String id;
    @Getter
    private final List<MaterialData> droppingFrom;
    @Getter
    private final List<ItemChance> loot;

    LootTable(
        String id,
        List<MaterialData> droppingFrom,
        List<ItemChance> loot
    ) {
        this.id = id;
        this.droppingFrom = droppingFrom;
        this.loot = loot;
    }


    public static ItemStack getRandomItem(MaterialData materialData) {
        for (LootTable table : values()) {
            if (!table.getDroppingFrom().contains(materialData)) continue;

            return getRandomItem(table);
        }
        return null;
    }

    public static ItemStack getRandomItem(LootTable table) {
        ItemStack selected = null;
        List<ItemChance> itemChanceList = table.getLoot();

        while (selected == null) {

            int randomIndex = ThreadLocalRandom.current().nextInt(itemChanceList.size());
            ItemChance itemChance = itemChanceList.get(randomIndex);

            double random = ThreadLocalRandom.current().nextDouble(100);
            if (itemChance.getChance() >= random)
                selected = itemChance.getItem();
        }
        return selected;
    }

}
