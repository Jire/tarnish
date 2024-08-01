package com.osroyale.content.skill.impl.farming.plants;

public interface Plant {

    int getSeedId();

    int getSeedAmount();

    int getLevelRequired();


    int getGrowthTime();

    int getStartingState();

    int getEndingState();

    int getFlowerProtect();

    int getHarvestId();

    double getHarvestXp();

    double getPlantingXp();

    int[] getPaymentToWatch();

    String getProductType();

    String[][] getInspectMessages();

}
