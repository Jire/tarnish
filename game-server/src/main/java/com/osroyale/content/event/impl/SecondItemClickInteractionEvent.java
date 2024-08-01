package com.osroyale.content.event.impl;

import com.osroyale.content.event.InteractionEvent;
import com.osroyale.game.world.items.Item;

public class SecondItemClickInteractionEvent extends ItemInteractionEvent {

	public SecondItemClickInteractionEvent(Item item, int slot) {
		super(InteractionEvent.InteractionType.SECOND_ITEM_CLICK, item, slot, 1);
	}
}
