package com.osroyale.content.event;

import com.osroyale.content.activity.Activity;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.content.event.InteractionEvent.InteractionType;

public class EventDispatcher {

	private final InteractionEvent interactionEvent;

	public EventDispatcher(InteractionEvent interactionEvent) {
		this.interactionEvent = interactionEvent;
	}

	public static boolean execute(Player player, InteractionEvent event) {
		return player.inActivity() && Activity.evaluate(player, it -> it.onEvent(player, event)) || player.skills.onEvent(event);
	}

	public void dispatch(InteractionType type, EventHandler eventHandler) {
		if (interactionEvent.isHandled()) {
			return;
		}

		if (interactionEvent.getType() == type) {
			interactionEvent.setHandled(eventHandler.handle(interactionEvent));
		}
	}

}
