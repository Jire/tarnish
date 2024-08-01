package com.osroyale.content.achievement;

/**
 * The types of achievement types.
 *
 * @author Daniel
 */
public enum AchievementType {
    PVP(0xE83838, "PvP"),
    PVM(0xDB8E3B, "PvM"),
    SKILLING(0x5CC947, "Skilling"),
    MINIGAME(0x4285E3, "Minigame"),
    MISCELLANEOUS(0xAE47ED, "Misc.");

    private final String title;
    private final int color;

    AchievementType(int color, String title) {
        this.title = title;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public int getColor() {
        return color;
    }
}
