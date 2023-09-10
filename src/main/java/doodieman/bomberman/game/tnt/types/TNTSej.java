package doodieman.bomberman.game.tnt.types;

import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TNTSej extends CustomTNT {

    public TNTSej(Game game) {
        super(CustomTNTType.TNT_SEJ, 70L, 4, game);
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

        //Sort the blocks in radius by distance. Or else it will fail
        //checking canSeeLocation() on them.
        List<Block> blockList = blocksInRadius
            .stream()
            .sorted(Comparator.comparingDouble(b -> {
                Location centered = b.getLocation().add(0.5, 0, 0.5);
                return centered.distance(this.getCenterLocation());
            }))
            .collect(Collectors.toList());;

        //Destroy blocks
        for (Block block : blockList) {
            Location centerBlockLocation = block.getLocation().clone().add(0.5, 0, 0.5);
            if (!this.canSeeLocation(centerBlockLocation))
                continue;

            this.destroyBlock(block);
        }

        this.destroy();
        this.destroyArmorStand();
    }

}
