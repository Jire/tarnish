package com.osroyale.fs.cache.decoder;

import com.osroyale.fs.cache.FileSystem;
import com.osroyale.fs.cache.archive.Archive;
import com.osroyale.fs.util.ByteBufferUtil;
import com.osroyale.game.world.object.GameObjectDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * A class which parses object definitions.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ObjectDefinitionDecoder implements Runnable {

    /** The logger to log process output. */
    private final static Logger LOGGER = LogManager.getLogger();

    /** The IndexedFileSystem. */
    private final FileSystem fs;

    /**
     * Creates the {@link ObjectDefinitionDecoder}.
     *
     * @param fs The {@link FileSystem}.
     */
    public ObjectDefinitionDecoder(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public void run() {
        LOGGER.info("Loading object definitions.");
        Archive archive = fs.getArchive(FileSystem.CONFIG_ARCHIVE);
        ByteBuffer dat = archive.getData("loc.dat");
        ByteBuffer idx = archive.getData("loc.idx");

        int count = idx.getShort() & 0xFFFF;

        if (count != GameObjectDefinition.MAX_DEFINITIONS) {
            throw new AssertionError("GameObjectDefinition size should be " + GameObjectDefinition.MAX_DEFINITIONS + ", not " + count);
        }

        int pos = 0;
        int loaded = 0;
        for (int index = 0; index < count; index++) {
            dat.position(pos);
            decode(index, dat);
            pos += idx.getShort() & 0xFFFF;
            loaded++;
        }
        LOGGER.info("Loaded " + loaded + " object definitions.");
    }

    /**
     * Parses a single game object definition by reading object info from a
     * buffer.
     *
     * @param id  The id of the object.
     * @param buffer The buffer.
     */
    private static void decode(int id, ByteBuffer buffer) {
        String name = "null";
        String desc = "null";
        int width = 1;
        int length = 1;
        boolean solid = true;
        boolean impenetrable = true;
        boolean hasActions = false;
        boolean interactive = false;
        boolean wall = false;
        boolean decoration = false;
        boolean removeClipping = false;
        boolean models = false;
        boolean is10 = true;
        int walkingFlag = 0;

        int lastOpcode = -1;
        while (true) {
            int opcode = buffer.get() & 0xff;
            if (opcode == 0) {
                break;
            }

            if (opcode == 1) {
                int len = buffer.get();
                if (len > 0 && !models) {
                    for (int idx = 0; idx < len; idx++) {
                        buffer.position(buffer.position() + Short.BYTES);
                        if (buffer.get() != 10 && idx == 0) {
                            is10 = false;
                        }
                    }
                } else {
                    buffer.position(buffer.position() + len * (Byte.BYTES + Short.BYTES));
                }
            } else if (opcode == 2) {
                name = ByteBufferUtil.readStringCp1252NullTerminated(buffer);
            } else if (opcode == 3) {

            } else if (opcode == 5) {
                int len = buffer.get();
                if (len > 0) {
                    models = true;
                    buffer.position(buffer.position() + len * Short.BYTES);
                }
            } else if (opcode == 14) {
                width = buffer.get();
            } else if (opcode == 15) {
                length = buffer.get();
            } else if (opcode == 17) {
                solid = false;
                impenetrable = false;
            } else if (opcode == 18) {
                impenetrable = false;
            } else if (opcode == 19) {
                hasActions = (buffer.get() == 1);
            } else if (opcode == 21) {

            } else if (opcode == 22) {

            } else if (opcode == 23) {

            } else if (opcode == 24) {
                buffer.getShort();
            } else if (opcode == 27) {
                // interact type = 1
            } else if (opcode == 28) {
                buffer.get();
            } else if (opcode == 29) {
                buffer.get();
            } else if (opcode == 39) {
                buffer.get();
            } else if (opcode >= 30 && opcode < 35) {
                ByteBufferUtil.readStringCp1252NullTerminated(buffer);
                interactive = true;
            } else if (opcode == 40) {
                int len = buffer.get();
                for (int i = 0; i < len; i++) {
                    buffer.getShort();
                    buffer.getShort();
                }
            } else if (opcode == 41) {
                int len = buffer.get();
                for (int i = 0; i < len; i++) {
                    buffer.getShort();
                    buffer.getShort();
                }
            } else if (opcode == 61) {
                buffer.getShort();
            } else if (opcode == 62) {
                wall = true;
            } else if (opcode == 64) {

            } else if (opcode == 65) {
                buffer.getShort();
            } else if (opcode == 66) {
                buffer.getShort();
            } else if (opcode == 67) {
                buffer.getShort();
            } else if (opcode == 68) {
                buffer.getShort();
            } else if (opcode == 69) {
                walkingFlag = buffer.get();
            } else if (opcode == 70) {
                buffer.getShort();
            } else if (opcode == 71) {
                buffer.getShort();
            } else if (opcode == 72) {
                buffer.getShort();
            } else if (opcode == 73) {
                decoration = true;
            } else if (opcode == 74) {
                removeClipping = true;
            } else if (opcode == 75) {
                buffer.get();
            } else if (opcode == 77) {
                buffer.getShort();
                buffer.getShort();

                int len = buffer.get() & 0xFF;
                for (int i = 0; i <= len; ++i) {
                    buffer.getShort();
                }
            } else if (opcode == 78) {
                buffer.getShort();
                buffer.get();
            } else if (opcode == 79) {
                buffer.getShort();
                buffer.getShort();
                buffer.get();

                int len = buffer.get() & 0xFF;
                for (int i = 0; i < len; ++i) {
                    buffer.getShort();
                }
            } else if (opcode == 81) {
                buffer.get();
            } else if (opcode == 82) {
                buffer.getShort();
            } else if (opcode == 89) {

            } else if (opcode == 92) {
                buffer.getShort();
                buffer.getShort();
                buffer.getShort();

                int len = buffer.get() & 0xFF;
                for (int i = 0; i <= len; ++i) {
                    buffer.getShort();
                }
            } else if (opcode == 249) {
                int len = buffer.get();
                for (int i = 0; i < len; i++) {
                    boolean isString = buffer.get() == 1;
                    ByteBufferUtil.getMedium(buffer);
                    if (isString) {
                        ByteBufferUtil.readStringCp1252NullTerminated(buffer);
                    } else {
                        buffer.getInt();
                    }
                }
            } else {
                throw new RuntimeException("unknown object opcode: " + opcode + ", last="+lastOpcode);
            }
            lastOpcode = opcode;
        }

        if (!name.equals("null")) {
            hasActions = models && is10;
            if (interactive) {
                hasActions = true;
            }
        }

        if (removeClipping) {
            solid = false;
            impenetrable = false;
        }

        int distance = 1;

        switch(id) {
            case 31561:
                distance = 2;
                break;
            case 26254:
                name = "Donator Box";
                break;
        }

        GameObjectDefinition.addDefinition(new GameObjectDefinition(id, name, desc, width, length, distance, solid, impenetrable, hasActions, wall, decoration, walkingFlag));
    }
}
