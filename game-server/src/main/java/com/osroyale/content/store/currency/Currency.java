package com.osroyale.content.store.currency;

import com.osroyale.game.world.entity.mob.player.Player;

/**
 * The parent class of all currencies that provides basic functionality for any
 * general currency. This can be used to register tangible, and even intangible
 * currencies.
 * @author lare96 <http://github.com/lare96>
 */
public interface Currency {

    /**
     * Determines if this currency is tangible.
     * @return {@code true} if this currency is tangible, {@code false} otherwise.
     */
    boolean tangible();

    /**
     * The method executed when the currency is taken from {@code player}.
     * @param player the player the currency is taken from.
     * @param amount the amount of currency that is taken.
     */
    boolean takeCurrency(Player player, int amount);

    /**
     * The method executed when the currency is given to {@code player}.
     * @param player the player the currency is given to.
     * @param amount the amount of currency that is given.
     */
    void recieveCurrency(Player player, int amount);

    /**
     * The method that retrieves the amount of currency {@code player} currently
     * has.
     * @param player the player who's currency amount will be determined.
     * @return the amount of the currency the player has.
     */
    int currencyAmount(Player player);

    /**
     * Determines if the currency can be received when {@code player}'s
     * inventory is full.
     * @param player the player to determine this for.
     * @return {@code true} if the currency can be recieved, {@code false}
     * otherwise.
     */
    boolean canRecieveCurrency(Player player);

    /**
     * Gets the name of the currency.
     */
    String toString();
}

