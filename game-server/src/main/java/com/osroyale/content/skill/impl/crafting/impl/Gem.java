package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.content.skill.impl.crafting.Craftable;
import com.osroyale.content.skill.impl.crafting.CraftableItem;
import com.osroyale.content.skill.impl.crafting.Crafting;
import com.osroyale.game.world.items.Item;

public enum Gem implements Craftable {
	OPAL(new Item(1755), new Item(1625), new CraftableItem(new Item(1609), new Item(1625), 1, 15.0)),
	JADE(new Item(1755), new Item(1627), new CraftableItem(new Item(1611), new Item(1627), 13, 20.0)),
	RED_TOPAZ(new Item(1755), new Item(1629), new CraftableItem(new Item(1613), new Item(1629), 16, 25.0)),
	SAPPHIRE(new Item(1755), new Item(1623), new CraftableItem(new Item(1607), new Item(1623), 20, 50.0)),
	EMERALD(new Item(1755), new Item(1621), new CraftableItem(new Item(1605), new Item(1621), 27, 67.5)),
	RUBY(new Item(1755), new Item(1619), new CraftableItem(new Item(1603), new Item(1619), 34, 85.0)),
	DIAMOND(new Item(1755), new Item(1617), new CraftableItem(new Item(1601), new Item(1617), 43, 107.5)),
	DRAGONSTONE(new Item(1755), new Item(1631), new CraftableItem(new Item(1615), new Item(1631), 55, 137.5)),
	ONYX(new Item(1755), new Item(6571), new CraftableItem(new Item(6573), new Item(6571), 67, 167.5)),
	ZENYTE(new Item(1755), new Item(19496), new CraftableItem(new Item(19493), new Item(19496), 89, 200));


	private final Item use;
	private final Item with;
	private final CraftableItem[] items;

	Gem(Item use, Item with, CraftableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	public static void load() {
		for (Gem cuttable : values()) {
			Crafting.addCraftable(cuttable);
		}
	}

	@Override
	public int getAnimation() {
		switch (this) {
		case OPAL:
		case JADE:
		default:
			return 891;
		case RED_TOPAZ:
			return 892;
		case SAPPHIRE:
			return 888;
		case EMERALD:
			return 889;
		case RUBY:
			return 887;
		case DIAMOND:
			return 890;
		case DRAGONSTONE:
			return 890;
		case ONYX:
			return 2717;
		}
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
		return new Item[] { with };
	}

	@Override
	public String getName() {
		return "Gem";
	}
}