package doodieman.bomberman.game.tnt.types;

import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TNTNaturlig extends CustomTNT {

    public TNTNaturlig(Game game) {
        super(CustomTNTType.TNT_NATURLIG, 60L, 3, game);
    }

    @Override
    public void onExplode() {

        List<Block> blocksInRadius = this.detectAllBlocksInRadius();
        List<Player> playersInRadius = this.detectAllPlayersInRadius();

        //Damage players
        for (Player player : playersInRadius) {
            if (!this.canSeeLocation(player.getLocation()))
                continue;
            //If its the player who placed, do not damage
            if (player.equals(this.getPlayerWhoPlaced()))
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
