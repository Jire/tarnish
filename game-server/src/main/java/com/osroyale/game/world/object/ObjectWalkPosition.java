package com.osroyale.game.world.object;

import com.osroyale.game.world.position.Position;

public class ObjectWalkPosition {

    private final int id;
    private final Position position;

    public ObjectWalkPosition(int id, Position position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }
}
