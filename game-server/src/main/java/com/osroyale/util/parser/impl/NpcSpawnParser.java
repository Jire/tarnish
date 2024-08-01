package com.osroyale.util.parser.impl;

import com.google.gson.JsonObject;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.parser.GsonParser;
import org.jire.tarnishps.OldToNew;

/**
 * Parses through the npc spawn file and creates {@link Npc}s on startup.
 *
 * @author Daniel | Obey
 */
public class NpcSpawnParser extends GsonParser {

    public NpcSpawnParser() {
        super("def/npc/npc_spawns");
    }

    @Override
    protected void parse(JsonObject data) {
        int id = data.get("id").getAsInt();
        boolean convertId = true;
        if (data.has("convert-id")) {
            convertId = data.get("convert-id").getAsBoolean();
        }
        if (convertId) {
            int newId = OldToNew.get(id);
            if (newId != -1) {
                id = newId;
            }
        }

        final Position position = builder.fromJson(data.get("position"), Position.class);
        final Direction facing = Direction.valueOf(data.get("facing").getAsString());
        int radius = 2;
        if (data.has("radius")) {
            radius = data.get("radius").getAsInt();
        }
        int instance = Mob.DEFAULT_INSTANCE;
        if (data.has("instance")) {
            instance = data.get("instance").getAsInt();
        }
        new Npc(id, position, radius, instance, facing).register();
    }

}
