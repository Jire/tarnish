package com.osroyale.content.store.currency.impl;

import com.osroyale.content.store.currency.Currency;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ItemDefinition;

/**
 * The currency that provides basic functionality for all tangible currencies.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemCurrency implements Currency {

    /** The item id for this currency. */
    public final int itemId;

    /** Constructs a new {@link ItemCurrency}. */
    public ItemCurrency(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean tangible() {
        return true;
    }

    @Override
    public boolean takeCurrency(Player player, int amount) {
        return player.inventory.remove(new Item(itemId, amount));
    }

    @Override
    public void recieveCurrency(Player player, int amount) {
        player.inventory.add(new Item(itemId, amount));
    }

    @Override
    public int currencyAmount(Player player) {
        return player.inventory.computeAmountForId(itemId);
    }

    @Override
    public boolean canRecieveCurrency(Player player) {
        return player.inventory.contains(itemId);
    }

    @Override
    public String toString() {
        return ItemDefinition.get(itemId).getName();
    }
}
