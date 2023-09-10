package doodieman.bomberman.game.tnt;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.bomberman.game.tnt.types.TNTBangBang;
import doodieman.bomberman.game.tnt.types.TNTLevel1;
import doodieman.bomberman.game.tnt.types.TNTLevel2;
import doodieman.bomberman.game.tnt.types.TNTLevel3;
import doodieman.bomberman.game.tnt.types.TNTLevel4;
import doodieman.bomberman.game.tnt.types.TNTNaturlig;
import doodieman.bomberman.game.tnt.types.TNTSej;
import doodieman.bomberman.game.tnt.types.TNTHaraldNyborg;
import doodieman.bomberman.game.tnt.types.TNTSkadestue;
import doodieman.bomberman.game.tnt.types.TNTSmut;
import doodieman.bomberman.game.tnt.types.TNTntn;
import doodieman.bomberman.utils.ItemBuilder;
import doodieman.bomberman.utils.SkullCreator;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum  CustomTNTType {

    TNT_LEVEL_1("level1", TNTLevel1.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjNmZGNlNTkzODM4Y2NiODc1ZGRkMTY4NzA1ODM5NmRmNmU1MWNmOTU4YmMxMDE2NjBjYjE0ZTJhNGQ3ZjgifX19"),
        "§fLevel §7[§f1§7] §fTNT",
        Arrays.asList("§f",
            "§7Radius: §f2 blocks",
            "§7Lunte: §f3,5 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇§f▇§a▇§f▇§0▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),
    TNT_LEVEL_2("level2", TNTLevel2.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTk1YWNhNzJkY2NlMTI5ODQ3NTM5OGZiN2FkZmY4NDQ3YzlhNjcwZTZjZjY0ZGQzOTMzYmJjOTQ5MThlOTkifX19"),
        "§fLevel §7[§f2§7] §fTNT",
        Arrays.asList("§f",
            "§7Radius: §f3 blocks",
            "§7Lunte: §f3,5 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇§f▇▇§a▇§f▇▇§0▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),
    TNT_LEVEL_3("level3", TNTLevel3.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU0MjU1Yjg0NmYxNDRiOTNiYzVmOWI3NTQ2ZTBjMjRiM2ExNDY2MDI0NjQzNjEwZDljNWQ2YmQ0YzhlNSJ9fX0="),
        "§fLevel §7[§f3§7] §fTNT",
        Arrays.asList("§f",
            "§7Radius: §f4 blocks",
            "§7Lunte: §f3,5 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§0▇§f▇▇▇§a▇§f▇▇▇§0▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),
    TNT_LEVEL_4("level4", TNTLevel4.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE5ZWYyNTQ2MDU2Zjk0OWI4NDMxY2RhMGQxMzg1ZmQzYmUyYjcxMWQ2YzQ1ZTQ0YmQ0OGNkZWE2OTBhN2RkIn19fQ=="),
        "§fLevel §7[§f4§7] §fTNT",
        Arrays.asList("§f",
            "§7Radius: §f5 blocks",
            "§7Lunte: §f3,5 sekunder",
            "§f",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇§a▇§f▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇")
    ),

    TNT_BANGBANG("bangbang", TNTBangBang.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjcxOGRiZjEwN2M2YTMxNzYzYjMwMjk4ZDFiMDlmZTllODM2ZGE2MzRjNTU2NzIzZWU0MWE0NzVkZGIyIn19fQ=="),
        "§9§nBang bang TNT",
        Arrays.asList("§f",
            "§fSpringer 2 gange!",
            "§f",
            "§7Radius: §f3 blocks",
            "§7Lunte: §f4 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇§f▇▇§a▇§f▇▇§0▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),

    TNT_SEJ("sej", TNTSej.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM5MmUzZjQ1YjQ5ZTQwNTY3MDIyNDg5MmY5M2ViYzg0ZmE3ZjhjOTZjMzZhYWIyNGE4ODU0ZjJjYmYwYjgifX19"),
        "§6§nSej TNT",
        Arrays.asList("§f",
            "§fSmadrer alle normale",
            "§fblocks i dens radius!",
            "§f",
            "§7Radius: §f3 blocks",
            "§7Lunte: §f4 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇§f▇▇§a▇§f▇▇§0▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),

    TNT_NATURLIG("naturlig", TNTNaturlig.class,
                 SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThiM2ZmZDM3ZTZmMzJhNGU3YzFhYmEyY2I0ODQ5MjNjZjY5NWY0NDdjODc5ZTcxMmVjMjVhNjEzZTAxMCJ9fX0="),
        "§2§nNaturlig TNT",
            Arrays.asList("§f",
            "§fSkader dig ikke!",
            "§f",
            "§7Radius: §f3 blocks",
            "§7Lunte: §f3 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇§f▇▇§a▇§f▇▇§0▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),

    TNT_SKADESTUE("skadestue", TNTSkadestue.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg4ZmI2YjJiM2VmYTZhYjI5OWY5ZjRlN2Q4YzFhMWQ3MzM4NzUzYmRmOGZlZjgxMDc1ZjIxNTU5NDNiYzY5In19fQ=="),
        "§c§nSkadestue TNT",
        Arrays.asList("§f",
            "§fKan smadre alt!",
            "§f",
            "§7Radius: §f5 blocks",
            "§7Lunte: §f5 sekunder",
            "§f",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇§a▇§f▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇")
    ),

    TNT_NTN("ntn", TNTntn.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVjM2YyNjQzOGRiZDgyNGNjN2RlYWVjNjdiMTUxZmIyNjQ1N2M2ZTIxNWVhNjM2NTJjMzNhMWIxY2JjNSJ9fX0="),
        "§d§nNTN TNT",
        Arrays.asList("§f",
            "§fPlacerer blocks i stedet!",
            "§ffor at smadre dem!",
            "",
            "§7Radius: §f3 blocks",
            "§7Lunte: §f1,5 sekunder",
            "§f",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇§f▇▇§a▇§f▇▇§0▇▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇▇▇§f▇▇▇§0▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇",
            "§0▇▇▇▇▇▇▇▇▇")
    ),

    TNT_SMUT("smut", TNTSmut.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U0MjdhZGZmMzU2ZjA5NDVlYmJhMzkxY2EwZWVmNDY1YWUxNzQ2MWVhN2M1M2FkMTczZmJiNmM3ZjM2YTgifX19"),
        "§b§nSmut TNT",
        Arrays.asList("§f",
            "§fSkubber spillere!",
            "",
            "§7Radius: §f5 blocks",
            "§7Lunte: §f1 sekund",
            "§f",
            "§0▇▇§f▇▇▇▇▇§0▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇§a▇§f▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§f▇▇▇▇▇▇▇▇▇",
            "§0▇§f▇▇▇▇▇▇▇§0▇",
            "§0▇▇§f▇▇▇▇▇§0▇▇")
    ),

    TNT_HARALDNYBORG("haraldnyborg", TNTHaraldNyborg.class,
        SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc5YTIzNzUzNjNiZTg5MTM4MWQ0MmM0NWIzNzc1NTNjMmI2ZTBmYmRjMTZkZWY1ZmJjNmIzMmVkNWNhZDdhNyJ9fX0="),
        "§5§nHarald Nyborg TNT",
        Arrays.asList("§f",
            "§fKan ikke regnes med!",
            "§f",
            "§7Radius: §f§k?§f blocks",
            "§7Lunte: §f§k?§f sekunder",
            "§f")
    );



    @Getter
    private final String id;
    @Getter
    private final Class<? extends CustomTNT> tntClass;

    private final ItemStack itemStack;
    private final String itemName;
    private final List<String> lore;

    private static final Map<String, CustomTNTType> BY_ID = new HashMap<>();

    CustomTNTType(
        String id,
        Class<? extends CustomTNT> tntClass,
        ItemStack itemStack,
        String itemName,
        List<String> lore
    ) {
        this.id = id;
        this.tntClass = tntClass;
        this.itemStack = itemStack;
        this.itemName = itemName;
        this.lore = lore;
    }

    public ItemStack getItem() {
        ItemBuilder builder = new ItemBuilder(this.itemStack);
        builder.name(this.itemName);
        builder.lore(this.lore);

        //Add NBT
        NBTItem nbtItem = new NBTItem(builder.build());
        NBTCompound compound = nbtItem.addCompound("customtnt");
        compound.setString("type", this.id);

        return nbtItem.getItem();
    }

    public void dropAtLocation(Location location) {
        Item item = location.getWorld().dropItem(location, this.getItem().clone());
        item.setVelocity(new Vector(0,0,0));
        item.setPickupDelay(0);
    }

    public static CustomTNTType getByID(String id) {
        return BY_ID.get(id);
    }
    static {
        for (CustomTNTType type : values()) {
            BY_ID.put(type.getId(),type);
        }
    }

}
