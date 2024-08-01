package com.osroyale.content.skill.impl.farming.plants;

import java.util.HashMap;
import java.util.Map;

public enum Allotment implements Plant {
    POTATO(5318, 1942, 5096, 3, 1, new int[] {6032, 2}, 40, 8, 9.5, 6, 12, InspectMessage.POTATOES),
    ONION(5319, 1957, 5096, 3, 5, new int[] {5438, 1}, 40, 9.5, 10.5, 13, 19, InspectMessage.ONIONS),
    CABBAGE(5324, 1965, 5097, 3, 7, new int[] {5458, 1}, 40, 10, 11.5, 20, 26, InspectMessage.CABBAGES),
    TOMATO(5322, 1982, 5096, 3, 12, new int[] {5478, 2}, 40,  12.5, 14, 27, 33, InspectMessage.TOMATOES),
    SWEETCORN(5320, 5986, 6059, 3, 20, new int[] {5931, 10}, 40,  17, 19, 34, 42, InspectMessage.SWEETCORNS),
    STRAWBERRY(5323, 5504, -1, 3, 31, new int[] {5386, 1}, 40, 26, 29, 43, 49, InspectMessage.STRAWBERRIES),
    WATERMELON(5321, 5982, 5098, 3, 47, new int[] {5970, 10}, 40, 48.5, 54.5, 52, 60, InspectMessage.WATERMELONS);

    private final int seedId;
    private final int harvestId;
    private final int flowerProtect;
    private final int seedAmount;
    private final int levelRequired;
    private final int[] paymentToWatch;
    private final int growthTime;
    private final double plantingXp;
    private final double harvestXp;
    private final int startingState;
    private final int endingState;
    private final InspectMessage inspect;

    private static final Map<Integer, Allotment> ALLOTMENTS = new HashMap<>();

    static {
        for (Allotment data : Allotment.values()) {
            ALLOTMENTS.put(data.seedId, data);
        }
    }

    Allotment(int seedId, int harvestId, int flowerProtect, int seedAmount, int levelRequired, int[] paymentToWatch, int growthTime,  double plantingXp, double harvestXp, int startingState, int endingState, InspectMessage inspect) {
        this.seedId = seedId;
        this.harvestId = harvestId;
        this.flowerProtect = flowerProtect;
        this.seedAmount = seedAmount;
        this.levelRequired = levelRequired;
        this.paymentToWatch = paymentToWatch;
        this.growthTime = growthTime;
        this.plantingXp = plantingXp;
        this.harvestXp = harvestXp;
        this.startingState = startingState;
        this.endingState = endingState;
        this.inspect = inspect;
    }

    public static Allotment forId(int seedId) {
        return ALLOTMENTS.get(seedId);
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
    public int getFlowerProtect() {
        return flowerProtect;
    }

    @Override
    public int getSeedAmount() {
        return seedAmount;
    }

    @Override
    public int getLevelRequired() {
        return levelRequired;
    }

    @Override
    public int[] getPaymentToWatch() {
        return paymentToWatch;
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
    public String getProductType() {
        return name().toLowerCase();
    }

    @Override
    public String[][] getInspectMessages() {
        return inspect.getMessages();
    }

}
