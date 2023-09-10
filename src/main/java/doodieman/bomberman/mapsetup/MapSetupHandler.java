package doodieman.bomberman.mapsetup;

import doodieman.bomberman.BomberMan;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class MapSetupHandler {

    @Getter
    private final MapSetupUtil util;

    public MapSetupHandler() {
        this.util = new MapSetupUtil(this);

        //Create the maps section in dataConfig if it doesn't exist
        FileConfiguration dataConfig = BomberMan.getInstance().getDataConfig();
        if (!dataConfig.contains("maps")) {
            dataConfig.createSection("maps");
            BomberMan.getInstance().saveDataConfig();
        }
    }

}
