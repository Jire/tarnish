package com.osroyale.content.skill.impl.hunter.birdhouse;

public enum BirdhouseSeedData {
    //Hop seed
    BARLEY(5305, 10),
    HAMMERSTONE(5307, 10),
    ASGARNIAN(5308, 10),
    JUTE(5306, 10),
    YANILLIAN(5309, 10),
    KRANDORIAN(5310, 10),
    WILDBLOOD(5311, 5),
    //Herb seeds
    GUAM(5291, 10),
    MARRENTILL(5292, 10),
    TARROMIN(5293, 10),
    HARRALANDER(5294, 10),
    RANARR(5295, 5),
    TOADFLAX(5296, 5),
    IRIT(5297, 5),
    AVANTOE(5298, 5),
    KWUARM(5299, 5),
    SNAPDRAGON(5300, 5),
    CADANTINE(5301, 5),
    LANTADYME(5302, 5),
    DWARF_WEED(5303, 5),
    TORSTOL(5304, 5),
    //Flower
    MARIGOLD(5096, 10),
    ROSEMARY(5097, 10),
    NASTURTIUM(5098, 10),
    WOAD(5099, 10),
    LIMPWURT(5100, 10),
    WHITE_LILY(22887, 10),
    //ALLOTMENT
    POTATO(5318, 10),
    ONION(5319, 10),
    CABBAGE(5324, 10),
    TOMATO(5322, 10),
    SWEETCORN(5320, 10),
    STRAWBERRY(5323, 10),
    WATERMELON(5321, 10),
    SNAPE_GRASS(22879, 10),
    //Bush
    REDBERRY(5101, 10),
    POISON_IVY(5106, 10),
    CADAVABERRY(5102, 10),
    DWELLBERRY(5103, 10),
    JANGERBERRY(5104, 10),
    WHITEBERRY(5105, 10),
    ;

    public int seedId, seedAmount;

    BirdhouseSeedData(int seedId, int seedAmount) {
        this.seedId = seedId;
        this.seedAmount = seedAmount;
    }

    public static BirdhouseSeedData forId(int seedId) {
        for(BirdhouseSeedData birdhouseSeedData : values())
            if(birdhouseSeedData.seedId == seedId)
                return birdhouseSeedData;

        return null;
    }
}
