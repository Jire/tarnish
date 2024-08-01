package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.items.Item;

public class ItemOnItemEvent implements Event {

    private final Item used;
    private final int usedSlot;
    private final Item with;
    private final int withSlot;

    public ItemOnItemEvent(Item used, int usedSlot, Item with, int withSlot) {
        this.used = used;
        this.usedSlot = usedSlot;
        this.with = with;
        this.withSlot = withSlot;
    }

    public Item getUsed() {
        return used;
    }

    public int getUsedSlot() {
        return usedSlot;
    }

    public Item getWith() {
        return with;
    }

    public int getWithSlot() {
        return withSlot;
    }

}
