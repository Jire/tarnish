package com.osroyale.content.skill.impl.farming.plants;

import java.util.HashMap;
import java.util.Map;

public enum Flower implements Plant {
    MARIGOLD(5096, 6010, 2, 20,  8.5, 47, 0x08, 0x0c, InspectMessage.MARIGOLD),
    ROSEMARY(5097, 6014, 11, 20, 12, 66.5, 0x0d, 0x11, InspectMessage.ROSEMARY),
    NASTURTIUM(5098, 6012, 24, 20,  19.5, 111, 0x12, 0x16, InspectMessage.NASTURTIUM),
    WOAD(5099, 1793, 25, 20,  20.5, 115.5, 0x17, 0x1b, InspectMessage.WOAD),
    LIMPWURT(5100, 225, 26, 25,  8.5, 120, 0x1c, 0x20, InspectMessage.LIMPWURT);

    private final int seedId;
    private final int harvestId;
    private final int levelRequired;
    private final int growthTime;
    private final double plantingXp;
    private final double harvestXp;
    private final int startingState;
    private final int endingState;
    private final InspectMessage inspect;

    private static final Map<Integer, Flower> FLOWERS = new HashMap<>();

    static {
        for (Flower data : Flower.values()) {
            FLOWERS.put(data.seedId, data);
        }
    }

    Flower(int seedId, int harvestId, int levelRequired, int growthTime,  double plantingXp, double harvestXp, int startingState, int endingState, InspectMessage inspect) {
        this.seedId = seedId;
        this.harvestId = harvestId;
        this.levelRequired = levelRequired;
        this.growthTime = growthTime;
        this.plantingXp = plantingXp;
        this.harvestXp = harvestXp;
        this.startingState = startingState;
        this.endingState = endingState;
        this.inspect = inspect;
    }

    public static Flower forId(int seedId) {
        return FLOWERS.get(seedId);
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
    public int[] getPaymentToWatch() {
        return new int[0];
    }

    @Override
    public int getFlowerProtect() {
        return -1;
    }

    @Override
    public String getProductType() {
        return "flower";
    }

    @Override
    public int getSeedAmount() {
        return 1;
    }

    @Override
    public String[][] getInspectMessages() {
        return inspect.getMessages();
    }

}
