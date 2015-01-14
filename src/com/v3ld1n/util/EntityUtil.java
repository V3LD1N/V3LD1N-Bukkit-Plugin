package com.v3ld1n.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import com.v3ld1n.V3LD1N;

public class EntityUtil {
    private static final Random random = new Random();

    private EntityUtil() {
    }
    
    /**
     * Pushes an entity towards a location
     * @param entity the entity to push
     * @param to the location to push the entity towards
     * @param speedX the x velocity
     * @param speedY the y velocity
     * @param speedZ the z velocity
     */
    public static void pushToward(Entity entity, Location to, double speedX, double speedY, double speedZ) {
        Location loc = entity.getLocation();
        Vector direction = loc.toVector().subtract(to.toVector()).normalize();
        direction.setX(direction.getX()*-(speedX))
                .setY(direction.getY()*-(speedY))
                .setZ(direction.getZ()*-(speedZ));
        entity.setVelocity(direction);
        if (entity instanceof Fireball) {
            ((Fireball) entity).setDirection(direction);
        }
    }

    /**
     * Randomly changes an entity's direction
     * @param entity the entity to move
     * @param distance the maximum distance from the starting location
     */
    public static void randomDirection(Entity entity, double distance) {
        Vector direction = entity.getLocation().getDirection();
        Vector newDirection;
        direction.add(new Vector(random.nextDouble() * distance, random.nextDouble() * distance, random.nextDouble() * distance));
        newDirection = direction.subtract(new Vector(random.nextDouble() * distance, random.nextDouble() * distance, random.nextDouble() * distance));
        entity.getLocation().setDirection(newDirection);
        entity.setVelocity(newDirection);
    }
    
    /**
     * Add health to a living entity
     * @param entity the entity to heal
     * @param health the amount of health to heal
     */
    public static void heal(LivingEntity entity, double health) {
        if (entity.getHealth() + health > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        } else {
            entity.setHealth(entity.getHealth() + health);
        }
    }
    
    /**
     * Launches a firework rocket that explodes 1 tick later
     * @param location the location to launch the rocket at
     * @param type the type of explosion
     * @param color the color of the explosion
     * @param fade the fade color of the explosion
     */
    public static void displayFireworkEffect(Location location, Type type, Color color, Color fade) {
        final Firework fw = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect fwe = FireworkEffect.builder()
                .with(type)
                .withColor(color)
                .withFade(fade)
                .withTrail()
                .withFlicker()
                .build();
        fwm.addEffects(fwe);
        fw.setFireworkMeta(fwm);
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                fw.detonate();
            }
        }, 1L);
    }

    /**
     * Strikes lightning at a location and destroys the projectile
     * @param projectile the projectile to destroy
     * @param location the location of the lightning strike
     * @param damage whether the lightning should do damage
     */
    public static void detonateLightningProjectile(Projectile projectile, Location location, boolean damage) {
        if (damage) {
            projectile.getWorld().strikeLightning(location);
        } else {
            projectile.getWorld().strikeLightningEffect(location);
        }
        projectile.remove();
    }

    /**
     * Strikes lightning at a location and destroys the projectile
     * @param projectile the projectile to destroy
     * @param location the location of the lightning strike
     */
    public static void detonateLightningProjectile(Projectile projectile, Location location) {
        projectile.getWorld().strikeLightning(location);
        projectile.remove();
    }

    /**
     * Deletes the projectile and spawns an instantly detonating firework rocket at its location
     * @param projectile the projectile
     * @param location the location to spawn the firework
     * @param type the firework type
     * @param color the firework color
     * @param fade the firework fade color
     */
    public static void detonateFireworkProjectile(Projectile projectile, Location location, Type type, Color color, Color fade) {
        displayFireworkEffect(location, type, color, fade);
        projectile.remove();
    }

    /**
     * Makes a projectile home toward monsters
     * @param projectile the projectile
     * @param ticks the interval in ticks
     */
    public static void homingProjectileTask(final Projectile projectile, long ticks) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Entity e = projectile.getNearbyEntities(25, 25, 25).get(0);
                Vector direction = projectile.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                direction.setX(direction.getX()*-(1.0))
                        .setY(direction.getY()*-(1.0))
                        .setZ(direction.getZ()*-(1.0));
                projectile.setVelocity(direction);
            }
        }, ticks, ticks);
    }

    /**
     * Makes a player "jump" away from a projectile
     * @param entity the jumping player
     * @param projectile the projectile
     * @param speedX the X-axis speed
     * @param speedY the Y-axis speed
     * @param speedZ the Z-axis speed
     */
    public static void projectileJump(LivingEntity entity, Projectile projectile, double speedX, double speedY, double speedZ) {
        boolean sneaking = false;
        if (entity.getType() == EntityType.PLAYER && ((Player) entity).isSneaking()) {
            sneaking = true;
        }
        if (sneaking) {
            double xz = 1.5;
            double y = 1.1;
            BlockFace down = BlockFace.DOWN;
            Location loc = entity.getLocation();
            Material block = loc.getBlock().getType();
            Material blockBelow = loc.getBlock().getRelative(down).getType();
            Material blockTwoBelow = loc.getBlock().getRelative(down).getRelative(down).getType();
            Material air = Material.AIR;
            if (block == air && blockBelow != air) {
                pushToward(entity, projectile.getLocation(), -speedX * xz, -speedY * y, -speedZ * xz);
            } else if (block == air && blockBelow == air && blockTwoBelow != air) {
                pushToward(entity, projectile.getLocation(), -speedX * xz, -speedY * y, -speedZ * xz);
            } else if (block == air && blockBelow == air && blockTwoBelow == air) {
                entity.setVelocity(new Vector(0, speedY * y, 0));
            } else if (block != air) {
                pushToward(entity, projectile.getLocation(), -speedX * xz, -speedY * y, -speedZ * xz);
            }
        } else {
            pushToward(entity, projectile.getLocation(), -speedX, -speedY, -speedZ);
        }
    }
}