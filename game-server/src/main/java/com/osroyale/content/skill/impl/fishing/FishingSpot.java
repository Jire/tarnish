package com.osroyale.content.skill.impl.fishing;

import java.util.HashMap;
import java.util.Map;

public enum FishingSpot {
    SMALL_NET_OR_BAIT(1518, new Fishable[]{Fishable.SHRIMP, Fishable.ANCHOVIES}, new Fishable[]{Fishable.SARDINE, Fishable.HERRING, Fishable.PIKE}),
    LURE_OR_BAIT(1526, new Fishable[]{Fishable.TROUT, Fishable.SALMON}, new Fishable[]{Fishable.SARDINE, Fishable.HERRING, Fishable.PIKE}),
    CAGE_OR_HARPOON(1519, new Fishable[]{Fishable.LOBSTER}, new Fishable[]{Fishable.TUNA, Fishable.SWORD_FISH}),
    LARGE_NET_OR_HARPOON(1520, new Fishable[]{Fishable.MACKEREL, Fishable.COD, Fishable.BASS}, new Fishable[]{Fishable.SHARK}),
    HARPOON_OR_SMALL_NET(1534, new Fishable[]{Fishable.MONK_FISH}, new Fishable[]{Fishable.MONK_FISH}),
    MANTA_RAY(1521, new Fishable[]{Fishable.MANTA_RAY}, new Fishable[]{Fishable.MANTA_RAY}),
    DARK_CRAB(1536, new Fishable[]{Fishable.DARK_CRAB}, new Fishable[]{Fishable.DARK_CRAB});

    private int id;
    private Fishable[] firstOption;
    private Fishable[] secondOption;
    private static Map<Integer, FishingSpot> fishingSpots = new HashMap<>();

    public static void declare() {
        for (FishingSpot spots : values())
            fishingSpots.put(spots.getId(), spots);
    }

    public static FishingSpot forId(int id) {
        return fishingSpots.get(id);
    }

    FishingSpot(int id, Fishable[] firstOption, Fishable[] secondOption) {
        this.id = id;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
    }

    public int getId() {
        return id;
    }

    public Fishable[] getFirstOption() {
        return firstOption;
    }

    public Fishable[] getSecondOption() {
        return secondOption;
    }
}
