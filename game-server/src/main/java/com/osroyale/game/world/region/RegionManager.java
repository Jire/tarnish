package com.osroyale.game.world.region;

import com.google.common.base.Preconditions;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.*;
import java.util.function.Consumer;

/**
 * Manages the world regions.
 *
 * @author Graham Edgecombe
 */
public class RegionManager {

    /** The active (loaded) region map. */
    private final Int2ObjectMap<Region> activeRegions = new Int2ObjectOpenHashMap<>();

    /**
     * Sends an action to {@link Mob} instance which is within a {@code
     * distance}.
     *
     * @param action action consumer.
     */
    public static void forNearbyPlayer(Mob mob, int distance, Consumer<Player> action) {
        mob.getRegion().getSurroundingRegions().ifPresent(regions -> {
            for (Region region : regions) {
                for (Player nearby : region.getPlayers(mob.getHeight())) {
                    if (nearby == null) continue;
                    if (Utility.getDistance(nearby, mob) > distance) continue;
                    if (nearby.getCurrentHealth() <= 0 || nearby.isDead()) continue;
                    action.accept(nearby);
                }
            }
        });
    }

    /**
     * Sends an action to {@link Mob} instance which is within a {@code
     * distance}.
     *
     * @param action action consumer.
     */
    public static void forNearbyPlayer(Position position, int distance, Consumer<Player> action) {
        position.getRegion().getSurroundingRegions().ifPresent(regions -> {
            for (Region region : regions) {
                for (Player other : region.getPlayers(position.getHeight())) {
                    if (other == null) continue;
                    if (Utility.getDistance(other, position) > distance) continue;
                    if (other.getCurrentHealth() <= 0 || other.isDead()) continue;
                    action.accept(other);
                }
            }
        });
    }

    /**
     * Gets the local players around an entity.
     *
     * @param mob The entity.
     * @return The collection of local players.
     */
    public List<Player> getLocalPlayers(Mob mob) {
        Preconditions.checkArgument(mob != null, "mob is null");
        List<Player> localPlayers = new LinkedList<>();
        for (Region region : getSurroundingRegions(mob.getPosition())) {
            for (Player player : region.getPlayers(mob.getHeight())) {
                if (mob.getPosition().isWithinDistance(player.getPosition(), Region.VIEW_DISTANCE)) {
                    localPlayers.add(player);
                }
            }
        }
        return localPlayers;
    }

    /**
     * Gets the local npcs around an entity.
     *
     * @param mob The entity.
     * @return The collection of local npcs.
     */
    public List<Npc> getLocalNpcs(Mob mob) {
        List<Npc> localNpcs = new LinkedList<>();
        for (Region region : getSurroundingRegions(mob.getPosition())) {
            for (Npc npc : region.getNpcs(mob.getHeight())) {
                if (mob.getPosition().isWithinDistance(npc.getPosition(), Region.VIEW_DISTANCE)) {
                    localNpcs.add(npc);
                }
            }
        }
        return localNpcs;
    }

    /**
     * Gets the regions surrounding a position.
     *
     * @param position The position.
     * @return The regions surrounding the position.
     */
    public Region[] getSurroundingRegions(Position position) {
        Region target = getRegion(position.getX(), position.getY());

        if (target.getSurroundingRegions().isPresent()) {
            return target.getSurroundingRegions().get();
        }

        Set<Region> surrounding = new HashSet<>();
        int chunkX = position.getChunkX();
        int chunkY = position.getChunkY();

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int xx = (chunkX + x * 8 + 6) << 3;
                int yy = (chunkY + y * 8 + 6) << 3;
                Region region = getRegion(xx, yy);

                if (!surrounding.contains(region)) {
                    surrounding.add(region);
                }
            }
        }

        Region[] x = surrounding.toArray(new Region[surrounding.size()]);
        target.setSurroundingRegions(Optional.of(x));
        return target.getSurroundingRegions().get();
    }

    /**
     * Gets a region by position.
     *
     * @param position The position.
     * @return The region.
     */
    public Region getRegion(Position position) {
        return getRegion(position.getX(), position.getY());
    }

    /**
     * Gets a region by its x and y coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The region.
     */
    public Region getRegion(final int x, final int y) {
        final int hash = hash(x, y);

        final Region region = activeRegions.get(hash);
        if (region != null) {
            return region;
        }

        final Region newRegion = new Region(x, y);
        activeRegions.put(hash, newRegion);
        return newRegion;
    }

    private static int hash(int x, int y) {
        return ((x >> 6) << 8) + (y >> 6);
    }

}
