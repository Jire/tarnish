package com.osroyale.content.activity.impl.recipefordisaster;

/**
 * Holds the recipe for disaster activity data.
 *
 * @author Daniel
 */
public enum RecipeForDisasterData {
    AGRITH_NA_NA(4880, 1),
    FLAMBEED(4881, 3),
    KARAMEL(4882, 6),
    DESSOURT(4883, 8),
    GELATINNOTH_MOTHER(4884, 9),
    CULINAROMANCER(4878, 10);

    public final int npc;
    public final int gloves;

    RecipeForDisasterData(int npc, int gloves) {
        this.npc = npc;
        this.gloves = gloves;
    }

    public RecipeForDisasterData getNext() {
        int index = ordinal() + 1;
        if (index >= values().length)
            return null;
        return values()[index];
    }
}
