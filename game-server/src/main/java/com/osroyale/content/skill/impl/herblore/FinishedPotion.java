package com.osroyale.content.skill.impl.herblore;

import com.osroyale.game.world.items.Item;

import java.util.Arrays;

/**
 * Holds all the finished potion data.
 *
 * @author Daniel
 */
public enum FinishedPotion implements Potion {
    ATTACK_POTION(121, 91, 221, 1, 25),
    ANTIPOISON(175, 93, 235, 5, 37.5),
    STRENGTH_POTION(115, 95, 225, 12, 50),
    RESTORE_POTION(127, 97, 223, 22, 62.5),
    ENERGY_POTION(3010, 97, 1975, 26, 67.5),
    DEFENCE_POTION(133, 99, 239, 30, 75),
    AGILITY_POTION(3034, 3002, 2152, 34, 80),
    COMBAT_POTION(9741, 97, 9736, 36, 84),
    RAYER_POTION(139, 99, 231, 38, 87.5),
    SUPER_ATTACK(145, 101, 221, 45, 100),
    VIAL_OF_STENCH(18661, 101, 1871, 46, 0),
    FISHING_POTION(181, 101, 235, 48, 112.5),
    SUPER_ENERGY(3018, 103, 2970, 52, 117.5),
    SUPER_STRENGTH(157, 105, 225, 55, 137.5),
    WEAPON_POISON(187, 105, 241, 60, 138),
    SUPER_RESTORE(3026, 3004, 223, 63, 142.5),
    SUPER_DEFENCE(163, 107, 239, 66, 150),
    ANTIFIRE(2454, 2483, 241, 69, 157.5),
    RANGING_POTION(169, 109, 245, 72, 162.5),
    MAGIC_POTION(3042, 2483, 3138, 76, 172.5),
    ZAMORAK_BREW(189, 111, 247, 78, 175),
    SARADOMIN_BREW(6687, 3002, 6693, 81, 180);

    /**
     * The finished potion product.
     */
    private final int product;

    /**
     * The finished potion ingredients.
     */
    private final Item[] ingredients;

    /**
     * The level required to make the potion.
     */
    private final int level;

    /**
     * The experience rewarded for the potion.
     */
    private final double experience;

    /**
     * Constructs a new <code>FinishedPotion</code>.
     *
     * @param product          The product item.
     * @param unfinishedPotion The unfinished potion item.
     * @param ingredient       The ingredients needed for the potion.
     * @param level            The level required.
     * @param experience       The experience rewarded.
     */
    FinishedPotion(int product, int unfinishedPotion, int ingredient, int level, double experience) {
        this.product = product;
        this.ingredients = new Item[]{new Item(unfinishedPotion), new Item(ingredient)};
        this.level = level;
        this.experience = experience;
    }

    /**
     * Gets the finished potion data.
     *
     * @param use  The item being used.
     * @param with The item being used with.
     * @return The finished potion data.
     */
    public static FinishedPotion get(Item use, Item with) {
        for (final FinishedPotion potion : values()) {
            if (potion.ingredients[0].equals(use) && potion.ingredients[1].equals(with) || potion.ingredients[1].equals(use) && potion.ingredients[0].equals(with)) {
                return potion;
            }
        }
        return null;
    }

    @Override
    public int getAnimation() {
        return 363;
    }

    @Override
    public double getExperience() {
        return experience;
    }

    @Override
    public Item[] getIngredients() {
        return ingredients;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public Item getProduct() {
        return new Item(product);
    }

    @Override
    public String toString() {
        return "FinishedPotion[product: " + getProduct() + ", level: " + level + ", experience: " + experience + ", ingredients: " + Arrays.toString(ingredients) + "]";
    }
}
