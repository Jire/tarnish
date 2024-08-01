package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Position;

public class DropItemEvent implements Event {

    private final Item item;
    private final int slot;
    private final Position position;

    public DropItemEvent(Item item, int slot, Position position) {
        this.item = item;
        this.slot = slot;
        this.position = position;
    }

    public Item getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public Position getPosition() {
        return position;
    }

}
