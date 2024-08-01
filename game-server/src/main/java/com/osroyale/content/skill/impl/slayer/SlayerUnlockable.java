package com.osroyale.content.skill.impl.slayer;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the unlockable slayer rewards and tasks.
 * 
 * @author Daniel
 */
public enum SlayerUnlockable {
	SLAYER_HELM("Malevolent masquerade", 11864, 250, "Learn to combine the protective Slayer\\nheadgear & Slayer gem into one universal\\nhelmet. 55 crafting needed."),
	BROAD_BOLT("Broader fletching", 11874, 75, "Learn to fletch broad arrows (with level 52\\nfletching) and broad bolts (with level 55 \\nfletching)."),
	RING_OF_WEALTH("Ring bling", 2572, 250, "Learn to craft your own Slayer rings, with\\nlevel 75 crafting."),
	CANNON_BALLS("Balls of fury", 2, 75, "Learn to craft your own cannonballs, with\\nlevel 35 smithing."),
	ZULRAH("Give me venom", 12924, 100, "Unlock the ability to be assigned Zulrah as\\na Slayer task, 95 Slayer needed."),
	KING_BLACK_DRAGON("King killer", 11283, 100, "Unlock the ability to be assigned King black\\ndragon as a Slayer task, 75 Slayer needed."),
	CHAOS_ELEMENT("I strive on chaos", 12694, 100, "Unlock the ability to be assigned Chaos\\nelemental as a Slayer task, 65 Slayer\\nneeded."),
	CRAZY_ARCHAEOLOGIST("You drive me crazy", 11990, 100, "Unlock the ability to be assigned Crazy\\narchaeologist as a Slayer task, 60\\nSlayer needed."),;

	/** The unlockable name. */
	private final String name;

	/** The unlockable item display. */
	private final int item;

	/** The unlockable cost. */
	private final int cost;

	/** The unlockable description. */
	private final String description;

	/**
	 * Constructs a new <code>SlayerUnlockable<code>.
	 * 
	 * @param name
	 *            The name of the unlockable.
	 * @param item
	 *            The item display of the unlockable.
	 * @param cost
	 *            The cost of the unlockable.
	 * @param description
	 *            The description of the unlockable.
	 */
    SlayerUnlockable(String name, int item, int cost, String description) {
		this.name = name;
		this.item = item;
		this.cost = cost;
		this.description = description;
	}

	/**
	 * Gets a the unlockable name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the unlockable item display.
	 * 
	 * @return item
	 */
	public int getItem() {
		return item;
	}

	/**
	 * Gets the unlockable cost.
	 * 
	 * @return cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Gets the unlockable description.
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the slayer unlockable data based on the ordinal.
	 * 
	 * @param ordinal
	 *            The unlockable ordinal.
	 * @return The slayer unlockable data.
	 */
	public static Optional<SlayerUnlockable> get(int ordinal) {
		return Arrays.stream(values()).filter($it -> $it.ordinal() == ordinal).findFirst();
	}
}
