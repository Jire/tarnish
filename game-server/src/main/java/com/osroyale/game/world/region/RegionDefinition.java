package com.osroyale.game.world.region;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Represents a single region definition.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class RegionDefinition {

    /** The region definitions. */
    private final static Int2ObjectMap<RegionDefinition> DEFINITIONS = new Int2ObjectOpenHashMap<>(8192);

    /** The hash of the region coordinates. */
    private final int hash;

    /** The terrain file id. */
    private final int terrainFile;

    /** The object file id. */
    private final int objectFile;

    /**
     * Constructs a new {@link RegionDefinition} with the specified hash,
     * terrain file id, object file id and preload state.
     *
     * @param hash        The hash of the region coordinates.
     * @param terrainFile The terrain file id.
     * @param objectFile  The object file id.
     */
    public RegionDefinition(int hash, int terrainFile, int objectFile) {
        this.hash = hash;
        this.terrainFile = terrainFile;
        this.objectFile = objectFile;
    }

    /**
     * Gets a {@link RegionDefinition}.
     *
     * @param region region hash to get definition from.
     * @return region definition
     */
    public static RegionDefinition get(int region) {
        return DEFINITIONS.get(region);
    }

    /**
     * Returns the flag if the region exists.
     *
     * @param region region hash to check.
     * @return contains flag.
     */
    public static boolean contains(int region) {
        return DEFINITIONS.containsKey(region);
    }

    /**
     * Adds a {@link RegionDefinition}.
     *
     * @param definition definition to add.
     */
    public static void set(RegionDefinition definition) {
        DEFINITIONS.put(definition.getHash(), definition);
    }

    /**
     * Gets the regional definitions.
     *
     * @return region definitions.
     */
    public static Int2ObjectMap<RegionDefinition> getDefinitions() {
        return DEFINITIONS;
    }

    /**
     * Returns the coordinate hash.
     */
    public int getHash() {
        return hash;
    }

    /**
     * Returns the terrain file id.
     */
    public int getTerrainFile() {
        return terrainFile;
    }

    /**
     * Returns the object file id.
     */
    public int getObjectFile() {
        return objectFile;
    }

}