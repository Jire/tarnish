package com.osroyale.content.famehall;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds all the fame entries.
 * 
 * @author Daniel
 */
public enum FameEntry {
	/**
	 * Player Killing
	 */
	REACH_100_KILLS("Reach 100 kills", 6824, FameType.PVP),
	REACH_250_KILLS("Reach 250 kills", 6824, FameType.PVP),
	REACH_500_KILLS("Reach 500 kills", 6824, FameType.PVP),
	REACH_1000_KILLS("Reach 1000 kills", 6824, FameType.PVP),
	REACH_2500_KILLS("Reach 2500 kills", 6824, FameType.PVP),
	OBTAIN_A_50_KILLSTREAK("First 50 Kill streak", 13315, FameType.PVP),
	OBTAIN_A_100_KILLSTREAK("First 100 Kill streak", 13316, FameType.PVP),
	KILL_200_BOUNTY_HUNTER_TARGETS("Kill 200 Bounty Hunter targets", 12753, FameType.PVP),
	KILL_500_BOUNTY_HUNTER_TARGETS("Kill 500 Bounty Hunter targets", 12756, FameType.PVP),
	KILL_1000_BOUNTY_HUNTER_TARGETS("Kill 1,000 Bounty Hunter targets", 12756, FameType.PVP),
	
	DEAL_10000_MELEE_DAMAGE("Deal 10,000 Melee damage", 4151, FameType.PVP),
	DEAL_10000_MAGIC_DAMAGE("Deal 10,000 Magic damage", 11791, FameType.PVP),
	DEAL_10000_RANGED_DAMAGE("Deal 10,000 Ranged damage", 19481, FameType.PVP),
	
	BOO("Get 100 kills with Abyssal whip", 4151, FameType.PVP),
	
	/**
	 * Monster Killing
	 */
	FIRST_TASSETS_DROP("First Bandos tassets drop", 11834, FameType.PVM),
	FIRST_ELYSIAN_DROP("First Elysian sigil drop", 12819, FameType.PVM),
	FIRST_VISAGE_DROP("First Visage drop", 11286, FameType.PVM),
	OBTAIN_A_BANDOS_PET("First Bandos pet drop", 12650, FameType.PVM),
	OBTAIN_A_ZAMORAK_PET("First Zamorak pet drop", 12652, FameType.PVM),
	OBTAIN_A_SARADOMIN_PET("First Saradomin pet drop", 12651, FameType.PVM),
	OBTAIN_A_ARMADYL_PET("Obtain an Armadyl pet drop", 12649, FameType.PVM),
	OBTAIN_A_ZULRAH_PET("First Zulrah pet drop", 12939, FameType.PVM),
	OBTAIN_A_REX_PET("First Rex pet drop", 12645, FameType.PVM),
	OBTAIN_A_PRIME_PET("First Prime pet drop", 12644, FameType.PVM),
	OBTAIN_A_SUPREME_PET("First Supreme pet drop", 12643, FameType.PVM),
	OBTAIN_A_MOLE_PET("First Giant mole pet drop", 12646, FameType.PVM),
	OBTAIN_A_KRAKEN_PET("First Kraken pet drop", 12655, FameType.PVM),
	OBTAIN_A_KBD_PET("First KBD pet drop", 12653, FameType.PVM),
	OBTAIN_A_VENENATIS_PET("First Venenatis pet drop", 13177, FameType.PVM),
	OBTAIN_A_CALLISTO_PET("First Callisto pet drop", 13178, FameType.PVM),
	OBTAIN_A_VETION_PET("First Vet'ion pet drop", 13179, FameType.PVM),
	OBTAIN_A_SCORPIA_PET("First Scorpia pet drop", 13181, FameType.PVM),
	
	/**
	 * Skilling
	 */
	MAX_AGILITY_LEVEL("Reach level 99 in Agility", 9771, FameType.SKILL),
	MAX_HERBLORE_LEVEL("Reach level 99 in Herblore", 9774, FameType.SKILL),
	MAX_THIEVING_LEVEL("Reach level 99 in Thieving", 9777, FameType.SKILL),
	MAX_CRAFTING_LEVEL("Reach level 99 in Crafting", 9780, FameType.SKILL),
	MAX_FLETCHING_LEVEL("Reach level 99 in Fletching", 9783, FameType.SKILL),
	MAX_SLAYER_LEVEL("Reach level 99 in Slayer", 9786, FameType.SKILL),
	MAX_HUNTER_LEVEL("Reach level 99 in Hunter", 9948, FameType.SKILL),
	MAX_RUNECRAFTING_LEVEL("Reach level 99 in Runecrafting", 9765, FameType.SKILL),
	MAX_CONSTRUCTION_LEVEL("Reach level 99 in Construction", 9789, FameType.SKILL),
	MAX_MINING_LEVEL("Reach level 99 in Mining", 9792, FameType.SKILL),
	MAX_SMITHING_LEVEL("Reach level 99 in Smithing", 9795, FameType.SKILL),
	MAX_FISHING_LEVEL("Reach level 99 in Fishing", 9798, FameType.SKILL),
	MAX_COOKING_LEVEL("Reach level 99 in Cooking", 9801, FameType.SKILL),
	MAX_FIREMAKING_LEVEL("Reach level 99 in Firemaking", 9804, FameType.SKILL),
	MAX_WOODCUTTING_LEVEL("Reach level 99 in Woodcutting", 9807, FameType.SKILL),
	MAX_FARMING_LEVEL("Reach level 99 in Farming", 9810, FameType.SKILL),
	
	/**
	 * Miscellaneous
	 */
	CLAIM_A_MAX_CAPE("Claim a Max cape", 13280, FameType.MISC),
	CLAIM_AN_ACHIEVEMENT_CAPE("Claim an Achievement cape", 13069, FameType.MISC),
	DRINK_10000_POTIONS("Drink 10,000 potions", 139, FameType.MISC),
	EAT_10000_FOOD("Eat 10,000 food", 385, FameType.MISC),
	MAX_CLAN_MEMBERS("Own a clan with 250 gameMembers", 12397, FameType.MISC),
	CRAFT_A_DFS("Craft a Dragonfire shield", 11283, FameType.MISC),
	CRAFT_AN_ELY("Craft an Elysian spirit shield", 12817, FameType.MISC),
	CRAFT_AN_ARCANE("Craft an Arcane spirit shield", 12825, FameType.MISC),
	CRAFT_AN_SPECTRAL("Craft a Spectral spirit shield", 12821, FameType.MISC),
	
	;

	/** The entry string. */
	private final String entry;

	/** The item display. */
	private final int display;

	/** The fame type. */
	private final FameType type;

	/**
	 * Constructs a new <code>FameEntry<code>.
	 * 
	 * @param entry
	 *            The entry string.
	 * @param display
	 *            The display item.
	 * @param type
	 *            The fame type.
	 */
    FameEntry(String entry, int display, FameType type) {
		this.entry = entry;
		this.display = display;
		this.type = type;
	}

	/**
	 * Gets the entry string.
	 * 
	 * @return Entry string.
	 */
	public String getEntry() {
		return entry;
	}

	/**
	 * Gets the display item.
	 * 
	 * @return Display item.
	 */
	public int getDisplay() {
		return display;
	}

	/**
	 * Gets the fame type.
	 * 
	 * @return Fame type.
	 */
	public FameType getType() {
		return type;
	}

	/**
	 * Gets all the entries based on the fame type.
	 * 
	 * @param type
	 *            The fame type to filter out.
	 * @return List of fame entries.
	 */
	public static List<FameEntry> getEntries(FameType type) {
		List<FameEntry> entry_list = new LinkedList<>();
		for (FameEntry entry : FameEntry.values()) {
			if (entry.getType() == type)
				entry_list.add(entry);
		}
		return entry_list;
	}
}
