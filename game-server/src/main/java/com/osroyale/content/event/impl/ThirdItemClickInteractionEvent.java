package com.osroyale.content.event.impl;

import com.osroyale.game.world.items.Item;

public class ThirdItemClickInteractionEvent extends ItemInteractionEvent {

	public ThirdItemClickInteractionEvent(Item item, int slot) {
		super(InteractionType.THIRD_ITEM_CLICK, item, slot, 2);
	}
}
