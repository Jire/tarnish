package com.osroyale.content.teleport;

import com.osroyale.Config;
import com.osroyale.game.world.position.Position;

import java.util.Arrays;
import java.util.Optional;

/**
 * Holds all the teleport tablet data.
 * 
 * @author Daniel
 */
public enum TeleportTablet {
	VARROCK_TABLET(8007, new Position(3213, 3424, 0)),
	LUMBRIDGE_TABLET(8008, new Position(3222, 3218, 0)),
	FALADOR_TABLET(8009, new Position(2965, 3379, 0)),
	CAMELOT_TABLET(8010, new Position(2757, 3477, 0)),
	ARDOUGNE_TABLET(8011, new Position(2661, 3305, 0)),
	WATCHTOWER_TABLET(8012, new Position(2549, 3112, 0)),
	HOME_TABLET(8013, Config.DEFAULT_POSITION),
	RIMMINGTON_TABLET(8007, new Position(3159, 3485, 0));

	/** The item identification of the tablet. */
	private final int item;

	/** The position which the tablet will teleport the player. */
	private final Position position;

	/**
	 * The teleport tablet data.
	 * 
	 * @param item
	 *            The item identification of the tablet.
	 * @param position
	 *            The teleport position of the tablet.
	 */
    TeleportTablet(int item, Position position) {
		this.item = item;
		this.position = position;
	}

	/**
	 * Gets the item identification of the tablet.
	 * 
	 * @return The item identification.
	 */
	public int getItem() {
		return item;
	}

	/**
	 * Gets the position of the teleport tablet.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the teleport tablet data based on the item identification.
	 * 
	 * @param id
	 *            The item identification.
	 * @return The teleport tablet data.
	 */
	public static Optional<TeleportTablet> forId(int id) {
		return Arrays.stream(values()).filter(a -> a.item == id).findAny();
	}
}
