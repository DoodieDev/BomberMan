package doodieman.bomberman.maphandler;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import doodieman.bomberman.maphandler.objects.GameMap;
import doodieman.bomberman.spawn.SpawnUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MapHandler {


    @Getter @Setter//The current loaded map
    private GameMap currentMap;
    @Getter
    private Location currentCorner1;
    @Getter
    private Location currentCorner2;

    @Getter
    private Location zeroLocation;

    @Getter
    private final MapUtil util;

    public MapHandler() {
        this.util = new MapUtil(this);
        this.zeroLocation = new Location(Bukkit.getWorld("gameworld"), 0, 0, 0);

        //Select a map
        this.currentMap = util.getAppropiateMap(0);
        if (currentMap == null) {
            Bukkit.broadcastMessage("§cThere is no maps available! The game will not work!");
            return;
        }

        this.pasteMapSchematic();

        //Teleport all players to the spawn, if they are in survival or adventure
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL) continue;
            player.teleport(SpawnUtil.getSpawnLocation());
        }
    }

    //Paste the map schematic
    public void pasteMapSchematic() {
        this.currentCorner1 = MapUtil.getInstance().getLocationFromOffset(currentMap.getCorner1());
        this.currentCorner2 = MapUtil.getInstance().getLocationFromOffset(currentMap.getCorner2());

        try {
            File file = new File(currentMap.getSchematicPath());
            Vector pasteLocation = new Vector(zeroLocation.getX(), zeroLocation.getY(), zeroLocation.getZ());
            com.sk89q.worldedit.world.World pasteWorld = new BukkitWorld(zeroLocation.getWorld());
            WorldData pasteWorldData = pasteWorld.getWorldData();
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(pasteWorldData);
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(pasteWorld, -1);
            Operation operation = clipboardHolder
                .createPaste(editSession, pasteWorldData)
                .to(pasteLocation)
                .ignoreAirBlocks(false)
                .build();
            Operations.completeLegacy(operation);

            editSession.flushQueue();

        } catch (IOException | MaxChangedBlocksException exception) {
            exception.printStackTrace();
        }
    }

    //Remove the current map schematic
    public void removeMapSchematic() {
        Location corner1 = util.getLocationFromOffset(currentMap.getCorner1());
        Location corner2 = util.getLocationFromOffset(currentMap.getCorner2());
        com.sk89q.worldedit.world.World editWorld = new BukkitWorld(corner1.getWorld());
        Vector pos1 = new Vector(corner1.getX(), corner1.getY(), corner1.getZ());
        Vector pos2 = new Vector(corner2.getX(), corner2.getY(), corner2.getZ());
        CuboidRegion region = new CuboidRegion(editWorld, pos1, pos2);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(editWorld, -1);
        editSession.setBlocks(region, new BaseBlock(0));
        editSession.flushQueue();
    }

}
