package com.osroyale.content.skill.impl.fletching.impl;

import com.osroyale.content.skill.impl.fletching.Fletchable;
import com.osroyale.content.skill.impl.fletching.FletchableItem;
import com.osroyale.content.skill.impl.fletching.Fletching;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Items;

public enum Bolt implements Fletchable {
	OPAL_BOLT(new Item(877, 10), new Item(45, 10), new FletchableItem(new Item(879, 10), 11, 1.6)),
	PEARL_BOLT(new Item(9140, 10), new Item(46, 10), new FletchableItem(new Item(880, 10), 41, 3.2)),
	RED_TOPAZ_BOLT(new Item(9141, 10), new Item(9188, 10), new FletchableItem(new Item(9336, 10), 48, 3.9)),
	SAPPHIRE_BOLT(new Item(9142, 10), new Item(9189, 10), new FletchableItem(new Item(9337, 10), 56, 4.7)),
	EMERALD_BOLT(new Item(9142, 10), new Item(9190, 10), new FletchableItem(new Item(9338, 10), 58, 5.5)),
	RUBY_BOLT(new Item(9143, 10), new Item(9191, 10), new FletchableItem(new Item(9339, 10), 63, 6.3)),
	DIAMOND_BOLT(new Item(9143, 10), new Item(9192, 10), new FletchableItem(new Item(9340, 10), 65, 7.0)),
	DRAGONSTONE_BOLT(new Item(9144, 10), new Item(9193, 10), new FletchableItem(new Item(9341, 10), 71, 8.2)),
	AMETHYST_BOLT(new Item(Items.BROAD_BOLTS, 10), new Item(Items.AMETHYST_BOLT_TIPS, 10), new FletchableItem(new Item(Items.AMETHYST_BROAD_BOLTS, 10), 76, 10.6)),
	ONYX_BOLT(new Item(9144, 10), new Item(9194, 10), new FletchableItem(new Item(9342, 10), 73, 9.4));

	private final Item use;
	private final Item with;
	private final FletchableItem[] items;

	Bolt(Item use, Item with, FletchableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	public static void load() {
		for (Bolt cuttable : values()) {
			Fletching.addFletchable(cuttable);
		}
	}

	@Override
	public int getAnimation() {
		switch (this) {
			case OPAL_BOLT:
				return 8472;
			case PEARL_BOLT:
				return 8473;
			case RED_TOPAZ_BOLT:
				return 8475;
			case SAPPHIRE_BOLT:
			case EMERALD_BOLT:
				return 8476;
			case RUBY_BOLT:
			case DIAMOND_BOLT:
				return 8477;
			case DRAGONSTONE_BOLT:
			case ONYX_BOLT:
			default:
				return 8478;
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