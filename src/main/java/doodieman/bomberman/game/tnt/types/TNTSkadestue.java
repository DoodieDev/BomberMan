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
import java.util.Arrays;
import java.util.List;

public class TNTSkadestue extends CustomTNT {

    public TNTSkadestue(Game game) {
        super(CustomTNTType.TNT_SKADESTUE, 100L, 5, game);
    }

    //Since this TNT can destroy bedrock, modify the Super list.
    @Override
    public List<MaterialData> getDetectableBlocks() {
        List<MaterialData> dataList = new ArrayList<>(Arrays.asList(
            new MaterialData(7, (byte) 0),
            new MaterialData(5, (byte) 5),
            new MaterialData(112, (byte) 0)
        ));
        dataList.addAll(super.getDetectableBlocks());
        return dataList;
    }

    @Override
    public void onExplode() {

        List<Block> blocksInRadius = this.detectAllBlocksInRadius();
        List<Player> playersInRadius = this.detectAllPlayersInRadius();

        //Damage players
        for (Player player : playersInRadius) {
            if (!this.canSeeLocation(player.getLocation()))
                continue;
            GameUtil.getInstance().handleKill(this.getPlayerWhoPlaced(), player);
        }

        //Damage blocks
        List<Block> blocksToRemove = new ArrayList<>();
        for (Block block : blocksInRadius) {
            Location centerBlockLocation = block.getLocation().clone().add(0.5, 0, 0.5);
            if (!this.canSeeLocation(centerBlockLocation))
                continue;

            blocksToRemove.add(block);
        }
        for (Block block : blocksToRemove)
            this.destroyBlock(block);

        this.destroy();
        this.destroyArmorStand();
    }

}
