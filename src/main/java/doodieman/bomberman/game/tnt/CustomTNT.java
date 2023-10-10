package doodieman.bomberman.game.tnt;

import doodieman.bomberman.BomberMan;
import doodieman.bomberman.game.GameUtil;
import doodieman.bomberman.game.loot.LootTable;
import doodieman.bomberman.game.objects.Game;
import doodieman.bomberman.game.objects.GamePlayer;
import doodieman.bomberman.playerdata.PlayerDataUtil;
import doodieman.bomberman.playerdata.PlayerStat;
import doodieman.bomberman.utils.LocationUtil;
import doodieman.bomberman.utils.PacketUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CustomTNT {

    @Getter
    private final CustomTNTType type;
    private final Game game;
    private final GameUtil util;

    @Getter @Setter
    private long fuseTicks;
    @Getter @Setter
    private double radius;
    @Getter
    private Location location;
    @Getter
    private BukkitTask fuseRunnable;
    @Getter
    private ArmorStand armorStand;
    @Getter
    private Location desiredArmorStandLocation;
    @Getter @Setter
    private Player playerWhoPlaced;
    @Getter
    private final List<Material> clearBlocks = Arrays.asList(Material.AIR, Material.BARRIER);

    private int blocksBroken;

    public CustomTNT(CustomTNTType type, long fuseTicks, double radius, Game game) {
        this.type = type;
        this.game = game;
        this.util = GameUtil.getInstance();
        this.fuseTicks = fuseTicks;
        this.radius = radius;
        this.fuseRunnable = null;
        this.location = null;
        this.playerWhoPlaced = null;
        this.blocksBroken = 0;
    }

    //This function can be overrided for custom tnts
    public List<MaterialData> getDetectableBlocks() {
        return Arrays.asList(
            new MaterialData(1, (byte) 0),
            new MaterialData(5, (byte) 1),
            new MaterialData(14, (byte) 0),
            new MaterialData(129, (byte) 0),
            new MaterialData(35, (byte) 6),
            new MaterialData(56, (byte) 0)
        );
    }

    public abstract void onExplode();

    //Place the TNT
    public void place(Location location) {
        this.location = location;

        //Spawn armorstand
        this.desiredArmorStandLocation = location.clone().add(0.5, -1.4, 0.5);
        Location asLocation = location.clone().add(0.5, 0, 0.5);

        this.armorStand = location.getWorld().spawn(asLocation,ArmorStand.class);
        this.armorStand.setHelmet(this.type.getItem());
        this.armorStand.setVisible(false);
        this.armorStand.setGravity(false);

        location.getWorld().playSound(location, Sound.FUSE, 1f, 1.2f);

        this.startFuseTimer();
    }

    //Destroy block (Only called from subclass)
    public void destroyBlock(Block block) {
        World world = block.getWorld();
        Location centerBlockLocation = block.getLocation().clone().add(0.5, 0.5, 0.5);

        //Drop random item
        ItemStack itemStack = LootTable.getRandomItem(new MaterialData(block.getType(), block.getData()));
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            Item item = world.dropItem(centerBlockLocation, itemStack);
            item.setVelocity(new Vector(0,0.2,0));
            item.setPickupDelay(0);
        }

        //Destroy the block
        world.spigot().playEffect(centerBlockLocation,Effect.TILE_BREAK,block.getTypeId(),0,0.5f,0.5f,0.5f,0,40,20);
        world.playSound(centerBlockLocation,Sound.DIG_STONE,1f,1f);
        block.setType(Material.AIR);
        blocksBroken++;
    }

    //Explode
    public void explodeEffect() {
        location.getWorld().playSound(location, Sound.ENDERDRAGON_HIT, 1f, 0.8f);
        this.playRippleEffect();
        this.onExplode();
    }

    public void destroy() {
        location.getWorld().getBlockAt(location).setType(Material.AIR);
        this.fuseRunnable.cancel();
        this.game.getPlacedTNTs().remove(this);

        //Drop the TNT
        type.dropAtLocation(this.getCenterLocation());

        //Add blocks broken stats
        PlayerDataUtil.addPlayerStat(playerWhoPlaced, PlayerStat.BLOCKS_BROKEN, blocksBroken);
        GamePlayer gamePlayer = this.util.getGamePlayer(playerWhoPlaced, game);
        if (gamePlayer != null)
            gamePlayer.addGameStat(PlayerStat.BLOCKS_BROKEN, blocksBroken);
    }

    public void destroyArmorStand() {
        armorStand.remove();
    }

    public void playRippleEffect() {

        double realRadius = this.getRealRadius();
        int particlesPerLine = 2 * (int) Math.round(realRadius);
        int lineAmount = 13 * (int) Math.round(realRadius);
        Location centerLocation = this.getCenterLocation();
        World world = centerLocation.getWorld();

        //Send lines of particles out in every direction
        for (int line = 0; line < lineAmount; line++) {

            double angle = (line * (360f / lineAmount));
            double maxLineRadius = this.getVisibleRadius(angle,realRadius);

            //Playout each particle in the line
            for (int particle = 0; particle <= particlesPerLine; particle++) {

                double radius = (realRadius / particlesPerLine) * particle;
                if (radius > maxLineRadius)
                    break;

                Location particleLocation = LocationUtil.getLocationInCircle(centerLocation,angle,radius);

                //Play the redstone particle for every player
                for (Player p : Bukkit.getOnlinePlayers())
                    PacketUtil.sendRedstoneParticle(p,particleLocation, Color.fromRGB(255, 0, 0));

                //Last particle in line - Explosion effect
                if (particle == particlesPerLine)
                    world.spigot().playEffect(particleLocation,Effect.EXPLOSION,0,0,0,0,0,0,1,50);
            }
        }
    }

    //Start the fusing timer. (Rotate animation)
    public void startFuseTimer() {
        this.fuseRunnable = new BukkitRunnable() {

            long tick = 0L;

            @Override
            public void run() {
                tick += 1L;

                if (tick == 1L) {
                    location.getWorld().getBlockAt(location).setType(Material.BARRIER);
                }

                //BOOM
                if (tick >= fuseTicks) {
                    explodeEffect();
                    this.cancel();
                    return;
                }

                //Rotate armorstand
                Location asLocation = armorStand.getLocation();
                double addedYaw = ((double) tick / fuseTicks) * 40;
                asLocation.setYaw(asLocation.getYaw() + (float) addedYaw);


                //Create "Falling" tnt animation
                if (asLocation.getY() > desiredArmorStandLocation.getY()) {
                    asLocation.setY(asLocation.getY() - 0.25);
                    if (asLocation.getY() < desiredArmorStandLocation.getY())
                        asLocation.setY(desiredArmorStandLocation.getY());
                }

                armorStand.teleport(asLocation);


                //Sound
                float pitch = (float) ((double) tick / fuseTicks);
                location.getWorld().playSound(location, Sound.ENDERDRAGON_WINGS, 0.05F, pitch);
            }
        }.runTaskTimer(BomberMan.getInstance(), 1L, 1L);
    }

    //Get all blocks in radius

    public List<Block> detectAllBlocksInRadius() {
        List<Block> detected = new ArrayList<>();
        List<MaterialData> detectable = this.getDetectableBlocks();

        World world = location.getWorld();
        double radiusSquared = this.getRealRadius() * this.getRealRadius();
        double centerX = location.getX();
        double centerZ = location.getZ();

        // Use a double loop for x and z with a step of 1.0
        for (double x = centerX - this.getRealRadius(); x <= centerX + this.getRealRadius(); x += 1.0) {
            for (double z = centerZ - this.getRealRadius(); z <= centerZ + this.getRealRadius(); z += 1.0) {
                Block block = world.getBlockAt((int) x, (int) location.getY(), (int) z);
                if (block.getLocation().distanceSquared(location) > radiusSquared) continue;
                if (!detectable.contains(new MaterialData(block.getType(), block.getData()))) continue;

                detected.add(block);
            }
        }
        return detected;
    }

    //Detect all players in radius
    public List<Player> detectAllPlayersInRadius() {
        List<Player> detected = new ArrayList<>();
        double radiusSquared = this.getRealRadius() * this.getRealRadius();
        for (GamePlayer gamePlayer : this.game.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player.getLocation().distanceSquared(this.getCenterLocation()) > radiusSquared) continue;
            detected.add(player);
        }
        return detected;
    }

    //Checks if the TNT can see a block.
    public boolean canSeeLocation(Location targetLocation) {
        World world = targetLocation.getWorld();

        Location centerLocation = this.location.clone().add(0.5, 0, 0.5);

        List<Location> blockLocations = Arrays.asList(
            targetLocation.clone().add(0.07, 0, 0.07),
            targetLocation.clone().add(-0.07, 0, 0.07),
            targetLocation.clone().add(-0.07, 0, -0.07),
            targetLocation.clone().add(0.07, 0, -0.07)
        );

        List<Material> clearBlockTypes = this.getClearBlocks();

        double maxDistance = targetLocation.distance(centerLocation);
        int totalPoints = (int) Math.ceil(maxDistance * 20);
        double distancePerPoint = maxDistance / totalPoints;

        //Loop all points
        for (int i = 0; i <= totalPoints; i++) {
            double distance = i * distancePerPoint;
            List<Block> blocksAt = new ArrayList<>();

            for (Location loc : blockLocations) {
                Location loc_closer = LocationUtil.moveCloser(loc, centerLocation, distance);
                blocksAt.add(world.getBlockAt(loc_closer));
            }

            //Go through blocks spotted
            for (Block blockAt : blocksAt) {
                Location blockLocation = blockAt.getLocation();
                Location originalBlockLocation = targetLocation.getBlock().getLocation();

                if (isWithinTolerance(blockLocation, originalBlockLocation, 0.001))
                    continue;
                if (!clearBlockTypes.contains(blockAt.getType()))
                    return false;
            }
        }
        return true;
    }

    //Get the visible radius in an angle from the bomb.
    public double getVisibleRadius(double angle, double maxRadius) {
        Location centerLocation = this.getCenterLocation();
        Location targetLocation = LocationUtil.getLocationInCircle(centerLocation,angle,maxRadius);

        World world = targetLocation.getWorld();

        List<Location> centerOffsetLocations = Arrays.asList(
            centerLocation.clone().add(0.07, 0, 0.07),
            centerLocation.clone().add(-0.07, 0, 0.07),
            centerLocation.clone().add(-0.07, 0, -0.07),
            centerLocation.clone().add(0.07, 0, -0.07)
        );

        List<Material> clearBlockTypes = this.getClearBlocks();
        int totalPoints = (int) Math.ceil(maxRadius * 20);
        double distancePerPoint = maxRadius / totalPoints;

        //Go through points between target and center
        for (int i = 0; i <= totalPoints; i++) {
            double radius = i * distancePerPoint;

            //Get all blocks in radius of the point (0.07 radius)
            List<Block> blocksAt = new ArrayList<>();
            for (Location loc : centerOffsetLocations) {
                Location loc_closer = LocationUtil.moveCloser(loc, targetLocation, radius);
                blocksAt.add(world.getBlockAt(loc_closer));
            }

            //Go through blocks spotted
            for (Block blockAt : blocksAt) {

                //If it detects a block that is not 'clear'. - Stop and return the radius.
                if (!clearBlockTypes.contains(blockAt.getType()))
                    return radius;
            }
        }
        return maxRadius;
    }

    private boolean isWithinTolerance(Location loc1, Location loc2, double tolerance) {
        return Math.abs(loc1.getX() - loc2.getX()) < tolerance
            && Math.abs(loc1.getY() - loc2.getY()) < tolerance
            && Math.abs(loc1.getZ() - loc2.getZ()) < tolerance;
    }

    public Location getCenterLocation() {
        return this.location.clone().add(0.5, 0, 0.5);
    }

    public double getRealRadius() {
        return this.radius - 0.5;
    }
}
