package com.osroyale.content.staff;

import com.osroyale.game.world.entity.mob.player.Player;

/**
 * The action execute of the staff panel.
 * 
 * @author Daniel
 *
 */
public interface ActionEffect {

	/**
	 * Handles the execute of the staff panel action.
	 * 
	 * @param player
	 *            The player causing the action.
	 *            The other player enduring the action execute.
	 */
    void handle(final Player player);
}
