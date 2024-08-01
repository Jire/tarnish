package com.osroyale.content.event;

public class InteractionEvent {

	public enum InteractionType {
		FIRST_CLICK_OBJECT,
		SECOND_CLICK_OBJECT,
		THIRD_CLICK_OBJECT,
		
		FIRST_CLICK_NPC,
		SECOND_CLICK_NPC,
		THIRD_CLICK_NPC,
		
		FIRST_ITEM_CLICK,
		SECOND_ITEM_CLICK,
		THIRD_ITEM_CLICK,
		FOURTH_ITEM_CLICK,
		
		ITEM_ON_ITEM,
		ITEM_ON_OBJECT,

		PICKUP_ITEM,
		
		CLICK_BUTTON,

		ITEM_CONTAINER_INTERACTION_EVENT,

		ON_STEP,
		ON_DEATH,
		ON_KILL,

		LOG_IN,
		LOG_OUT


	}

	private final InteractionType type;
	private boolean handled;

	protected InteractionEvent(InteractionType type) {
		this.type = type;
	}

	public InteractionType getType() {
		return type;
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

}
