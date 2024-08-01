package com.osroyale.game.world.entity.mob.player.persist;

import com.google.gson.JsonElement;
import com.osroyale.game.world.entity.mob.player.Player;

abstract class PlayerJSONProperty {

    final String label;

    PlayerJSONProperty(String label) {
        this.label = label;
    }

    abstract void read(Player player, JsonElement property);

    abstract Object write(Player player);

}
