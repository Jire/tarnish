package com.osroyale.content.consume;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the food data.
 *
 * @author Daniel.
 */
public enum FoodData {
    SLICED_BANANA(3162, 2, "Sliced banana"),
    EASTER_EGG1(1961, 12, "Easter Egg"),
    PUMPKIN(1959, 14, "Pumpkin"),
    HALF_JUG_OF_WINE(1989, 7, "Half Full Wine Jug"),
    WINE(1993, 11, "Wine"),
    MANTA(391, 22, "Manta Ray"),
    SHARK(385, 20, "Shark"),
    LOBSTER(379, 12, "Lobster"),
    BEER(1917, 1, "Beer"),
    GREENMANS_ALE(1909, 1, "Greenman's Ale"),
    TROUT(333, 7, "Trout"),
    SALMON(329, 9, "Salmon"),
    SWORDFISH(373, 14, "Swordfish"),
    TUNA(361, 10, "Tuna"),
    MONKFISH(7946, 16, "Monkfish"),
    SEA_TURTLE(397, 21, "Sea Turtle"),
    CABBAGE(1965, 1, "Cabbage"),
    CAKE(1891, 4, "Cake"),
    BASS(365, 13, "Bass"),
    COD(339, 7, "Cod"),
    POTATO(1942, 1, "Potato"),
    BAKED_POTATO(6701, 4, "Baked Potato"),
    POTATO_WITH_CHEESE(6705, 16, "Potato with Cheese"),
    EGG_POTATO(7056, 16, "Egg Potato"),
    CHILLI_POTATO(7054, 14, "Chilli Potato"),
    MUSHROOM_POTATO(7058, 20, "Mushroom Potato"),
    TUNA_POTATO(7060, 22, "Tuna Potato"),
    SHRIMPS(315, 3, "Shrimps"),
    HERRING(347, 5, "Herring"),
    SARDINE(325, 4, "Sardine"),
    CHOCOLATE_CAKE(1897, 5, "Chocolate Cake"),
    HALF_CHOCOLATE_CAKE(1899, 5, "2/3 Chocolate Cake"),
    CHOCOLATE_SLICE(1901, 5, "Chocolate Slice"),
    ANCHOVIES(319, 1, "Anchovies"),
    PLAIN_PIZZA(2289, 2289, 2291, 7, "Plain Pizza"),
    HALF_PLAIN_PIZZA(2291, 2289, -1, 7, "1/2 Plain pizza"),
    MEAT_PIZZA(2293, 2293, 2295, 8, "Meat Pizza"),
    HALF_MEAT_PIZZA(2295, 2293, -1, 8, "1/2 Meat Pizza"),
    ANCHOVY_PIZZA(2297, 2297, 2299, 9, "Anchovy Pizza"),
    HALF_ANCHOVY_PIZZA(2299, 2297, -1, 9, "1/2 Anchovy Pizza"),
    PINEAPPLE_PIZZA(2301, 2301, 2303, 11, "Pineapple Pizza"),
    HALF_PINEAPPLE_PIZZA(2303, 2301, -1, 11, "1/2 Pineapple Pizza"),
    BREAD(2309, 5, "Bread"),
    APPLE_PIE(2323, 2323, 2335, 7, "Apple Pie"),
    HALF_APPLE_PIE(2335, 2323, -1, 7, "Half Apple Pie"),
    REDBERRY_PIE(2325, 2325, 2333, 5, "Redberry Pie"),
    HALF_REDBERRY_PIE(2333, 2325, -1, 5, "Half Redberry Pie"),
    UGTHANKI_KEBAB(1883, 2, "Ugthanki kebab"),
    MEAT_PIE(2327, 2327, 2331, 6, "Meat Pie"),
    HALF_MEAT_PIE(2331, 2327, -1, 6, "Half Meat Pie"),
    SUMMER_PIE(7218, 7218, 7220, 11, "Summer Pie"),
    HALF_SUMMER_PIE(7220, 7218, -1, 11, "Half Summer Pie"),
    PIKE(351, 8, "Pike"),
    POTATO_WITH_BUTTER(6703, 14, "Potato with Butter"),
    BANANA(1963, 2, "Banana"),
    PEACH(6883, 8, "Peach"),
    ORANGE(2108, 2, "Orange"),
    PINEAPPLE_RINGS(2118, 2, "Pineapple Rings"),
    PINEAPPLE_CHUNKS(2116, 2, "Pineapple Chunks"),
    EASTER_EGG(7928, 1, "Easter Egg"),
    EASTER_EGG2(7929, 1, "Easter Egg"),
    EASTER_EGG3(7930, 1, "Easter Egg"),
    EASTER_EGG4(7931, 1, "Easter Egg"),
    EASTER_EGG5(7932, 1, "Easter Egg"),
    EASTER_EGG6(7933, 1, "Easter Egg"),
    PURPLE_SWEETS(10476, 9, "Purple Sweets"),
    POT_OF_CREAM(2130, 1, "Pot of cream"),
    BANDAGES(4049, 3, "Bandages"),
    COOKED_KARAMBWAN(3144, -1, -1, 18, "Cooked Karambwan"),
    DARK_CRAB(11936, 22, "Dark Crab"),
    ANGLERFISH(13441, 22, "Anglerfish");

    /** The id of the consumable item. */
    private final int id;

    /** The parent food id. */
    private final int parent;

    /** The replacement food id. */
    private final int replacement;

    /** The amount of health received after consuming the item. */
    private final int heal;

    /** The name of the consumable item. */
    private final String name;

    /**
     * The different consumables.
     *
     * @param id   The id of the item.
     * @param heal The amount of health received after consuming the item.
     * @param name The name of the consumable item.
     */
    FoodData(int id, int parent, int replacement, int heal, String name) {
        this.id = id;
        this.parent = parent;
        this.replacement = replacement;
        this.heal = heal;
        this.name = name;
    }

    /**
     * The different consumables.
     *
     * @param id   The id of the item.
     * @param heal The amount of health received after consuming the item.
     * @param name The name of the consumable item.
     */
    FoodData(int id, int heal, String name) {
        this(id, id, -1, heal, name);
    }

    /**
     * Gets the food data based on the item identification.
     *
     * @param id The item identification.
     * @return The food data.
     */
    public static Optional<FoodData> forId(int id) {
        return Arrays.stream(values()).filter(a -> a.id == id).findFirst();
    }

    //TODO Cleaner
    public static int anglerfishHeal(Player player) {
        int hitpoints = player.skills.getMaxLevel(Skill.HITPOINTS);
        if (hitpoints >= 10 && hitpoints <= 19)
            return 3;
        if (hitpoints >= 20 && hitpoints <= 24)
            return 4;
        if (hitpoints >= 25 && hitpoints <= 29)
            return 6;
        if (hitpoints >= 30 && hitpoints <= 39)
            return 7;
        if (hitpoints >= 40 && hitpoints <= 49)
            return 8;
        if (hitpoints >= 50 && hitpoints <= 59)
            return 11;
        if (hitpoints >= 60 && hitpoints <= 69)
            return 12;
        if (hitpoints >= 70 && hitpoints <= 74)
            return 13;
        if (hitpoints >= 75 && hitpoints <= 79)
            return 15;
        if (hitpoints >= 80 && hitpoints <= 89)
            return 16;
        if (hitpoints >= 90 && hitpoints <= 92)
            return 17;
        if (hitpoints >= 93)
            return 22;
        return 0;
    }

    /** Gets the id of the consumable. */
    public int getId() {
        return id;
    }

    /** Gets the replacement id. */
    public int getReplacement() {
        return replacement;
    }

    /** Checks if this food is fast. */
    public boolean isFast() {
        return replacement != -1 || parent != id;
    }

    /** Gets the healing amount of the consumable. */
    public int getHeal() {
        return heal;
    }

    /** Gets the name of the consumable. */
    public String getName() {
        return name;
    }
}
