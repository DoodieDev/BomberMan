package doodieman.bomberman.gameworld;

import doodieman.bomberman.spawn.SpawnUtil;
import doodieman.bomberman.utils.FileUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.File;

public class GameWorldHandler {

    @Getter
    private World world;

    public GameWorldHandler() {
        this.deleteGameWorld();
        this.createWorld();
    }

    public void createWorld() {
        WorldCreator worldCreator = new WorldCreator("gameworld");
        worldCreator.generator(new VoidWorldGenerator());
        worldCreator.type(WorldType.FLAT);
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.generateStructures(false);
        this.world = worldCreator.createWorld();
        this.world.setAutoSave(false);
        this.world.setGameRuleValue("doMobSpawning", "false");
        this.world.setGameRuleValue("randomTickSpeed", "0");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setGameRuleValue("doDaylightCycle", "false");
        this.world.setDifficulty(Difficulty.EASY);
        this.world.setTime(6000L);
    }

    public void deleteGameWorld() {
        World gameworld = Bukkit.getWorld("gameworld");

        if (gameworld != null) {
            for (Player player : gameworld.getPlayers())
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }

        try {
            Bukkit.unloadWorld("gameworld", false);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            System.out.println("Failed unloading the world!");
        }

        FileUtils.deleteDir(new File("gameworld"));
    }

}
