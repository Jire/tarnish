package com.osroyale.content.skill.impl.farming.plants;

import java.util.HashMap;
import java.util.Map;

public enum Herb implements Plant {
    GUAM(5291, 199, 9, 60, 11, 12.5, 4, 8),
    MARRENTILL(5292, 201, 14, 60, 13.5, 15, 11, 15),
    TARROMIN(5293, 203, 19, 60,  16, 18, 18, 22),
    HARRALANDER(5294, 205, 26, 60,  21.5, 24, 25, 29),
    GOUT_TUBER(6311, 3261, 29, 60, 105, 45, 192, 196),
    RANARR(5295, 207, 32, 60,  27, 30.5, 32, 36),
    TOADFLAX(5296, 3049, 38, 60, 34, 38.5, 39, 43),
    IRIT(5297, 209, 44, 60, 43, 48.5, 46, 50),
    AVANTOE(5298, 211, 50, 60,  54.5, 61.5, 53, 57),
    KUARM(5299, 213, 56, 60, 69, 78, 0x44, 72),
    SNAPDRAGON(5300, 3051, 62, 60,  87.5, 98.5, 75, 79),
    CADANTINE(5301, 215, 67, 60,  106.5, 120, 82, 86),
    LANTADYME(5302, 2485, 73, 60,  134.5, 151.5, 89, 93),
    DWARF(5303, 217, 79, 60, 170.5, 192, 96, 100),
    TORSOL(5304, 219, 85, 60,  199.5, 224.5, 103, 107);

    private final int seedId;
    private final int harvestId;
    private final int levelRequired;
    private final int growthTime;
    private final double plantingXp;
    private final double harvestXp;
    private final int startingState;
    private final int endingState;

    private static final Map<Integer, Herb> HERBS = new HashMap<>();

    static {
        for (Herb data : Herb.values()) {
            HERBS.put(data.seedId, data);
        }
    }

    private static final String[][] MESSAGES = new String[][] {
        {"The seed has only just been planted."},
        {"The herb is now ankle height."},
        {"The herb is now knee height."},
        {"The herb is now mid-thigh height."},
        {"The herb is fully grown and ready to harvest."}
    };

    Herb(int seedId, int harvestId, int levelRequired, int growthTime,  double plantingXp, double harvestXp, int startingState, int endingState) {
        this.seedId = seedId;
        this.harvestId = harvestId;
        this.levelRequired = levelRequired;
        this.growthTime = growthTime;
        this.plantingXp = plantingXp;
        this.harvestXp = harvestXp;
        this.startingState = startingState;
        this.endingState = endingState;
    }

    public static Herb forId(int seedId) {
        return HERBS.get(seedId);
    }

    @Override
    public int getSeedId() {
        return seedId;
    }

    @Override
    public int getHarvestId() {
        return harvestId;
    }

    @Override
    public int getLevelRequired() {
        return levelRequired;
    }

    @Override
    public int getGrowthTime() {
        return growthTime;
    }



    @Override
    public double getPlantingXp() {
        return plantingXp;
    }

    @Override
    public double getHarvestXp() {
        return harvestXp;
    }

    @Override
    public int getStartingState() {
        return startingState;
    }

    @Override
    public int getEndingState() {
        return endingState;
    }

    @Override
    public int getSeedAmount() {
        return 1;
    }

    @Override
    public String getProductType() {
        return "herb";
    }

    @Override
    public int getFlowerProtect() {
        return -1;
    }

    @Override
    public int[] getPaymentToWatch() {
        return new int[0];
    }

    @Override
    public String[][] getInspectMessages() {
        return MESSAGES;
    }

}
