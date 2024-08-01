package com.osroyale.net.codec;

/**
 * Represents the order in which bytes are written.
 * 
 * @author nshusa
 */
public enum ByteOrder {

    /**
     * Represents Little-endian
     */
    LE,

    /**
     * Represents Big-endian
     */
    BE,

    /**
     * Represents Middle-endian
     */
    ME,

    /**
     * Represents Inverse-middle-endian
     */
    IME,

}