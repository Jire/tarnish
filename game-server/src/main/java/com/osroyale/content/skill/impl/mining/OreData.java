package com.osroyale.content.skill.impl.mining;

/**
 * Holds all the ore data.
 *
 * @author Daniel
 */
public enum OreData {
    CLAY(434, 1, 5, 11391, 15, 741_600, 5, 0.3, new int[]{11363, 11362, 7487}, -1),
    COPPER(436, 1, 17.5, 11391, 15, 741_600, 5, 0.5, new int[]{10943, 11161, 7453}, 3.6),
    TIN(438, 1, 17.5, 11391, 15, 741_600, 5, 0.5, new int[]{11360,11361, 7486}, 3.6),
    IRON(440, 15, 35, 11391, 20, 741_600, 8, 0.2, new int[]{11365,11364, 7455}, 6.25),
    SILVER(442, 20, 40, 11391, 25, 741_600, 8, 0.2, new int[]{11368,11369, 7490}, 7.8),
    COAL(453, 30, 50, 11391, 25, 290_640, 9, 0.2, new int[]{11367, 11366, 7489}, -1),
    GOLD(444, 40, 65, 11391, 30, 296_640, 7, 0.15, new int[]{11371, 11370, 7458}, 9),
    MITHRIL(447, 55, 80, 11391, 50, 148_320, 9, 0.05, new int[]{11373, 11372, 7459}, 12),
    ADAMANTITE(449, 70, 95, 11391, 75, 59_328, 11, 0.03, new int[]{11375, 11374, 7460}, 18.75),
    RUNITE(451, 85, 125, 11391, 100, 42_337, 13, 0.02, new int[]{11377, 11376}, 25),
    RUNE_ESSENCE(7936, 1, 25, -1, 20, 326_780, Integer.MAX_VALUE, 0.1, new int[]{34773,7471}, -1),
    GEM_ROCK(-1, 40, 85, 11391, 75, 326_780, 2, 0.2, new int[] {11381, 11380, 9030}, -1),
    AMETHYST(21347, 92, 240, 11393, 75, 326_780, 10, 0.2, new int[] {11389, 11388}, -1),
    GEM_ROCK_I(-1, 40, 65, 11391, 135, 326_780, 2, 0.3, new int[] {11380, 11381, 7464}, -1),
    ASHES(21622, 22, 125, 30986, 100, 42_337, 13, 0.02, new int[]{30985}, -1),

    //30985 ash pile
    ;

    /** The ore this node contains. */
    public final int ore;

    /** The minimum level to mine this node. */
    public final int level;

    /** The experience. */
    public final double experience;

    /** The node replacement. */
    public final int replacement;

    /** The node respawn timer. */
    public final int respawn;

    /** The pet chance. */
    public final int pet;

    /** The amount of ores that this ore can give. */
    public final int oreCount;

    /** The success rate for the ore data. */
    public final double success;

    /** The object identification of this node. */
    public final int[] objects;

    public double infernalExperience;

    /** Creates the node. */
    OreData(int ore, int level, double experience, int replacement, int respawn, int pet, int oreCount, double success, int[] objects, double infernalExperience) {
        this.objects = objects;
        this.level = level;
        this.experience = experience;
        this.replacement = replacement;
        this.respawn = respawn;
        this.pet = pet;
        this.oreCount = oreCount;
        this.success = success;
        this.ore = ore;
        this.infernalExperience = infernalExperience;
    }

    /** Gets the ore data. */
    public static OreData forId(int id) {
        for (OreData data : OreData.values())
            for (int object : data.objects)
                if (object == id)
                    return data;
        return null;
    }
}
