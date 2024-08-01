package com.osroyale.game.world.entity.mob.player;

/**
 * Represents the options for right-clicking players.
 * 
 * @author Seven
 * @author Michael | Chex
 */
public enum PlayerOption {

	/** The option for challenging another player to a duel. */
	DUEL_REQUEST(1, "Challenge"),
	
	/** The option for attacking another player. */
	ATTACK(2, "Attack"),
	
	/** The option for following another player. */
	FOLLOW(3, "Follow"),
	
	/** The option for trading another player. */
	TRADE_REQUEST(4, "Trade with"),
	
	/** The option for moderators and staff which brings up the report abuse option. */
	REPORT(5, "Report"),
	
	/** The option for moderators and staff which brings up the report abuse option. */
	VIEW_PROFILE(5, "View profile"),
	GAMBLE_REQUEST(6, "Gamble with"),
	;
	
	/** The index of this option in the player menu. */
	private final int index;

	/** The name of this option in the player menu. */
	private final String name;

	/**
	 * Creates a new {@code PlayerOption}.
	 * 
	 * @param index
	 *            The index of this option in the player menu.
	 * 
	 * @param name
	 *            The name of this option in the player menu.
	 */
    PlayerOption(int index, String name) {
		this.index = index;
		this.name = name;
	}

	/**
	 * Gets the index of this option.
	 * 
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the option name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

}
