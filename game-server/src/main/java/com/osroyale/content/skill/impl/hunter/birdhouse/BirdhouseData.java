package com.osroyale.content.skill.impl.hunter.birdhouse;

public enum BirdhouseData {
    BIRDHOUSE(100, new int[] { 5, 15 }, new int[] { 5, 280 }, new int[] { 30554, 30555 }, 1511, 21512),
    OAK_BIRDHOUSE(125, new int[] { 15, 20 }, new int[] { 14, 420 }, new int[] { 30557, 30558 }, 1521, 21515),
    WILLOW_BIRDHOUSE(128, new int[] { 25, 25 }, new int[] { 24, 560 }, new int[] { 30560, 30561 }, 1519, 21518),
    TEAK_BIRDHOUSE(130, new int[] { 35, 30 }, new int[] { 34, 700 }, new int[] { 30563, 30564 }, 6333, 21521),
    MAPLE_BIRDHOUSE(140, new int[] { 45, 35 }, new int[] { 44, 820 }, new int[] { 31828, 31829 }, 1517, 22192),
    MAHOGANY_BIRDHOUSE(150, new int[] { 50, 40 }, new int[] { 49, 960 }, new int[] { 31831, 31832 }, 6332, 22195),
    YEW_BIRDHOUSE(160, new int[] { 60, 45 }, new int[] { 59, 1020 }, new int[] { 31834, 31835 }, 1515, 22198),
    MAGIC_BIRDHOUSE(170, new int[] { 75, 50 }, new int[] { 74, 1140 }, new int[] { 31837, 31838 }, 1513, 22201),
    REDWOOD_BIRDHOUSE(175, new int[] { 90, 55 }, new int[] { 89, 1200 }, new int[] { 31840, 31841 }, 19669, 22204),
    ;

    public int[] craftingData, hunterData, objectData;
    public int succesRates, logId, birdHouseId;

    BirdhouseData(int succesRates, int[] craftingData, int[] hunterData, int[] objectData, int logId, int birdHouseId) {
        this.succesRates = succesRates;
        this.craftingData = craftingData;
        this.hunterData = hunterData;
        this.objectData = objectData;
        this.logId = logId;
        this.birdHouseId = birdHouseId;
    }

    public static BirdhouseData forLog(int logId) {
        for(BirdhouseData birdHouseData : values())
            if(birdHouseData.logId == logId)
                return birdHouseData;

        return null;
    }

    public static BirdhouseData forBirdhouse(int birdHouseId) {
        for(BirdhouseData birdHouseData : values())
            if(birdHouseData.birdHouseId == birdHouseId)
                return birdHouseData;

        return null;
    }
}