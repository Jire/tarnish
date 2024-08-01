package com.osroyale.content.skill.impl.construction;

import com.osroyale.game.world.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the construction buildable object.
 * 
 * @author Daniel
 */
public enum BuildableObject {
	/* Main object. */
	CRATE("Crate", BuildableType.MAIN_OBJECT, 1, 100, 1, new Item(960, 1)),
	STOOL("Stool", BuildableType.MAIN_OBJECT, 1102, 250, 5, new Item(960, 2)),
	BOOKCASE("Bookcase", BuildableType.MAIN_OBJECT, 12282, 450, 8, new Item(960, 4)),
	MARKET_STALL("Market stall", BuildableType.MAIN_OBJECT, 1539, 600, 9, new Item(960, 3), new Item(8790, 1)),
	COFFIN("Coffin", BuildableType.MAIN_OBJECT, 398, 750, 10, new Item(960, 2), new Item(2353, 1), new Item(1325, 2)),
	TABLE("Table", BuildableType.MAIN_OBJECT, 595, 900, 15, new Item(960, 5)),
	PILLAR("Pillar", BuildableType.MAIN_OBJECT, 7016, 975, 18, new Item(2351, 5)),
	CHAIR("Chair", BuildableType.MAIN_OBJECT, 6195, 1000, 22, new Item(2351, 3), new Item(8790, 1)),
	KITCHEN_SINK("Kitchen sink", BuildableType.MAIN_OBJECT, 12279, 2500, 40, new Item(960, 2), new Item(2353, 3)),
	GRANDFATHER_CLOCK("Grandfather clock", BuildableType.MAIN_OBJECT, 12293, 2750, 45, new Item(960, 3), new Item(2357)),
	PRAYER_ALTAR("Prayer altar", BuildableType.MAIN_OBJECT, 409, 4000, 50, new Item(960, 3), new Item(1718, 1)),
	CRYSTAL_CHEST("Crystal chest", BuildableType.MAIN_OBJECT, 2191, 5000, 75, new Item(960, 5), new Item(989, 10)),
	BANK_BOOTH("Bank booth", BuildableType.MAIN_OBJECT, 11744, 10000, 90, new Item(20527, 250000), new Item(960, 10), new Item(1775, 5)),

	/* Skill object. */
	TREE("Tree", BuildableType.SKILL_OBJECT, 1278, 950, 15, new Item(8419, 1), new Item(1511, 5)),
	OAK_TREE("Oak tree", BuildableType.SKILL_OBJECT, 11756, 1250, 25, new Item(8421, 1), new Item(1521, 5)),
	
	/* Miscellaneous object. */
	
	
	;

	private final String name;
	private final BuildableType type;
	private final int object;
	private final int level;
	private final int experience;
	private final Item[] items;

	BuildableObject(String name, BuildableType type, int object, int experience, int level, Item... items) {
		this.name = name;
		this.type = type;
		this.object = object;
		this.level = level;
		this.experience = experience;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public BuildableType getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}

	public int getObject() {
		return object;
	}

	public Item[] getItems() {
		return items;
	}

	public static List<BuildableObject> get(BuildableType type) {
		List<BuildableObject> object_list = new ArrayList<>();
		for (BuildableObject object : values()) {
			if (object.type == type) {
				object_list.add(object);
			}
		}
		return object_list;
	}
}
