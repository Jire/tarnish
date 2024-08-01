package com.osroyale.fs.cache.decoder;

import com.osroyale.fs.cache.FileSystem;
import com.osroyale.fs.cache.archive.Archive;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class which parses animation definitions.
 */
public final class AnimationDefinitionDecoder implements Runnable {

    /** The logger to log process output. */
    private final static Logger LOGGER = LogManager.getLogger();

    public static final Int2ObjectMap<AnimationDefinition> definitions = new Int2ObjectOpenHashMap<>();

    /** The IndexedFileSystem. */
    private final FileSystem fs;

    public AnimationDefinitionDecoder(final FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public void run() {
        LOGGER.info("Loading animation definitions.");

        final Archive archive = fs.getArchive(FileSystem.CONFIG_ARCHIVE);
        final ByteBuf buffer = Unpooled.wrappedBuffer(archive.getData("seq.dat"));
        try {
            final int highestFileId = buffer.readUnsignedShort();
            for (int i = 0; i <= highestFileId; i++) {
                final int id = buffer.readUnsignedShort();
                if (id == 65535) break;

                final int length = buffer.readUnsignedShort();
                final ByteBuf animBuffer = buffer.readBytes(length);
                try {
                    final AnimationDefinition definition = new AnimationDefinition(id);
                    definition.decode(animBuffer);

                    definitions.put(id, definition);
                } finally {
                    animBuffer.release();
                }

                if (id >= highestFileId) break;
            }
        } finally {
            buffer.release();
        }

        LOGGER.info("Loaded " + definitions.size() + " animation definitions.");
    }

}
