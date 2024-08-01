package com.osroyale.content.activity;


import java.util.LinkedList;
import java.util.List;

/**
 * Holds all activity types that are timed.
 *
 * @author Daniel
 */
public enum ActivityType {
    FIGHT_CAVES(true),
    KOLODION_ARENA(true),
    INFERNO(true),
    RECIPE_FOR_DISASTER(true),
    BARROWS(true),
    ZULRAH(true),
    KRAKEN(true),
    DUEL_ARENA(false),
    TUTORIAL(false),
    PEST_CONTROL(false),
    VORKATH(true),
    JAIL(false),
    WARRIOR_GUILD(false),
    CERBERUS(true),
    GODWARS(false),
    RANDOM_EVENT(false);

    final boolean record;
    private static final List<ActivityType> RECORDABLE = new LinkedList<>();

    static {
        for (ActivityType activity : values()) {
            if (activity.record)
                RECORDABLE.add(activity);
        }
    }

    ActivityType(boolean record) {
        this.record = record;
    }

    public static List<ActivityType> getRecordable() {
        return new LinkedList<>(RECORDABLE);
    }

    public static ActivityType getOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal > RECORDABLE.size())
            return null;
        return RECORDABLE.get(ordinal);
    }

    public static ActivityType getFirst() {
        return getOrdinal(0);
    }
}
