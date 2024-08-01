package com.osroyale.fs.cache.decoder;

import com.osroyale.fs.cache.Cache;
import com.osroyale.fs.cache.FileSystem;
import com.osroyale.fs.util.ByteBufferUtil;
import com.osroyale.fs.util.CompressionUtil;
import com.osroyale.game.world.World;
import com.osroyale.game.world.object.*;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.game.world.region.RegionDefinition;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class which parses static object definitions, which include tool.mapviewer
 * tiles and landscapes.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class RegionDecoder implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The FileSystem.
     */
    private final FileSystem fs;

    /**
     * Amount of regions correctly decoded.
     */
    private final AtomicInteger decoded = new AtomicInteger();

    /**
     * Amount of regions incorrectly decoded.
     */
    private final AtomicInteger errors = new AtomicInteger();

    /**
     * Creates the {@link ObjectDefinitionDecoder}.
     *
     * @param fs The {@link FileSystem}.
     */
    public RegionDecoder(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public void run() {
        LOGGER.info("Loading regional map data.");
        final Cache mapIndex = fs.getCache(FileSystem.MAP_INDEX);
        RegionDefinition.getDefinitions()
                .values()
                .forEach(def -> load(mapIndex, def));
        LOGGER.info("Loaded " + decoded + " regions, skipped " + errors + " maps.");
    }

    public void load(final Cache mapIndex, final RegionDefinition def) {
        final int hash = def.getHash();
        final int x = (hash >> 8 & 0xFF) << 6;
        final int y = (hash & 0xFF) << 6;
        try {
            final Region region = World.getRegions().getRegion(x, y);

            ByteBuffer terrainData = mapIndex.get(def.getTerrainFile());
            if (terrainData == null) {
                errors.getAndIncrement();
                return;
            }
            ByteBuffer terrainBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(terrainData.array()));

            final IntSet downHeights = new IntOpenHashSet();

            parseTerrain(region, terrainBuffer, downHeights);

            ByteBuffer gameObjectData = mapIndex.get(def.getObjectFile());
            if (gameObjectData == null) {
                errors.getAndIncrement();
                return;
            }
            ByteBuffer gameObjectBuffer = ByteBuffer.wrap(CompressionUtil.gunzip(gameObjectData.array()));
            parseGameObject(region, gameObjectBuffer, x, y, downHeights);

            decoded.getAndIncrement();
        } catch (Exception e) {
            e.printStackTrace();
            errors.getAndIncrement();
        }
    }

    /**
     * Parses a {@link GameObject} on the specified coordinates.
     *
     * @param buf The uncompressed game object data buffer.
     * @param x   The x coordinate this object is on.
     * @param y   The y coordinate this object is on.
     */
    private void parseGameObject(Region region, ByteBuffer buf, int x, int y, IntSet downHeights) {
        int objId = -1;
        while (true) {
            int objIdOffset = ByteBufferUtil.getSmart(buf);
            if (objIdOffset == 0) break;

            objId += objIdOffset;
            int objPosInfo = 0;

            while (true) {
                int objPosInfoOffset = ByteBufferUtil.getSmart(buf);
                if (objPosInfoOffset == 0) break;
                objPosInfo += objPosInfoOffset - 1;

                int objOtherInfo = buf.get() & 0xFF;
                int localY = objPosInfo & 0x3f;
                int localX = objPosInfo >> 6 & 0x3f;
                int height = objPosInfo >> 12 & 0x3;

                Optional<ObjectType> type = ObjectType.valueOf(objOtherInfo >> 2);
                Optional<ObjectDirection> face = ObjectDirection.valueOf(objOtherInfo & 0x3);

                if (downHeights.contains(Position.hash(localX, localY, 1))) {
                    if (--height < 0) continue;
                } else if (downHeights.contains(Position.hash(localX, localY, height))) {
                    height--;
                }

                if (!type.isPresent() || !face.isPresent()) {
                    continue;
                }

                Position pos = new Position(localX + x, localY + y, height);
                GameObjectDefinition def = GameObjectDefinition.forId(objId);

                if (def == null)
                    continue;

                StaticGameObject staticObject = new StaticGameObject(def, pos, type.get(), face.get());

                if (Region.SKIPPED_OBJECTS.contains(pos)) {
                    region.skip(staticObject);
                } else {
                    TraversalMap.markObject(region, staticObject, true, true);
                }
            }
        }
    }

    /**
     * Loads all of the tool.mapviewer indexes entries and decodes each.
     *
     * @param mapBuffer The uncompressed tool.mapviewer entry data buffer.
     */
    private void parseTerrain(Region region, ByteBuffer mapBuffer, IntSet downHeights) {
        int[][][] attributes = new int[4][64][64];

        for (int height = 0; height < 4; height++) {
            for (int localX = 0; localX < 64; localX++) {
                for (int localY = 0; localY < 64; localY++) {
                    while (true) {
                        int attributeId = mapBuffer.getShort() & 0xFFFF;
                        if (attributeId == 0) {
                            break;
                        }
                        if (attributeId == 1) {
                            int tileHeight = mapBuffer.get() & 0xFF;
                            break;
                        }
                        if (attributeId <= 49) {
                            int overlayId = mapBuffer.getShort() & 0xFFFF;
                        } else if (attributeId <= 81) {
                            attributes[height][localX][localY] = attributeId - 49;
                        }
                    }
                }
            }
        }

        for (int height = 0; height < 4; height++) {
            for (int localX = 0; localX < 64; localX++) {
                for (int localY = 0; localY < 64; localY++) {

                    if ((attributes[height][localX][localY] & 2) == 2) {
                        downHeights.add(Position.hash(localX, localY, height));
                    }

                    if ((attributes[height][localX][localY] & 1) == 1) {
                        int plane = height;

                        if ((attributes[1][localX][localY] & 2) == 2) {
                            downHeights.add(Position.hash(localX, localY, 1));
                            plane--;
                        }

                        if (plane >= 0) {
                            TraversalMap.block(region, plane, localX, localY);
                        }
                    }
                }
            }
        }
    }

}
