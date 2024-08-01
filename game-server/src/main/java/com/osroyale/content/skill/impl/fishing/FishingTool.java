package com.osroyale.content.skill.impl.fishing;

import java.util.HashMap;
import java.util.Map;

public enum FishingTool {
    SMALL_NET(303, 621, new short[]{317, 3150, 321, 5004, 7994}),
    BIG_NET(305, 621, new short[]{353, 341, 363}),
    CRAYFISH_CAGE(13431, 619, new short[]{13435}),
    FISHING_ROD(307, 622, new short[]{327, 345, 349, 3379, 5001, 2148}),
    FLYFISHING_ROD(309, 622, new short[]{335, 331}),
    KARAMBWAN_POT(3157, -1, new short[]{3142}),
    HARPOON(311, 618, new short[]{359, 371}),
    DRAGON_HARPOON(21028, 618, new short[]{359, 371}),
    DRAGON_HARPOON2(21029, 618, new short[]{359, 371}),
    LOBSTER_POT(301, 619, new short[]{377});

    private int toolId;
    private int animation;
    private short[] outcomes;

    private static Map<Integer, FishingTool> tools = new HashMap<>();

    public static void declare() {
        for (FishingTool tool : values())
            tools.put(tool.getToolId(), tool);
    }

    FishingTool(int toolId, int animation, short[] outcomes) {
        this.toolId = toolId;
        this.outcomes = outcomes;
        this.animation = animation;
    }

    public int getAnimationId() {
        return animation;
    }

    public short[] getOutcomes() {
        return outcomes;
    }

    public int getToolId() {
        return toolId;
    }

    public static FishingTool forId(int id) {
        return tools.get(id);
    }

}
