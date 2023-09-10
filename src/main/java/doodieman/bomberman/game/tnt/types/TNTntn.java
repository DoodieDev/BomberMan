package doodieman.bomberman.game.tnt.types;

import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TNTntn extends CustomTNT {

    public TNTntn(Game game) {
        super(CustomTNTType.TNT_NTN, 30L, 3, game);
    }

    //Since this TNT can destroy bedrock, modify the Super list.
    @Override
    public List<MaterialData> getDetectableBlocks() {
        return new ArrayList<>(Collections.singletonList(
            new MaterialData(0, (byte) 0)
        ));
    }

    @Override
    public void onExplode() {

        List<Block> blocksInRadius = this.detectAllBlocksInRadius();

        //Place blocks
        List<Block> blocksToPlace = new ArrayList<>();
        for (Block block : blocksInRadius) {
            Location centerBlockLocation = block.getLocation().clone().add(0.5, 0, 0.5);

            if (!this.canSeeLocation(centerBlockLocation))
                continue;

            blocksToPlace.add(block);
        }
        //Random chance
        for (Block block : blocksToPlace) {
            boolean random = ThreadLocalRandom.current().nextBoolean();
            if (!random)
                continue;

            block.setType(Material.WOOL);
            block.setData((byte) 6);
        }

        this.destroy();
        this.destroyArmorStand();
    }

}
