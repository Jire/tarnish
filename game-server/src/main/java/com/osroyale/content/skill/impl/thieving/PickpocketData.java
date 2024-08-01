package com.osroyale.content.skill.impl.thieving;

import com.osroyale.Config;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Items;

/**
 * Holds all the data for pickpocketing Npcs.
 * 
 * @author Daniel
 */
public enum PickpocketData {
	MAN(new int[] { 3080, 3081 }, 1, 8, 5, 1, new Item(Config.CURRENCY, 355)),

	WOMAN(new int[] { 3083 }, 1, 8, 5, 1, new Item(Config.CURRENCY, 355)),

	FARMER(new int[] { 3086 }, 10, 14.5, 5, 1, new Item(Config.CURRENCY, 655), new Item(5318, 10)),

	AL_KHARID_WARRIOR(new int[] { 3103 }, 25, 26, 5, 2, new Item(Config.CURRENCY, 800)),

	ROGUE(new int[] { 2884 }, 32, 35.5, 5, 2, new Item(Config.CURRENCY, 1000)),

	MASTER_FARMER(new int[] { 3257, 3097, 5730 }, 38, 43, 5, 3, new Item(Config.CURRENCY, 1150), new Item(Items.POTATO_SEED, 1, 4),
			new Item(Items.ONION_SEED, 1, 2), new Item(Items.CABBAGE_SEED, 1, 3), new Item(Items.TOMATO_SEED, 1, 2),
			new Item(Items.SWEETCORN_SEED, 1, 2), new Item(Items.STRAWBERRY_SEED, 1), new Item(Items.WATERMELON_SEED, 1),
			new Item(22879, 1), new Item(Items.MARIGOLD_SEED, 1), new Item(Items.NASTURTIUM_SEED, 1), new Item(Items.ROSEMARY_SEED, 1),
			new Item(Items.WOAD_SEED, 1), new Item(Items.LIMPWURT_SEED, 1), new Item(Items.GUAM_SEED, 1), new Item(Items.MARRENTILL_SEED, 1),
			new Item(Items.TARROMIN_SEED, 1), new Item(Items.HARRALANDER_SEED, 1), new Item(Items.RANARR_SEED, 1), new Item(Items.TOADFLAX_SEED, 1),
			new Item(Items.IRIT_SEED, 1), new Item(Items.AVANTOE_SEED, 1), new Item(Items.KWUARM_SEED, 1), new Item(Items.SNAPDRAGON_SEED, 1),
			new Item(Items.CADANTINE_SEED, 1), new Item(Items.LANTADYME_SEED, 1), new Item(Items.DWARF_WEED_SEED, 1), new Item(Items.TORSTOL_SEED, 1)),

	VARROCK_GUARD(new int[] { 3010 }, 40, 46.8, 5, 2, new Item(Config.CURRENCY, 1250)),

	BEARDED_POLLNIVNIAN_BANDIT(new int[] { 736 }, 45, 65, 5, 5, new Item(Config.CURRENCY, 1450), new Item(1523, 1), new Item(179, 1)),

	DESERT_BANDIT(new int[] { 693 }, 53, 79.5, 5, 3, new Item(Config.CURRENCY, 1450), new Item(1523, 1), new Item(179, 1)),

	KNIGHT(new int[] { 3108 }, 55, 84.3, 5, 3, new Item(Config.CURRENCY, 1650)),

	POLLNIVNIAN_BANDIT(new int[] { 714 }, 55, 84.3, 5, 5, new Item(Config.CURRENCY, 1650)),

	YANILLE_WATCHMAN(new int[] { 3251 }, 65, 137.5, 5, 3, new Item(Config.CURRENCY, 1800)),

	MENAPHITE_THUG(new int[] { 3549 }, 65, 137.5, 5, 5, new Item(Config.CURRENCY, 1800)),

	PALADIN(new int[] { 1144 }, 70, 151.75, 5, 3, new Item(Config.CURRENCY, 2000), new Item(562, 5)),

	GNOME(new int[] { 6094 }, 75, 198.5, 5, 1, new Item(Config.CURRENCY, 6000), new Item(557, 5), new Item(445, 2), new Item(570, 2)),

	HERO(new int[] { 3106 }, 80, 275, 6, 4, new Item(Config.CURRENCY, 6000), new Item(Config.CURRENCY, 4500), new Item(565, 3), new Item(1602, 2), new Item(1994, 2)),

	ELF(new int[] { 5298 }, 85, 353, 6, 5, new Item(Config.CURRENCY, 6000), new Item(Config.CURRENCY, 5500), new Item(565, 3), new Item(1602, 2), new Item(1994, 2));

	/* The npc identification. */
	private final int[] npc;

	/* The level required. */
	private final int level;

	/* The experience rewarded. */
	private final double experience;

	/* The stun time. */
	private final int stun;

	/* The damage from stun. */
	private final int damage;

	/* The possible loots. */
	private final Item[] loot;

	/**
	 * The pickpocketing data.
	 * 
	 * @param npc
	 *            The array of npcc.
	 * @param npc
	 *            The npc identification.
	 * @param level
	 *            The level required.
	 * @param experience
	 *            The experience rewarded.
	 * @param stun
	 *            The stun timer.
	 * @param damage
	 *            The damage from stun.
	 * @param loot
	 *            The possible loots.
	 */
	PickpocketData(int[] npc, int level, double experience, int stun, int damage, Item... loot) {
		this.npc = npc;
		this.level = level;
		this.experience = experience;
		this.stun = stun;
		this.damage = damage;
		this.loot = loot;
	}

	/**
	 * Gets the npc identification.
	 * 
	 * @return The npc identification.
	 */
	public int[] getNpc() {
		return npc;
	}

	/**
	 * Gets the level required.
	 * 
	 * @return The level required.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the experience rewarded.
	 * 
	 * @return The experience rewarded.
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * Gets The stun timer.
	 * 
	 * @return The stun timer.
	 */
	public int getStun() {
		return stun;
	}

	/**
	 * Gets The damage from stun.
	 * 
	 * @return The damage from stun.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Gets the possible loot.
	 * 
	 * @return The possible loot.
	 */
	public Item[] getLoot() {
		return loot;
	}

	/**
	 * Gets the pickpocket data for npc.
	 * 
	 * @param npc
	 *            The npc being pickpocketed.
	 * @return The pickpocket data.
	 */
	public static PickpocketData forId(int npc) {
		for (PickpocketData data : PickpocketData.values())
			for (int id : data.npc)
				if (id == npc)
					return data;
		return null;
	}
}
