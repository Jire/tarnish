package com.osroyale.fs.cache;

import com.google.common.base.Preconditions;
import com.osroyale.fs.cache.archive.Archive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.CRC32;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Represents a file system of {@link Cache}s and {@link Archive}s.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class FileSystem {

    /** Represents the id of the configurations cache. */
    public static final int CONFIG_INDEX = 0;

    /** Represents the id of the model cache. */
    public static final int MODEL_INDEX = 1;

    /** Represents the id of the animations cache. */
    public static final int ANIMATION_INDEX = 2;

    /** Represents the id of the sounds and music cache. */
    public static final int MIDI_INDEX = 3;

    /** Represents the id of the tool.mapviewer and landscape cache. */
    public static final int MAP_INDEX = 4;

    /** Represents the id of the title screen archive. */
    public static final int TITLE_ARCHIVE = 1;

    /** Represents the id of the configurations archive. */
    public static final int CONFIG_ARCHIVE = 2;

    /** Represents the id of the interface archive. */
    public static final int INTERFACE_ARCHIVE = 3;

    /** Represents the id of the media and sprite archive. */
    public static final int MEDIA_ARCHIVE = 4;

    /** Represents the id of the manifest archive. */
    public static final int MANIFEST_ARCHIVE = 5;

    /** Represents the id of the textures archive. */
    public static final int TEXTURES_ARCHIVE = 6;

    /**
     * Represents the id of the word archive - user for storing profane or
     * illegal words not allowed to be spoken in-game.
     */
    public static final int WORD_ARCHIVE = 7;

    /** Represents the id of the sound and music archive. */
    public static final int SOUND_ARCHIVE = 8;

    /** Represents the maximum amount of archives within this file system. */
    private static final int MAXIMUM_ARCHIVES = 9;

    /** Represents the maximum amount of indices within this file system. */
    private static final int MAXIMUM_INDICES = 256;

    /** Represents the prefix of this {@link FileSystem}s main cache files. */
    private static final String DATA_PREFIX = "main_file_cache.dat";

    /** Represents the prefix of this {@link FileSystem}s index files. */
    private static final String INDEX_PREFIX = "main_file_cache.idx";

    /** All of the {@link Cache}s within this {@link FileSystem}. */
    private final Cache[] caches;

    /** All of the {@link Archive}s within this {@link FileSystem}. */
    private final Archive[] archives;

    /** The cached archive hashes. */
    private ByteBuffer archiveHashes;

    /**
     * Constructs a new {@link FileSystem} with the specified {@link Cache}s and
     * {@link Archive}s
     *
     * @param caches   All of the {@link Cache}s within this {@link
     *                 FileSystem}.
     * @param archives All of the {@link Archive}s within this {@link
     *                 FileSystem}.
     */
    private FileSystem(Cache[] caches, Archive[] archives) {
        this.caches = caches;
        this.archives = archives;
    }

    /**
     * Constructs and initializes a {@link FileSystem} from the specified {@code
     * directory}.
     *
     * @param directory The directory of the {@link FileSystem}.
     * @return The constructed {@link FileSystem} instance.
     * @throws IOException If some I/O exception occurs.
     */
    public static FileSystem create(String directory) throws IOException {
        Path root = Paths.get(directory);
        Preconditions.checkArgument(Files.isDirectory(root), "Supplied path must be a directory! " + root);

        Path data = root.resolve(DATA_PREFIX);
        Preconditions.checkArgument(Files.exists(data), "No data file found in the specified path!");

        SeekableByteChannel dataChannel = Files.newByteChannel(data, READ, WRITE);

        Cache[] caches = new Cache[MAXIMUM_INDICES];
        Archive[] archives = new Archive[MAXIMUM_ARCHIVES];

        for (int index = 0; index < caches.length; index++) {
            Path path = root.resolve(INDEX_PREFIX + index);
            if (Files.exists(path)) {
                SeekableByteChannel indexChannel = Files.newByteChannel(path, READ, WRITE);
                caches[index] = new Cache(dataChannel, indexChannel, index);
            }
        }

        // We don't use index 0
        for (int id = 1; id < archives.length; id++) {
            Cache cache = Objects.requireNonNull(caches[CONFIG_INDEX], "Configuration cache is null - unable to decode archives");
            archives[id] = Archive.decode(cache.get(id));
        }

        return new FileSystem(caches, archives);
    }

    /**
     * Gets an {@link Archive} for the specified {@code id}, this method
     * fails-fast if no archive can be found.
     *
     * @param id The id of the {@link Archive} to fetch.
     * @return The {@link Archive} for the specified {@code id}.
     * @throws NullPointerException If the archive cannot be found.
     */
    public Archive getArchive(int id) {
        Preconditions.checkElementIndex(id, archives.length);
        return Objects.requireNonNull(archives[id]);
    }

    /**
     * Gets a {@link Cache} for the specified {@code id}, this method fails-fast
     * if no cache can be found.
     *
     * @param id The id of the {@link Cache} to fetch.
     * @return The {@link Cache} for the specified {@code id}.
     * @throws NullPointerException If the cache cannot be found.
     */
    public Cache getCache(int id) {
        Preconditions.checkElementIndex(id, caches.length);
        return Objects.requireNonNull(caches[id]);
    }

    /**
     * Returns a {@link ByteBuffer} of file data for the specified index within
     * the specified {@link Cache}.
     *
     * @param cacheId The id of the cache.
     * @param indexId The id of the index within the cache.
     * @return A {@link ByteBuffer} of file data for the specified index.
     * @throws IOException If some I/O exception occurs.
     */
    public ByteBuffer getFile(int cacheId, int indexId) throws IOException {
        Cache cache = getCache(cacheId);
        synchronized (cache) {
            return cache.get(indexId);
        }
    }

    /**
     * Returns the cached {@link #archiveHashes} if they exist, otherwise they
     * are calculated and cached for future use.
     *
     * @return The hashes of each {@link Archive}.
     * @throws IOException If some I/O exception occurs.
     */
    public ByteBuffer getArchiveHashes() throws IOException {
        synchronized (this) {
            if (archiveHashes != null) {
                return archiveHashes.duplicate();
            }
        }

        int[] crcs = new int[MAXIMUM_ARCHIVES];

        CRC32 crc32 = new CRC32();
        for (int file = 1; file < crcs.length; file++) {
            crc32.reset();

            ByteBuffer buffer = getFile(CONFIG_INDEX, file);
            crc32.update(buffer);

            crcs[file] = (int) crc32.getValue();
        }

        ByteBuffer buffer = ByteBuffer.allocate((crcs.length + 1) * Integer.BYTES);

        int hash = 1234;
        for (int crc : crcs) {
            hash = (hash << 1) + crc;
            buffer.putInt(crc);
        }

        buffer.putInt(hash);
        buffer.flip();

        synchronized (this) {
            archiveHashes = buffer.asReadOnlyBuffer();
            return archiveHashes.duplicate();
        }
    }

}