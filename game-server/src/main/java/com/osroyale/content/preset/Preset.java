package com.osroyale.content.preset;

import com.osroyale.content.skill.impl.magic.Spellbook;
import com.osroyale.game.world.entity.mob.prayer.PrayerBook;
import com.osroyale.game.world.items.Item;

/**
 * The preset class.
 * 
 * @author Daniel
 */
public class Preset {

	/** The name of the preset. */
	private String name;

	/** The inventory items of the preset. */
	private final Item[] inventory;

	/** The equipment items of the preset. */
	private final Item[] equipment;

	/** The quick-prayers of the preset. */
	private final PrayerBook prayer;

	/** The spellbook of the preset. */
	private final Spellbook spellbook;

	/**
	 * Constructs a new preset.
	 * 
	 * @param name
	 *            The name of the preset.
	 * @param inventory
	 *            The inventory of the preset.
	 * @param equipment
	 *            The equipment of the preset.
	 * @param prayer
	 *            The quick-prayers of the preset.
	 * @param spellbook
	 *            The spellbook of the preset.
	 */
	public Preset(String name, Item[] inventory, Item[] equipment, PrayerBook prayer, Spellbook spellbook) {
		this.name = name;
		this.inventory = inventory;
		this.equipment = equipment;
		this.prayer = prayer;
		this.spellbook = spellbook;
	}

	/**
	 * Constructs a new preset.
	 * 
	 * @param name
	 *            The name of the preset.
	 */
	public Preset(String name) {
		this(name, new Item[28], new Item[14], new PrayerBook(), Spellbook.MODERN);
	}

	/**
	 * Constructs a new preset.
	 */
	public Preset() {
		this(null, new Item[28], new Item[14], new PrayerBook(), Spellbook.MODERN);
	}
	

	/**
	 * Gets the name of the preset.
	 * 
	 * @return The preset name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the inventory of the preset.
	 * 
	 * @return The preset inventory.
	 */
	public Item[] getInventory() {
		return inventory;
	}

	public Item[] getEquipment() {
		return equipment;
	}

	public PrayerBook getPrayer() {
		return prayer;
	}

	public Spellbook getSpellbook() {
		return spellbook;
	}

	public void setName(String name) {
		this.name = name;
	}
}
