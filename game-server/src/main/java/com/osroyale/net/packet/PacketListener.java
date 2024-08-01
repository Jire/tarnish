package com.osroyale.net.packet;

import com.osroyale.game.world.entity.mob.player.Player;

/**
 * The itemcontainer that allows any implementing {@Packet}s. The ability to be
 * intercepted as an in {@code Packet}.
 * 
 * @author SeVen
 */
public interface PacketListener {

	/**
	 * Handles the packet that has just been received.
	 * 
	 * @param player
	 *            The player receiving this packet.
	 * 
	 * @param packet
	 *            The packet that has been received.
	 */
	void handlePacket(Player player, GamePacket packet);
}
