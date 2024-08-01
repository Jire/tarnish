package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.items.Item;

public class ItemClickEvent implements Event {

    private final int type;

    private final Item item;

    private final int slot;

    public ItemClickEvent(int type, Item item, int slot) {
        this.type = type;
        this.item = item;
        this.slot = slot;
    }

    public int getType() {
        return type;
    }

    public Item getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

}
