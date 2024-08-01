package com.osroyale.game.world.items.containers;

import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

import java.util.Optional;

/**
 * An adapter for {@link ItemContainerListener} that updates {@link Item}s on a widget whenever items change, and sends the
 * underlying {@link Player} a message when the container is full.
 * @author lare96 <http://github.org/lare96>
 */
public abstract class ItemContainerAdapter implements ItemContainerListener {

    /**
     * The {@link Player} instance.
     */
    private final Player player;

    /**
     * Creates a new {@link ItemContainerAdapter}.
     * @param player The {@link Player} instance.
     */
    public ItemContainerAdapter(Player player) {
        this.player = player;
    }

    /**
     * Queues a message that displays items from an {@link ItemContainer} on a widget.
     */
    protected void sendItemsToWidget(ItemContainer container) {
        container.refresh(player, getWidgetId());
    }

    /**
     * @return The widget to display items on.
     */
    public abstract int getWidgetId();

    /**
     * @return The message sent when the {@link ItemContainer} exceeds its capacity.
     */
    public abstract String getCapacityExceededMsg();

    @Override
    public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index, boolean refresh, boolean login) {
        if(refresh)
            sendItemsToWidget(container);
    }

    @Override
    public void bulkItemsUpdated(ItemContainer container) {
        sendItemsToWidget(container);
    }

    @Override
    public void capacityExceeded(ItemContainer container) {
        player.send(new SendMessage(getCapacityExceededMsg()));
    }
}
