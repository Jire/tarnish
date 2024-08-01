package com.osroyale.content.skill.impl.fishing;

import java.util.HashMap;
import java.util.Map;

import static com.osroyale.util.Items.*;

public enum Fishable {
    SHRIMP(317, SMALL_FISHING_NET, 1, 10.0D, -1),
    CRAYFISH(13435, 13431, 1, 10.0D, -1),
    KARAMBWANJI(3150, SMALL_FISHING_NET, 5, 5.0D, -1),
    SARDINE(327, FISHING_ROD, 5, 20.0D, FISHING_BAIT),
    HERRING(345, FISHING_ROD, 10, 30.0D, FISHING_BAIT),
    ANCHOVIES(321, SMALL_FISHING_NET, 15, 40.0D, -1),
    MACKEREL(353, BIG_FISHING_NET, 16, 20.0D, -1),
    TROUT(335, FLY_FISHING_ROD, 20, 50.0D, 314),
    COD(341, BIG_FISHING_NET, 23, 45.0D, -1),
    PIKE(349, FISHING_ROD, 25, 60.0D, FISHING_BAIT),
    SLIMY_EEL(3379, FISHING_ROD, 28, 65.0D, FISHING_BAIT),
    SALMON(331, FLY_FISHING_ROD, 30, 70.0D, 314),
    FROG_SPAWN(5004, SMALL_FISHING_NET, 33, 75.0D, -1),
    TUNA(359, HARPOON, 35, 80.0D, -1),
    TUNA2(359, DRAGON_HARPOON, 35, 80.0D, -1),

    CAVE_EEL(5001, FISHING_ROD, 38, 80.0D, FISHING_BAIT),
    LOBSTER(377, LOBSTER_POT, 40, 90.0D, -1),
    BASS(363, BIG_FISHING_NET, 46, 100.0D, -1),
    SWORD_FISH(371, HARPOON, 50, 100.0D, -1),
    SWORD_FISH2(371, DRAGON_HARPOON, 50, 100.0D, -1),

    LAVA_EEL(2148, FISHING_ROD, 53, 30.0D, FISHING_BAIT),
    MONK_FISH(7944, SMALL_FISHING_NET, 62, 110.0D, -1),
    KARAMBWAN(3142, 3157, 65, 100.0D, -1),
    SHARK(383, HARPOON, 76, 125.0D, -1),
    SEA_TURTLE(395, -1, 79, 38.0D, -1),
    MANTA_RAY(389, BIG_FISHING_NET, 81, 155.0D, -1),
    DARK_CRAB(11934, 301, 85, 205.0D, DARK_FISHING_BAIT);

    public static void declare() {
        for (Fishable fishes : values())
            fish.put((int) fishes.getRawFishId(), fishes);
    }

    private short rawFishId;
    private short toolId;
    private short levelRequired;
    private short baitRequired;
    private double experienceGain;
    private static Map<Integer, Fishable> fish = new HashMap<>();

    public static Fishable forId(int rawFishId) {
        return fish.get(rawFishId);
    }

    Fishable(int rawFishId, int toolId, int levelRequired, double experienceGain, int baitRequired) {
        this.rawFishId = ((short) rawFishId);
        this.toolId = ((short) toolId);
        this.levelRequired = ((short) levelRequired);
        this.experienceGain = experienceGain;
        this.baitRequired = ((short) baitRequired);
    }

    public short getBaitRequired() {
        return baitRequired;
    }

    public double getExperience() {
        return experienceGain;
    }

    public int getRawFishId() {
        return rawFishId;
    }

    public short getRequiredLevel() {
        return levelRequired;
    }

    public short getToolId() {
        return toolId;
    }
}