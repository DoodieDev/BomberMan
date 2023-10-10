package doodieman.bomberman;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import doodieman.bomberman.game.GameHandler;
import doodieman.bomberman.gameworld.GameWorldHandler;
import doodieman.bomberman.lobby.LobbyHandler;
import doodieman.bomberman.maphandler.MapHandler;
import doodieman.bomberman.mapsetup.MapSetupHandler;
import doodieman.bomberman.mapsetup.command.MapSetupCommand;
import doodieman.bomberman.playerdata.PlayerDataHandler;
import doodieman.bomberman.playerdata.command.StatsCommand;
import doodieman.bomberman.ranking.RankingHandler;
import doodieman.bomberman.simpleevents.SimpleEventsListener;
import doodieman.bomberman.spawn.SpawnCommand;
import doodieman.bomberman.warp.commands.DeleteWarpCommand;
import doodieman.bomberman.warp.commands.SetWarpCommand;
import doodieman.bomberman.warp.commands.WarpCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class BomberMan extends JavaPlugin {

    @Getter
    private static BomberMan instance;

    @Getter
    private GameWorldHandler gameWorldHandler;
    @Getter
    private MapHandler mapHandler;
    @Getter
    private LobbyHandler lobbyHandler;
    @Getter
    private GameHandler gameHandler;
    @Getter
    private PlayerDataHandler playerDataHandler;
    @Getter
    private RankingHandler rankingHandler;

    @Getter
    private FileConfiguration dataConfig;

    //External dependencies
    @Getter
    private WorldEditPlugin worldEdit;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.loadDataConfig();

        //External dependencies
        this.worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        //Commands
        this.getCommand("mapsetup").setExecutor(new MapSetupCommand());

        this.getCommand("setwarp").setExecutor(new SetWarpCommand());
        this.getCommand("delwarp").setExecutor(new DeleteWarpCommand());
        this.getCommand("warp").setExecutor(new WarpCommand());
        this.getCommand("spawn").setExecutor(new SpawnCommand());
        this.getCommand("stats").setExecutor(new StatsCommand());

        //Handlers
        new MapSetupHandler();
        this.playerDataHandler = new PlayerDataHandler();

        this.gameWorldHandler = new GameWorldHandler();
        this.mapHandler = new MapHandler();
        this.lobbyHandler = new LobbyHandler();
        this.gameHandler = new GameHandler();
        this.rankingHandler = new RankingHandler();

        //Events
        Bukkit.getPluginManager().registerEvents(new SimpleEventsListener(),this);
    }

    @Override
    public void onDisable() {

    }

    private void loadDataConfig() {
        File dataFile = new File(this.getDataFolder(), "data.yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ignored) { }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveDataConfig() {
        File dataFile = new File(this.getDataFolder(), "data.yml");
        try {
            dataConfig.save(dataFile);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
