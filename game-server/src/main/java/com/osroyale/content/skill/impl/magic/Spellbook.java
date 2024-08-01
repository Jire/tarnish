package com.osroyale.content.skill.impl.magic;

/**
 * The in-game spellbooks for players.
 * 
 * @author Daniel | Obey
 */
public enum Spellbook {
	MODERN("Modern", 0, 40000),
	ANCIENT("Ancient", 1, 12855),
	LUNAR("Lunar", 2, 29999);
	
	/** The name of the spellbook. */
	private final String name;

	/** The id of this spellbook. */
	private final int id;
	/** The sidebar interfaceId of this spellbook. */
	private final int sidebar;

	/** Constructs a new <code>Spellbook</code>. */
	Spellbook(String name, int id, int sidebar) {
		this.name = name;
		this.id = id;
		this.sidebar = sidebar;
	}
	
	/** Gets the spellbook name. */
	public String getName() {
		return name;
	}

	/** Gets the id of this spellbook. */
	public int getId() {
		return id;
	}

	/** Gets the sidebar interfaceId of this spellbook. */
	public int getInterfaceId() {
		return sidebar;
	}
}
