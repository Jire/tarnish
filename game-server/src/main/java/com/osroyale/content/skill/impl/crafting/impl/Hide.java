package com.osroyale.content.skill.impl.crafting.impl;

import com.osroyale.content.skill.impl.crafting.Craftable;
import com.osroyale.content.skill.impl.crafting.CraftableItem;
import com.osroyale.content.skill.impl.crafting.Crafting;
import com.osroyale.game.world.items.Item;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public enum Hide implements Craftable {
	GREEN_DRAGONHIDE(new Item(1733), new Item(1745),
			new CraftableItem(new Item(1135), new Item(1745, 3), 63, 186.0),
			new CraftableItem(new Item(1065), new Item(1745, 1), 57, 62.0),
			new CraftableItem(new Item(1099), new Item(1745, 2), 60, 124.0)),
	BLUE_DRAGONHIDE(new Item(1733), new Item(2505),
			new CraftableItem(new Item(2499), new Item(2505, 3), 71, 210.0),
			new CraftableItem(new Item(2487), new Item(2505, 1), 66, 70.0),
			new CraftableItem(new Item(2493), new Item(2505, 2), 68, 140.0)),
	RED_DRAGONHIDE(new Item(1733), new Item(2507),
			new CraftableItem(new Item(2501), new Item(2507, 3), 77, 234.0),
			new CraftableItem(new Item(2489), new Item(2507, 1), 76, 78.0),
			new CraftableItem(new Item(2495), new Item(2507, 2), 75, 156.0)),
	BLACK_DRAGONHIDE(new Item(1733), new Item(2509),
			new CraftableItem(new Item(2503), new Item(2509, 3), 84, 258.0),
			new CraftableItem(new Item(2491), new Item(2509, 1), 79, 86.0),
			new CraftableItem(new Item(2497), new Item(2509, 2), 82, 172.0)),
	SNAKESIN(new Item(1733), new Item(6289),
			new CraftableItem(new Item(6322), new Item(6289, 15), 82, 3 + 2/3.0),
			new CraftableItem(new Item(6324), new Item(6289, 12), 79, 4 + 1/6.0),
			new CraftableItem(new Item(6330), new Item(6289, 8), 79, 4 + 3/8.0),
			new CraftableItem(new Item(6326), new Item(6289, 5), 79, 9.0),
			new CraftableItem(new Item(6328), new Item(6289, 6), 84, 5.0)),
	YAK_HIDE(new Item(1733), new Item(10818),
			new CraftableItem(new Item(10822), new Item(10818), 43, 32.0),
			new CraftableItem(new Item(10824), new Item(10818, 2), 46, 32.0)),
	HARDLEATHER_BODY(new Item(1733), new Item(1743),
			new CraftableItem(new Item(1131), new Item(1743), 28, 35.0));

	private final Item use;
	private final Item with;
	private final CraftableItem[] items;

	Hide(Item use, Item with, CraftableItem... items) {
		this.use = use;
		this.with = with;
		this.items = items;
	}

	public static void load() {
		for (Hide cuttable : values()) {
			Crafting.addCraftable(cuttable);
		}
	}

	public static int[] getDisplay(int id) {
		List<Integer> list = new ArrayList<>();

		for (Hide hide : values()) {
			if (hide.getIngredients(0)[0].getId() == id) {
				list.add(hide.getCraftableItems()[0].getProduct().getId());
			}
		}

		int[] ids = new int[list.size()];

		for (int i = 0; i < list.size(); i++) {
			ids[i] = list.get(i);
		}

		return ids;
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
		return new Item[] { new Item(1734, items[index].getRequiredItem().getAmount()), items[index].getRequiredItem()};
	}

	@Override
	public String getName() {
		return "Hide";
	}
}