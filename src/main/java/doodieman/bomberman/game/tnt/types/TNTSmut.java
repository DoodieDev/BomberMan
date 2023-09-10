package doodieman.bomberman.game.tnt.types;

import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.tnt.CustomTNT;
import doodieman.bomberman.game.tnt.CustomTNTType;
import doodieman.bomberman.utils.LocationUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class TNTSmut extends CustomTNT {

    public TNTSmut(Game game) {
        super(CustomTNTType.TNT_SMUT, 20L, 5, game);
    }

    @Override
    public void onExplode() {

        List<Player> playersInRadius = this.detectAllPlayersInRadius();

        //Damage players
        for (Player player : playersInRadius) {
            if (!this.canSeeLocation(player.getLocation()))
                continue;

            //Push player away
            LocationUtil.pushPlayerAway(player,this.getCenterLocation(),2);
        }

        this.destroy();
        this.destroyArmorStand();
    }

}
