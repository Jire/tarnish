package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.ground.GroundItem;
import com.osroyale.game.world.position.Position;

public class PickupItemEvent implements Event {

    private final GroundItem groundItem;

    public PickupItemEvent(GroundItem groundItem) {
        this.groundItem = groundItem;
    }

    public GroundItem getGroundItem() {
        return groundItem;
    }

    public Item getItem() {
        return groundItem.item;
    }

    public Position getPosition() {
        return groundItem.getPosition();
    }

}
