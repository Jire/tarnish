package com.osroyale.content.skill.impl.prayer;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds the Bone data.
 */
public enum BoneData {
    NORMAL_BONES(526, 527, 4.5D, 1),
    WOLF_BONES(2859, 2860, 4.5D, 1),
    BAT_BONES(530, 531, 4.5D, 1),
    BIG_BONES(532, 533, 15.0D, 2),
    BABYDRAGON_BONES(534, 535, 30.0D, 3),
    DRAGON_BONES(536, 537, 72.0D, 4),
    OURG_BONES(4834, 4835, 140.0D, 4),
    LONG_BONE(10976, 10977, 100.0D, 4),
    SKELETAL_WYVERN_BONES(6812, 6818, 72.0D, 4),
    LAVA_DRAGON_BONES(11943, 11944, 85.0D, 4),
    DAGG_BONES(6729, 6730, 125.0D, 4),
    SUPERIOR_DRAGON_BONES(22124, 22125, 150.0D, 5);

    /** The id of the bone */
    private final int id;

    private final int id2;
    /** The experience of the bone */
    private final double experience;

    /** The amount of prayer points rewarded for bone amulet. */
    private final int boneAmulet;

    /** The bone data */
    BoneData(int id, int noted, double experience, int boneAmulet) {
        this.id = id;
        this.id2 = noted;
        this.experience = experience;
        this.boneAmulet = boneAmulet;
    }

    /** Gets the id of the bone */
    public int getId() {
        return id;
    }
    public int getId2() {
        return id2;
    }

    /** Gets the experience of the bone */
    public double getExperience() {
        return experience;
    }

    public int getBoneAmulet() {
        return boneAmulet;
    }

    /** Gets the bone data based on the item */
    public static Optional<BoneData> forId(int id) {
        return Arrays.stream(values()).filter(a -> a.id == id).findAny();
    }
}