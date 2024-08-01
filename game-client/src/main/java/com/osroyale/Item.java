package com.osroyale;

public final class Item extends Renderable {

	public Item() {
	}

	public final Model getRotatedModel() {
		ItemDefinition itemDef = ItemDefinition.lookup(itemId);
		return itemDef.method201(itemAmount);
	}

	public Item(int itemId, int amount) {
		this.itemId = itemId;
		this.itemAmount = amount;
	}

	public int itemId;
	public int itemAmount;
	public int type;
}
