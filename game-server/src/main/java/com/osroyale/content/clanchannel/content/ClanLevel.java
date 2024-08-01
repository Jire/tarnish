package com.osroyale.content.clanchannel.content;

/**
 * Handles the clan levels (based off the total experience earned).
 *
 * @author Daniel
 */
public enum ClanLevel {
    BRONZE(0, 0, "553E21"),
    IRON(50_000, 5, "484242"),
    STEEL(150_000, 15, "867B7B"),
    BLACK(300_000, 30, "1E1C1C"),
    MITHRIL(750_000, 50, "3D48BF"),
    ADAMANT(1_500_000, 75, "3B4E3B"),
    RUNE(3_000_000, 115, "4A6775"),
    DRAGON(6_000_000, 165, "782924"),
    BARROW(10_000_000, 250, "4F614B"),
    GILDED(18_000_000, 500, "EBC41A"),
    GODLY(30_000_000, 800, "E0D290"),
    THIRD_AGE(50_000_000, 1250, "59b9ff"),
    RARE(100_000_000, 2500, "D468F2");

    /** The experience of the level. */
    private final long experience;

    /** The clan points rewarded. */
    private final int points;

    /** The clan level color. */
    private final String color;

    /** The clan level. */
    ClanLevel(long experience, int points, String color) {
        this.experience = experience;
        this.points = points;
        this.color = color;
    }

    /** Gets the experience of the clan level. */
    public long getExperience() {
        return experience;
    }

    /** Gets the points rewarded for leveling up. */
    public int getPoints() {
        return points;
    }

    /** Gets the clan level color. */
    public String getColor() {
        return color;
    }

    /** Gets a clan level based off the experience. */
    public static ClanLevel getLevel(double experience) {
        ClanLevel level = BRONZE;
        for (ClanLevel levels : values()) {
            if (experience >= levels.getExperience())
                level = levels;
        }
        return level;
    }
}
