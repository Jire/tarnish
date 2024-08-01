package com.osroyale.content.skill.impl.herblore;

import com.osroyale.game.world.items.Item;

/**
 * The potion making itemcontainer.
 *
 * @author Daniel
 */
public interface Potion {

    /**
     * Gets the potion animation.
     *
     * @return The potion animation.
     */
    int getAnimation();

    /**
     * Gets the experience rewarded.
     *
     * @return The experience rewarded.
     */
    double getExperience();

    /**
     * Gets the ingredients required for the potion.
     *
     * @return The potion ingredients.
     */
    Item[] getIngredients();

    /**
     * Gets the level required for making the potion.
     *
     * @return The level required.
     */
    int getLevel();

    /**
     * Gets the potion product.
     *
     * @return The potion product.
     */
    Item getProduct();
}