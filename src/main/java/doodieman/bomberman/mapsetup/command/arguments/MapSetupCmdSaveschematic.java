package doodieman.bomberman.mapsetup.command.arguments;

import com.sk89q.worldedit.bukkit.selections.Selection;
import doodieman.bomberman.BomberMan;
import doodieman.bomberman.mapsetup.MapSetupUtil;
import doodieman.bomberman.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MapSetupCmdSaveschematic {

    public MapSetupCmdSaveschematic(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§c/mapsetup saveschematic <MapID>");
            return;
        }

        String mapID = args[1].toLowerCase();
        MapSetupUtil util = MapSetupUtil.getInstance();

        if (!util.doesMapExist(mapID)) {
            player.sendMessage("§cMap med det ID eksisterer ikke!");
            return;
        }

        //Get players selection
        Selection selection = BomberMan.getInstance().getWorldEdit().getSelection(player);
        if (selection == null) {
            player.sendMessage("§cDu skal markere et område med WorldEdit");
            return;
        }

        Location corner1 = selection.getMinimumPoint();
        Location corner2 = selection.getMaximumPoint();

        util.saveSchematic(mapID,corner1,corner2);

        player.sendMessage("§aSchematic er blevet gemt!");
        player.sendMessage("§a§lHusk! §aHvis du har ændret størrelsen på mappet, er det vigtigt du sætter spawnpoints fra ny.");
    }

}
