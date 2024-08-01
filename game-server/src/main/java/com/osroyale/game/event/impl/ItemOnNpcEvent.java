package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.items.Item;

public class ItemOnNpcEvent implements Event {

    private final Npc npc;
    private final Item used;
    private final int slot;

    public ItemOnNpcEvent(Npc npc, Item used, int slot) {
        this.npc = npc;
        this.used = used;
        this.slot = slot;
    }

    public Npc getNpc() {
        return npc;
    }

    public Item getUsed() {
        return used;
    }

    public int getSlot() {
        return slot;
    }

}
