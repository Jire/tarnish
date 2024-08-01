package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

/**
 * Send other player index on itemcontainer
 * @author Daniel
 *
 */
public class SendPlayerDisplay extends OutgoingPacket {
	
	private final int id;

	public SendPlayerDisplay(int id) {
		super(201, 4);
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeInt(id);
		return true;
	}

}
