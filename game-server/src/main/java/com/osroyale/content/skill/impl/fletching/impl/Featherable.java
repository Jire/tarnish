package com.osroyale.content.skill.impl.fletching.impl;

import com.osroyale.content.skill.impl.fletching.Fletchable;
import com.osroyale.content.skill.impl.fletching.FletchableItem;
import com.osroyale.content.skill.impl.fletching.Fletching;
import com.osroyale.game.world.items.Item;

public enum Featherable implements Fletchable {
	HEADLESS_ARROWS(new Item(314, 15), new Item(52, 15), new FletchableItem(new Item(53, 15), 1, 1.0)),
	BRONZE_BOLT(new Item(314, 10), new Item(9375, 10), new FletchableItem(new Item(877, 10), 9, 0.5)),
	IRON_BOLT(new Item(314, 10), new Item(9377, 10), new FletchableItem(new Item(9140, 10), 39, 1.5)),
	STEEL_BOLT(new Item(314, 10), new Item(9378, 10), new FletchableItem(new Item(9141, 10), 46, 3.5)),
	MITHRIL_BOLT(new Item(314, 10), new Item(9379, 10), new FletchableItem(new Item(9142, 10), 54, 5.0)),
	ADAMANT_BOLT(new Item(314, 10), new Item(9380, 10), new FletchableItem(new Item(9143, 10), 61, 7.0)),
	RUNITE_BOLT(new Item(314, 10), new Item(9381, 10), new FletchableItem(new Item(9144, 10), 69, 10.0)),
	BRONZE_DARTs(new Item(314, 10), new Item(819, 10), new FletchableItem(new Item(806, 10), 1, 1.8)),
	IRON_DARTs(new Item(314, 10), new Item(820, 10), new FletchableItem(new Item(807, 10), 22, 3.8)),
	STEEL_DARTs(new Item(314, 10), new Item(821, 10), new FletchableItem(new Item(808, 10), 37, 7.5)),
	MITHRIL_DARTs(new Item(314, 10), new Item(822, 10), new FletchableItem(new Item(809, 10), 52, 11.2)),
	ADAMANT_DARTs(new Item(314, 10), new Item(823, 10), new FletchableItem(new Item(810, 10), 67, 15.0)),
	RUNE_DARTs(new Item(314, 10), new Item(824, 10), new FletchableItem(new Item(811, 10), 81, 18.8)),
	AMETHYST_DARTs(new Item(314, 10), new Item(25853, 10), new FletchableItem(new Item(25849, 10), 90, 21.0)),
	DRAGON_DARTs(new Item(314, 10), new Item(11232, 10), new FletchableItem(new Item(11230, 10), 95, 25.0));

	private final Item use;
	private final Item with;
	private final FletchableItem[] items;

	Featherable(Item use, Item with, FletchableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	public static void load() {
		for (Featherable featherable : values()) {
			Fletching.addFletchable(featherable);
		}
	}

	@Override
	public int getAnimation() {
		switch (this) {
			case BRONZE_DARTs:
				return 8482;
			case IRON_DARTs:
				return 8483;
			case STEEL_DARTs:
				return 8484;
			case MITHRIL_DARTs:
				return 8485;
			case ADAMANT_DARTs:
				return 8486;
			case RUNE_DARTs:
				return 8487;
			case DRAGON_DARTs:
				return 8488;
			case BRONZE_BOLT:
				return 8463;
			case IRON_BOLT:
				return 8464;
			case STEEL_BOLT:
				return 8467;
			case MITHRIL_BOLT:
				return 8468;
			case ADAMANT_BOLT:
				return 8469;
			case RUNITE_BOLT:
				return 8470;
			case HEADLESS_ARROWS:
			default:
				return 1248;
		}
	}

	@Override
	public int getGraphics() {
		return -1;
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
	public FletchableItem[] getFletchableItems() {
		return items;
	}

	@Override
	public String getProductionMessage() {
		return null;
	}

	@Override
	public Item[] getIngredients() {
		return new Item[] { use, with };
	}
}