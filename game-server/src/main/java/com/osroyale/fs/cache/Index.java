package com.osroyale.fs.cache;

import com.osroyale.fs.util.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * Represents an index within some {@link Cache}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Index {

    /** The length of the index. */
    private final int length;

    /** The id of the index. */
    private final int id;

    /**
     * Constructs a new {@link Index} with the expected length and id. This
     * constructor is marked {@code private} and should not be modified to be
     * invoked directly, use {@link Index#decode(ByteBuffer)} instead.
     *
     * @param length The length of the index.
     * @param id     The id of the index.
     */
    private Index(int length, int id) {
        this.length = length;
        this.id = id;
    }

    /**
     * Decodes an {@link Index} from the specified {@link ByteBuffer}.
     *
     * @param buffer The {@link ByteBuffer} to get the index from.
     * @return The decoded index.
     */
    public static Index decode(ByteBuffer buffer) {
        int length = ByteBufferUtil.getMedium(buffer);
        int id = ByteBufferUtil.getMedium(buffer);
        return new Index(length, id);
    }

    /** Returns the id of this index. */
    public int getId() {
        return id;
    }

    /** Returns the length of this index. */
    public int getLength() {
        return length;
    }

    /** Tests whether or not this index is valid. */
    public boolean check() {
        return length > 0;
    }

}