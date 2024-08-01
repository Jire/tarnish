package com.osroyale.content.skill.impl.farming;

/**
 * The farming state of a patch.
 *
 * @author Michael | Chex
 */
public enum FarmingState {

    /** The patch is empty and ready to be used. */
    NORMAL,

    /** The patch is watered. */
    WATERED,

    /** The patch is diseased. */
    DISEASED,

    /** The patch is dead. */
    DEAD

}
