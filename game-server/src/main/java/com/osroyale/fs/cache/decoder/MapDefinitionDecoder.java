package com.osroyale.fs.cache.decoder;

import com.osroyale.fs.cache.FileSystem;
import com.osroyale.fs.cache.archive.Archive;
import com.osroyale.game.world.region.RegionDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * A class which parses {@link RegionDefinition}s
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MapDefinitionDecoder implements Runnable {

    /** The logger that will print important information. */
    private final static Logger LOGGER = LogManager.getLogger();

    /** The IndexedFileSystem. */
    private final FileSystem fs;

    /**
     * Creates the {@link MapDefinitionDecoder}.
     *
     * @param fs The {@link FileSystem}.
     */
    public MapDefinitionDecoder(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public void run() {
        LOGGER.info("Loading region definitions.");
        Archive archive = fs.getArchive(FileSystem.MANIFEST_ARCHIVE);
        ByteBuffer buffer = archive.getData("map_index");
        int count = (buffer.getShort() & 0xFFFF);
        for (int i = 0; i < count; i++) {
            int hash = buffer.getShort() & 0xFFFF;
            int terrainFile = buffer.getShort() & 0xFFFF;
            int objectFile = buffer.getShort() & 0xFFFF;
            RegionDefinition.set(new RegionDefinition(hash, terrainFile, objectFile));
        }
        LOGGER.info("Loaded " + count + " region definitions.");
    }

}