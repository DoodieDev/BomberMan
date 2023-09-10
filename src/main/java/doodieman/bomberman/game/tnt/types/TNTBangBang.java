package doodieman.bomberman.game.tnt.types;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TNTBangBang extends CustomTNT {

    private int explodes;

    public TNTBangBang(Game game) {
        super(CustomTNTType.TNT_BANGBANG, 80L, 3, game);
        this.explodes = 0;
    }

    @Override
    public void onExplode() {

        //Explode 2 times, then destroy
        explodes++;
        if (explodes < 2) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    explodeEffect();
                    destroy();
                }
            }.runTaskLater(BomberMan.getInstance(), 10L);
        }
        this.destroyArmorStand();

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
    }

}
