package com.osroyale.game.world.items.containers;

import com.osroyale.game.world.items.Item;

import java.util.Optional;

/**
 * A listener that is fired by {@link ItemContainer}.
 * <p></p>
 * One should aim to extend {@link ItemContainerAdapter} for generic use cases rather
 * than implement this directly.
 * @author lare96 <http://github.org/lare96>
 */
public interface ItemContainerListener {

    /**
     * Fired when an {@link Item} is added, removed, or replaced.
     * @param container The {@link ItemContainer} firing the randomevent.
     * @param oldItem   The old item being removed from this container.
     * @param newItem   The new item being added to this container.
     * @param index     The index the update is occurring on.
     * @param refresh   The condition if we have to refresh this container.
     */
    default void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh, boolean login) {
    }

    /**
     * Fired when an {@link Item}s are added, removed, or replaced in bulk. This is to prevent firing multiple {@code
     * itemUpdated(ItemContainer, int)} events for a single operation.
     * @param container The {@link ItemContainer} firing the randomevent.
     */
    default void bulkItemsUpdated(ItemContainer container) {
    }

    /**
     * Fired when the capacity of {@code container} is exceeded.
     * @param container The {@link ItemContainer} firing the randomevent.
     */
    default void capacityExceeded(ItemContainer container) {
    }
}
