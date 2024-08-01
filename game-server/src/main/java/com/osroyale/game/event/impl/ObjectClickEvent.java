package com.osroyale.game.event.impl;

import com.osroyale.game.event.Event;
import com.osroyale.game.world.object.GameObject;

public class ObjectClickEvent implements Event {

    private final int type;

    private final GameObject object;

    public ObjectClickEvent(int type, GameObject object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public GameObject getObject() {
        return object;
    }

}
