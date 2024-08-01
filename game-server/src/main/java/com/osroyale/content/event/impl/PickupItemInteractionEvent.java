package com.osroyale.content.event.impl;

import com.osroyale.content.event.InteractionEvent;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;

public class PickupItemInteractionEvent extends InteractionEvent {

	private final Item item;
	private final Position position;

	public PickupItemInteractionEvent(Item item, Position position) {
		super(InteractionType.PICKUP_ITEM);
		this.item = item;
		this.position = position;
	}

	public Item getItem() {
		return item;
	}

	public Position getPosition() {
		return position;
	}
}