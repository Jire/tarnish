package com.osroyale.net.packet;

/**
 * Represents a type of packet
 * 
 * @author nshusa
 */
public enum PacketType {

    EMPTY,

    /**
     * A fixed size packet where the size never changes.
     */
    FIXED,

    /**
     * A variable packet where the size is indicated by a byte.
     */
    VAR_BYTE,

    /**
     * A variable packet where the size is indicated by a short.
     */
    VAR_SHORT

}
