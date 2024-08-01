package com.osroyale.content.event;

import com.osroyale.game.world.entity.mob.player.Player;

public interface InteractionEventListener {

    boolean onEvent(Player player, InteractionEvent interactionEvent);
}