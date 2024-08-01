package com.osroyale.content.skill.impl.fletching.impl;

import com.osroyale.content.skill.impl.fletching.Fletchable;
import com.osroyale.content.skill.impl.fletching.FletchableItem;
import com.osroyale.content.skill.impl.fletching.Fletching;
import com.osroyale.game.world.items.Item;

public enum Stringable implements Fletchable {
	SHORTBOWS(new Item(1777), new Item(50), new FletchableItem(new Item(841), 5, 5.0)),
	LONGBOWS(new Item(1777), new Item(48), new FletchableItem(new Item(839), 10, 5.0)),
	OAK_SHORTBOWS(new Item(1777), new Item(54), new FletchableItem(new Item(843), 20, 16.5)),
	OAK_LONGBOWBOWS(new Item(1777), new Item(56), new FletchableItem(new Item(845), 25, 25.0)),
	WILLOW_SHORTBOWS(new Item(1777), new Item(60), new FletchableItem(new Item(849), 35, 32.25)),
	WILLOW_LONGBOWBOWS(new Item(1777), new Item(58), new FletchableItem(new Item(847), 40, 41.5)),
	MAPLE_SHORTBOWS(new Item(1777), new Item(64), new FletchableItem(new Item(853), 50, 50.0)),
	MAPLE_LONGBOWBOWS(new Item(1777), new Item(62), new FletchableItem(new Item(851), 55, 58.2)),
	YEW_SHORTBOWS(new Item(1777), new Item(68), new FletchableItem(new Item(857), 65, 67.5)),
	YEW_LONGBOWBOWS(new Item(1777), new Item(66), new FletchableItem(new Item(855), 70, 75.0)),
	MAGIC_SHORTBOWS(new Item(1777), new Item(72), new FletchableItem(new Item(861), 80, 83.2)),
	MAGIC_LONGBOWBOWS(new Item(1777), new Item(70), new FletchableItem(new Item(859), 85, 91.5)),
	BRONZE_CROSSBOWS(new Item(9438), new Item(9454), new FletchableItem(new Item(9174), 9, 6.0)),
	IRON_CROSSBOWS(new Item(9438), new Item(9457), new FletchableItem(new Item(9177), 39, 22.0)),
	STEEL_CROSSBOWS(new Item(9438), new Item(9459), new FletchableItem(new Item(9179), 46, 27.0)),
	MITHRIL_CROSSBOWS(new Item(9438), new Item(9461), new FletchableItem(new Item(9181), 54, 32.0)),
	ADAMANT_CROSSBOWS(new Item(9438), new Item(9463), new FletchableItem(new Item(9183), 61, 41.0)),
	RUNITE_CROSSBOWS(new Item(9438), new Item(9465), new FletchableItem(new Item(9185), 61, 41.0));

	private final Item use;
	private final Item with;
	private final FletchableItem[] items;

	Stringable(Item use, Item with, FletchableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	public static void load() {
		for (Stringable featherable : values()) {
			Fletching.addFletchable(featherable);
		}
	}

	@Override
	public int getAnimation() {
		switch (this) {
		case SHORTBOWS:
			return 6678;
		case LONGBOWS:
			return 6684;
		case OAK_SHORTBOWS:
			return 6679;
		case OAK_LONGBOWBOWS:
			return 6685;
		case WILLOW_SHORTBOWS:
			return 6680;
		case WILLOW_LONGBOWBOWS:
			return 6686;
		case MAPLE_SHORTBOWS:
			return 6681;
		case MAPLE_LONGBOWBOWS:
			return 6687;
		case YEW_SHORTBOWS:
			return 6682;
		case YEW_LONGBOWBOWS:
			return 6688;
		case MAGIC_SHORTBOWS:
			return 6683;
		case MAGIC_LONGBOWBOWS:
			return 6689;
		case BRONZE_CROSSBOWS:
			return 9454;
		case STEEL_CROSSBOWS:
			return 9457;
		case IRON_CROSSBOWS:
			return 9459;
		case MITHRIL_CROSSBOWS:
			return 9461;
		case ADAMANT_CROSSBOWS:
			return 9463;
		case RUNITE_CROSSBOWS:
			return 9465;
		default:
			return 6678;
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