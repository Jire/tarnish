package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;

public class ItemOnPlayerEvent implements Event {

    private final Player other;

    private final Item used;

    private final int slot;

    public ItemOnPlayerEvent(Player other, Item used, int slot) {
        this.other = other;
        this.used = used;
        this.slot = slot;
    }

    public Player getOther() {
        return other;
    }

    public Item getUsed() {
        return used;
    }

    public int getSlot() {
        return slot;
    }

}
