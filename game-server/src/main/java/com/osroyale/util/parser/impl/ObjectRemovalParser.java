package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.parser.GsonParser;

/**
 * Handles parsing the removed object.
 *
 * @author Daniel
 */
public class ObjectRemovalParser extends GsonParser {

    public ObjectRemovalParser() {
        super("def/object/removed_objects", false);
    }

    @Override
    protected void parse(JsonObject data) {
        Position position = builder.fromJson(data.get("position"), Position.class);
        Region.SKIPPED_OBJECTS.add(position);
    }
}