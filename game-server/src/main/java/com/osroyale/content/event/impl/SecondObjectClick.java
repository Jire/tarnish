package com.osroyale.content.event.impl;

import com.osroyale.content.event.InteractionEvent;
import com.osroyale.game.world.object.GameObject;

public class SecondObjectClick extends ObjectInteractionEvent {

	public SecondObjectClick(GameObject object) {
		super(InteractionEvent.InteractionType.SECOND_CLICK_OBJECT, object, 1);
	}

}