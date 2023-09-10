package doodieman.bomberman.lobby.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class LobbyPlayer {

    @Getter
    private final Player player;
    @Getter @Setter
    private boolean inQueue;

    public LobbyPlayer(Player player) {
        this.player = player;
        this.inQueue = false;
    }

}
