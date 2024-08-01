package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.content.skill.impl.crafting.Craftable;
import com.osroyale.content.skill.impl.crafting.CraftableItem;
import com.osroyale.game.world.items.Item;

public enum Leather implements Craftable {
	LEATHER_GLOVES(new Item(1733), new Item(1741), new CraftableItem(new Item(1059), new Item(1741), 1, 13.8)),
	LEATHER_BOOTS(new Item(1733), new Item(1741), new CraftableItem(new Item(1061), new Item(1741), 7, 16.25)),
	LEATHER_COWL(new Item(1733), new Item(1741), new CraftableItem(new Item(1167), new Item(1741), 9, 18.5)),
	LEATHER_VANBRACES(new Item(1733), new Item(1741), new CraftableItem(new Item(1063), new Item(1741), 11, 22.0)),
	LEATHER_BODY(new Item(1733), new Item(1741), new CraftableItem(new Item(1129), new Item(1741), 14, 25.0)),
	LEATHER_CHAPS(new Item(1733), new Item(1741), new CraftableItem(new Item(1095), new Item(1741), 18, 27.0)),
	LEATHER_COIF(new Item(1733), new Item(1741), new CraftableItem(new Item(1169), new Item(1741), 38, 37.0));

	private final Item use;
	private final Item with;
	private final CraftableItem[] items;

	Leather(Item use, Item with, CraftableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	@Override
	public int getAnimation() {
		return 1249;
	}

	@Override
	public Item getUse() {
		return use;
	}

	@Override
	public Item getWith() {
		return with;
	}

	@Override
	public CraftableItem[] getCraftableItems() {
		return items;
	}

	@Override
	public String getProductionMessage() {
		return null;
	}

	@Override
	public Item[] getIngredients(int index) {
		return new Item[] { new Item(1734, items[index].getRequiredItem().getAmount()), items[index].getRequiredItem() };
	}

	@Override
	public String getName() {
		return "Leather";
	}
}