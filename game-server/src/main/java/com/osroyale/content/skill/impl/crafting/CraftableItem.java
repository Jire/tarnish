package com.osroyale.content.skill.impl.crafting;

import com.osroyale.game.world.items.Item;

/**
 * The craftable item.
 *
 * @author Daniel
 */
public final class CraftableItem {

    /**
     * The craftable product.
     */
    private final Item product;

    /**
     * The craftable required item.
     */
    private final Item requiredItem;

    /**
     * The craftable level required.
     */
    private final int level;

    /**
     * The craftable experience rewarded.
     */
    private final double experience;

    /**
     * Constructs a new <code>CraftableItem</code>.
     *
     * @param product      The product item.
     * @param requiredItem The required item.
     * @param level        The level required.
     * @param experience   The experience rewarded.
     */
    public CraftableItem(Item product, Item requiredItem, int level, double experience) {
        this.product = product;
        this.requiredItem = requiredItem;
        this.level = level;
        this.experience = experience;
    }

    /**
     * Gets the product item.
     *
     * @return The product item.
     */
    public Item getProduct() {
        return product;
    }

    /**
     * Gets the required items.
     *
     * @return The required items.
     */
    public Item getRequiredItem() {
        return requiredItem;
    }

    /**
     * Gets the level required.
     *
     * @return The level required.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the experience rewarded.
     *
     * @return The experience rewarded.
     */
    public double getExperience() {
        return experience;
    }
}