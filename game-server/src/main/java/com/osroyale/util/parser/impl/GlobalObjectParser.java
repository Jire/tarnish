package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.object.CustomGameObject;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.util.parser.GsonParser;

/**
 * The class that loads all global object on startup.
 * 
 * @author Daniel
 */
public class GlobalObjectParser extends GsonParser {

	public GlobalObjectParser() {
		super("def/object/global_objects", false);
	}

	@Override
	protected void parse(JsonObject data) {
		int id = data.get("id").getAsInt();
		ObjectType type = ObjectType.valueOf(data.get("type").getAsInt()).orElseThrow(IllegalArgumentException::new);
		ObjectDirection rotation = ObjectDirection.valueOf(data.get("rotation").getAsString());
		Position position = builder.fromJson(data.get("position"), Position.class);
		new CustomGameObject(id, position, rotation, type).register();
	}
}
