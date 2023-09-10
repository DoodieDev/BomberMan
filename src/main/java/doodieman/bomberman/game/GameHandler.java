package doodieman.bomberman.game;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.game.objects.Game;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;


public class GameHandler {

    @Getter
    private final GameUtil util;
    @Getter @Setter
    private Game activeGame;

    @Getter
    private static GameHandler instance;

    public GameHandler() {
        this.util = new GameUtil(this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), BomberMan.getInstance());

        this.activeGame = null;
        instance = this;
    }
}
