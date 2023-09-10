package doodieman.bomberman.game.tnt.types;

import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TNTHaraldNyborg extends CustomTNT {

    public TNTHaraldNyborg(Game game) {
        super(CustomTNTType.TNT_HARALDNYBORG, 0L, 0, game);
    }

    @Override
    public void place(Location location) {

        setFuseTicks(ThreadLocalRandom.current().nextLong(20L, 101L));
        setRadius(ThreadLocalRandom.current().nextInt(2,6));

        super.place(location);
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
