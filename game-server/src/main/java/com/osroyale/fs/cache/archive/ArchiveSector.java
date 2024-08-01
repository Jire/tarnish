package com.osroyale.fs.cache.archive;

import com.osroyale.fs.cache.FileSystem;

import java.nio.ByteBuffer;

/**
 * Represents a sector within an {@link Archive}. <p> A archive sector contains
 * a hashed name and compressed data, this data represents files and information
 * within the {@link FileSystem}. </p>
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ArchiveSector {

    /** The data within this sector. */
    private final ByteBuffer data;

    /** The hashed name of this sector. */
    private final int hash;

    /**
     * Constructs a new {@link ArchiveSector} with the specified data and hashed
     * name.
     *
     * @param data The data within this sector.
     * @param hash The hashed name of this sector.
     */
    protected ArchiveSector(ByteBuffer data, int hash) {
        this.data = data;
        this.hash = hash;
    }

    /** Returns the data within this sector. */
    public ByteBuffer getData() {
        return data;
    }

    /** Returns the hashed name of this sector. */
    public int getHash() {
        return hash;
    }

}