package com.osroyale.content.clanchannel;

/**
 * The different clan types.
 *
 * @author Daniel
 */
public enum ClanType {
    /** Clans that are focused on PVP. */
    PVP("PvP", 8),

    /** Clans that are focused on PVM. */
    PVM("PvM", 3),

    /** Clans that are focused on skilling. */
    SKILLING("Skilling", 5),

    /** Clans that are focused on socialism. */
    SOCIAL("Social", 20),

    /** Clans that are focused on iron man accounts. */
    IRON_MAN("Ironman", 18);

    private final String name;
    private final int icon;

    ClanType(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIconValue() {
        return icon;
    }

    public String getIcon() {
        return "<icon=" + getIconValue() + ">";
    }

}
