package com.osroyale.content.mysterybox;

/**
 * Created by Daniel on 2018-02-03.
 */
public enum MysteryRarity {
    COMMON(.10),
    UNCOMMON(.08),
    RARE(.5),
    EXOTIC(.2);

    public final double chance;

    MysteryRarity(double chance) {
        this.chance = chance;
    }
}
