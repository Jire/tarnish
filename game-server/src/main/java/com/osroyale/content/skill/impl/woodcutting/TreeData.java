package com.osroyale.content.skill.impl.woodcutting;

/**
 * Holds all the data for trees.
 *
 * @author Daniel
 */
public enum TreeData {
    NORMAL_TREE(1, 1511, 1342, 15, 25.0D, 317_647, 2, 1.0, new int[]{1276, 1278, 1279}),
    DYING_TREE(1, 1511, 3649, 15, 25.0D, 317_647, 2, 1.0, new int[]{3648}),
    DEAD_TREE(1, 1511, 1341, 15, 25.0D, 317_647, 2, 1.0, new int[]{1284}),
    DEAD_TREE1(1, 1511, 1351, 15, 25.0D, 317_647, 2, 1.0, new int[]{1286}),
    DEAD_TREE2(1, 1511, 1347, 15, 25.0D, 317_647, 2, 1.0, new int[]{1282, 1283}),
    DEAD_TREE3(1, 1511, 1352, 15, 25.0D, 317_647, 2, 1.0, new int[]{1365}),
    DEAD_TREE4(1, 1511, 1353, 15, 25.0D, 317_647, 2, 1.0, new int[]{1289}),
    DEAD_TREE5(1, 1511, 1285, 15, 25.0D, 317_647, 2, 1.0, new int[]{1349}),
    ACHEY_TREE(1, 2862, 3371, 15, 25.0D, 317_647, 2, 1.0, new int[]{2023}),
    OAK_TREE(15, 1521, 1356, 25, 37.5D, 361_146, 5, 0.8, new int[]{10820}),
    WILLOW_TREE(30, 1519, 9471, 30, 67.5D, 289_286, 10, 0.6, new int[]{1756, 1758, 1760, 10831}),
    WILLOW_TREE1(30, 1519, 9711, 30, 67.5D, 289_286, 10, 0.6, new int[]{1750, 10819}),
    TEAK_TREE(35, 6333, 9037, 32, 85.0D, 251_528, 10, 0.6, new int[]{9036}),
    MAPLE_TREE(45, 1517, 9712, 35, 100.0D, 221_918, 10, 0.5, new int[]{1759, 10832}),
    HOLLOW_TREE(45, 3239, 4061, 35, 100.0D, 221_918, 10, 0.5, new int[]{1752}),
    MAHOGANY_TREE(50, 6332, 9035, 40, 130.0D, 175_227, 11, 0.4, new int[]{9034}),
    ARCTIC_PINE_TREE(54, 10810, 1356, 40, 150.0D, 155_227, 11, 0.4, new int[]{3037}),
    YEW_TREE(60, 1515, 9714, 45, 175.0D, 145_013, 12, 0.3, new int[]{10822, 10823}),
    MAGIC_TREE(75, 1513, 9713, 60, 250.0D, 72_321, 15, 0.1, new int[]{10834}),
    REDWOOD_TREE(90, 19669, 29669, 60, 250.0D, 28_321, 19, 0.1, new int[]{29668, 29670});

    /** The level required to chop the tree. */
    public final int levelRequired;

    /** The product identification for chopping the tree. */
    public final int item;

    /** THe replacement object for chopping down the tree. */
    public final int replacement;

    /** The respawn time for the tree. */
    public final int respawn;

    /** The experience rewarded for chopping down the tree. */
    public final double experience;

    /** The pet rate for obtaining the tree. */
    public final int petRate;

    /** The log attribute for the tree. */
    public final int logs;

    /** The success rate for chopping down the tree. */
    public final double success;

    /** The tree identifications. */
    public final int[] tree;

    /** Constructs a new <enum>TreeData</enum>. */
    TreeData(int level, int item, int replacement, int respawn, double experience, int petRate, int logs, double success, int[] tree) {
        this.levelRequired = level;
        this.item = item;
        this.replacement = replacement;
        this.respawn = respawn;
        this.experience = experience;
        this.petRate = petRate;
        this.tree = tree;
        this.logs = logs;
        this.success = success;
    }

    /** Gets the tree data based on the object identification. */
    public static TreeData forId(int id) {
        for (TreeData data : TreeData.values())
            for (int object : data.tree)
                if (object == id) return data;
        return null;
    }
}