package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.position.Position;

public class MovementEvent implements Event {

    private final Position destination;

    public MovementEvent(Position destination) {
        this.destination = destination;
    }

    public Position getDestination() {
        return destination;
    }

}
