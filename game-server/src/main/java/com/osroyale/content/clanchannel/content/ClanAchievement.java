package com.osroyale.content.clanchannel.content;

import com.osroyale.util.Difficulty;

import java.util.Optional;

/**
 * Handles the clan achievements.
 *
 * @author Daniel.
 */
public enum ClanAchievement {
    /* Easy */
    CLAN_MEMBERS_I("Have 10 members in your clan", 10, Difficulty.EASY),
    TASKS_I("Complete 10 clan tasks", 10, Difficulty.EASY),
    PLAYER_KILLER_I("Kill 25 wilderness players", 25, Difficulty.EASY),

    /* Medium */
    CLAN_MEMBERS_II("Have 50 members in your clan", 50, Difficulty.MEDIUM),
    TASKS_II("Complete 100 clan tasks", 100, Difficulty.MEDIUM),
    PLAYER_KILLER_II("Kill 150 wilderness players", 150, Difficulty.MEDIUM),

    /* Hard */
    CLAN_MEMBERS_III("Have 150 members in your clan", 150, Difficulty.HARD),
    TASKS_III("Complete 500 clan tasks", 500, Difficulty.HARD),
    PLAYER_KILLER_III("Kill 750 wilderness players", 750, Difficulty.HARD),
    ;

    /** The details of the achievement. */
    public final String details;

    /** The amount required of the achievement. */
    public final int amount;

    /** The difficulty of the achievement. */
    public final Difficulty difficulty;

    /** Constructs a new <code>ClanAchievement</code>. */
    ClanAchievement(String details, int amount, Difficulty difficulty) {
        this.details = details;
        this.amount = amount;
        this.difficulty = difficulty;
    }

    /** Gets the reward points for completing an achievement. */
    public int getPoints() {
        switch (difficulty) {
            case EASY:
                return 3;
            case MEDIUM:
                return 5;
            case HARD:
                return 8;
        }
        return 0;
    }

    /** Gets the reward experience for completing an achivement. */
    public double getExperience() {
        switch (difficulty) {
            case EASY:
                return 2500;
            case MEDIUM:
                return 5000;
            case HARD:
                return 10000;
        }
        return 0;
    }

    /** Gets the clan achivement based on the ordinal. */
    public static Optional<ClanAchievement> forOrdinal(int ordinal) {
        for (ClanAchievement achievement : values()) {
            if (achievement.ordinal() == ordinal)
                return Optional.of(achievement);
        }
        return Optional.empty();
    }
}
