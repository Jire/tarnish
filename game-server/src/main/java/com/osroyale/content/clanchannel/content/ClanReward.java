package com.osroyale.content.clanchannel.content;

import java.util.Optional;

/**
 * Holds all the clan reward data.
 *
 * @author Daniel
 */
public enum ClanReward {
    DOUBLE_SKILL_EXPERIENCE_1HOUR("Double skill experience (1hr)", 11188, 60, -1, -1);

    /** The name of the reward. */
    public final String name;

    /** The item identification of the reward. */
    public final int item;

    /** The duration of the reward. */
    public final int duration;

    /** The experience of the reward. */
    public final int experience;

    /** The drop modifier of the reward. */
    public final double dropModifier;

    /** Makes a new <code>ClanReward<code> */
    ClanReward(String name, int item, int duration, int experience, double dropModifier) {
        this.name = name;
        this.item = item;
        this.duration = duration;
        this.experience = experience;
        this.dropModifier = dropModifier;
    }

    /** Handles getting the clan reward based on the item identification. */
    public static Optional<ClanReward> forId(int item) {
        for (ClanReward reward : values())
            if (reward.item == item)
                return Optional.of(reward);
        return Optional.empty();
    }
}
