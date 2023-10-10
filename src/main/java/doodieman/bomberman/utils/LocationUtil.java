package doodieman.bomberman.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.List;

public class LocationUtil {

    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
    }
    public static Location stringToLocation(String string) {
        String[] parts = string.split(";");
        if (parts.length != 6) return new Location(Bukkit.getWorld("world"), 0, 0, 0);

        World world = Bukkit.getWorld(parts[0]);
        float x = Float.parseFloat(parts[1]);
        float y = Float.parseFloat(parts[2]);
        float z = Float.parseFloat(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);

        return new Location(world, x, y, z, yaw, pitch);
    }


    public static String eulerAngleToString(EulerAngle eulerAngle) {
        return eulerAngle.getX()+";"+eulerAngle.getY()+";"+eulerAngle.getZ();
    }
    public static EulerAngle stringToEulerAngle(String string) {
        String[] parts = string.split(";");
        if (parts.length != 3) return new EulerAngle(0,0,0);

        float x = Float.parseFloat(parts[0]);
        float y = Float.parseFloat(parts[1]);
        float z = Float.parseFloat(parts[2]);

        return new EulerAngle(x,y,z);
    }

    public static String vectorToString(Vector vector) {
        return vector.getX()+";"+vector.getY()+";"+vector.getZ();
    }
    public static Vector stringToVector(String string) {
        String[] parts = string.split(";");
        if (parts.length != 3) return new Vector(0,0,0);

        float x = Float.parseFloat(parts[0]);
        float y = Float.parseFloat(parts[1]);
        float z = Float.parseFloat(parts[2]);

        return new Vector(x,y,z);
    }
    public static Vector getVectorOffset(Location origin, Location point) {
        double x = point.getX() - origin.getX();
        double y = point.getY() - origin.getY();
        double z = point.getZ() - origin.getZ();
        return new Vector(x,y,z);
    }


    public static boolean isInBoundaries(Location loc, Location corner1, Location corner2) {
        double
            x1 = Math.min(corner1.getX(), corner2.getX()),
            y1 = Math.min(corner1.getY(), corner2.getY()),
            z1 = Math.min(corner1.getZ(), corner2.getZ()),
            x2 = Math.max(corner1.getX(), corner2.getX()),
            y2 = Math.max(corner1.getY(), corner2.getY()),
            z2 = Math.max(corner1.getZ(), corner2.getZ());

        return
            isBetween(Math.floor(loc.getX()), x1, x2)
                && isBetween(Math.floor(loc.getY()), y1, y2)
                && isBetween(Math.floor(loc.getZ()), z1, z2);
    }

    public static double oppositeAngle(double angle) {
        double oppositeAngle = angle + 180;
        oppositeAngle = oppositeAngle % 360;
        if (oppositeAngle < 0) {
            oppositeAngle += 360;
        }
        return oppositeAngle;
    }

    public static double addAngle(double current, double add) {
        return (current + add) % 360;
    }

    private static boolean isBetween(double number, double min, double max) {
        return number >= min && number <= max;
    }

    //Moves location1 closer towards location2
    public static Location moveCloser(Location loc1, Location loc2, double distance) {
        double dx = loc2.getX() - loc1.getX();
        double dy = loc2.getY() - loc1.getY();
        double dz = loc2.getZ() - loc1.getZ();
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length <= distance) return loc2.clone();
        double newX = loc1.getX() + dx * (distance / length);
        double newY = loc1.getY() + dy * (distance / length);
        double newZ = loc1.getZ() + dz * (distance / length);
        return new Location(loc1.getWorld(), newX, newY, newZ);
    }

    public static Location getCenterLocation(Location loc1, Location loc2) {
        double centerX = (loc1.getX() + loc2.getX()) / 2.0;
        double centerY = (loc1.getY() + loc2.getY()) / 2.0;
        double centerZ = (loc1.getZ() + loc2.getZ()) / 2.0;

        return new Location(loc1.getWorld(), centerX, centerY, centerZ);
    }

    public static Location getLocationInCircle(Location loc, double angle, double radius) {
        double x = (loc.getX() + radius * Math.cos(angle * Math.PI / 180));
        double z = (loc.getZ() + radius * Math.sin(angle * Math.PI / 180));
        return new Location(loc.getWorld(), x, loc.getY(), z);
    }

    public static double getAngleToLocation(Location fromLoc, Location toLoc) {
        // Calculate the vector from 'fromLoc' to 'toLoc'
        double deltaX = toLoc.getX() - fromLoc.getX();
        double deltaZ = toLoc.getZ() - fromLoc.getZ();

        // Calculate the angle in radians
        double angle = Math.atan2(deltaZ, deltaX);

        // Ensure the angle is between 0 and 2*PI
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        // Convert radians to degrees
        double degrees = angle * 180 / Math.PI;

        return degrees;
    }

    public static Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public static Block getTargetBlock(Location location, int range) {
        BlockIterator iter = new BlockIterator(location, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }


    public static boolean isOnlyBlocksBetween(Location loc1, Location loc2, List<Material> materials, int pointsPerBlock) {
        World world = loc1.getWorld();

        double maxDistance = loc1.distance(loc2);
        int totalPoints = (int) Math.ceil(maxDistance * pointsPerBlock);
        double distancePerPoint = maxDistance / totalPoints;

        for (int i = 0; i <= totalPoints; i++) {
            double distance = i * distancePerPoint;
            Location location = LocationUtil.moveCloser(loc1,loc2,distance);
            Block block = world.getBlockAt(location);
            if (!materials.contains(block.getType()))
                return false;
        }

        return true;
    }

    public static void pushPlayerTowards(Player player, Location target, double strength) {
        Vector direction = target.toVector().subtract(player.getLocation().toVector());
        direction.normalize();
        direction.multiply(new Vector(strength, strength, strength));
        player.setVelocity(direction);
    }
    public static void pushPlayerAway(Player player, Location target, double strength) {
        Vector direction = player.getLocation().toVector().subtract(target.toVector());
        direction.normalize();
        direction.multiply(new Vector(strength, strength, strength));
        player.setVelocity(direction);
    }

}
