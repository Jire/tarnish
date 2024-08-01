package com.osroyale.content.skill.impl.farming;

/**
 * The farming stage of a patch.
 *
 * @author Michael | Chex
 */
public enum FarmingStage {

    /** The patch is full of weeds. */
    FULL_WEEDS,

    /** The patch needs to be weeded two more times. */
    TWO_THIRDS_WEEDS,

    /** The patch needs to be weeded one more time. */
    ONE_THIRD_WEEDS,

    /** The patch is empty of weeds. */
    EMPTY;

    /**
     * Gets the previous farming stage.
     *
     * @return the previous farming stage, or the current stage if no previous stage.
     */
    public FarmingStage getPreviousStage() {
        if (ordinal() == 0)
            return this;
        return values()[ordinal() - 1];
    }

    /**
     * Gets the next farming stage.
     *
     * @return the next farming stage, or the current stage if no next stage.
     */
    public FarmingStage getNextStage() {
        if (ordinal() + 1 == values().length)
            return this;
        return values()[ordinal() + 1];
    }

}
