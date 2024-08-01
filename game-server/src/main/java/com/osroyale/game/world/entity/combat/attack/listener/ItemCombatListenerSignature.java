package com.osroyale.game.world.entity.combat.attack.listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Listens for items.
 *
 * @author Michael | Chex
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemCombatListenerSignature {

    /**
     * The item ids to attach this listener to.
     *
     * @return the item ids
     */
    int[] items();

    /**
     * Checks if all items are required.
     *
     * @return {@code true} if all items listed are required
     */
    boolean requireAll();

}
