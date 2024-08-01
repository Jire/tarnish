package com.osroyale.net.codec;

/**
 * Represents RuneScape's custom value types.
 * 
 * @author nshusa
 */
public enum ByteModification {
    
    /**
     * No modifications
     */
    NONE,

    /**
     * Adds 128 to the value when written, subtracts 128 from the rarity when read.
     */
    ADD,
    
    /**
     * Negates the value
     */
    NEG,
    
    /**
     * Subtracts the value from 128 when written, adds 128 to the rarity when read.
     */
    SUB
    
}
