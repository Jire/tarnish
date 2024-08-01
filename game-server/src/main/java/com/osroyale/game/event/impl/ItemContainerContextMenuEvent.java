package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;

public class ItemContainerContextMenuEvent implements Event {

    private final int type;
    private final int interfaceId;
    private final int removeSlot;
    private final int removeId;
    private final int amount;

    public ItemContainerContextMenuEvent(int type, int interfaceId, int removeSlot, int removeId) {
        this(type, interfaceId, removeSlot, removeId, 0);
    }

    public ItemContainerContextMenuEvent(int type, int interfaceId, int removeSlot, int removeId, int amount) {
        this.type = type;
        this.interfaceId = interfaceId;
        this.removeSlot = removeSlot;
        this.removeId = removeId;
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getRemoveSlot() {
        return removeSlot;
    }

    public int getRemoveId() {
        return removeId;
    }

    public int getAmount() {
        return amount;
    }
}
