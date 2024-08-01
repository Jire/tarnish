package com.osroyale.content.famehall;

/**
 * The fame class.
 * 
 * @author Daniel
 */
public class Fame {

	/** The fame type. */
	private final FameType fame;

	/** The player name. */
	private final String name;

	/** The accomplished date. */
	private final String accomplished;

	/**
	 * Constructs a new <code>Fame<code>.
	 * 
	 * @param fame
	 *            The fame type.
	 * @param name
	 *            The player name.
	 * @param accomplished
	 *            The accomplished date.
	 */
	public Fame(FameType fame, String name, String accomplished) {
		this.fame = fame;
		this.name = name;
		this.accomplished = accomplished;
	}

	/**
	 * Gets the fame type.
	 * 
	 * @return Fame type.
	 */
	public FameType getFame() {
		return fame;
	}

	/**
	 * Gets the player name.
	 * 
	 * @return Player name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the accomplished date.
	 * 
	 * @return Fame accomplished date.
	 */
	public String getAccomplished() {
		return accomplished;
	}
}
