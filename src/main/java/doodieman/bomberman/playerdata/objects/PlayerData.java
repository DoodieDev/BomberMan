package doodieman.bomberman.playerdata.objects;

import doodieman.bomberman.BomberMan;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private OfflinePlayer player;
    private FileConfiguration data;

    public PlayerData(OfflinePlayer player) {
        this.player = player;
        load();
    }

    private void load() {
        //Creates the folder FirePvP/playerdata/ if it doesn't exist
        File folder = new File(BomberMan.getInstance().getDataFolder(), "playerdata");
        folder.mkdirs();

        //Creates the unique data file if it doesn't exist
        File dataFile = new File(folder, player.getUniqueId()+".yml");
        if (!dataFile.exists()) try {
            dataFile.createNewFile();
        } catch (IOException ex) { }

        //Loads the file as a bukkit config
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public FileConfiguration getFile() {
        return this.data;
    }

    public void save() {
        File folder = new File(BomberMan.getInstance().getDataFolder(), "playerdata");
        File dataFile = new File(folder, player.getUniqueId()+".yml");
        try {
            data.save(dataFile);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }


}
