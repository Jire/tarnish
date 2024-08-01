package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendLogout extends OutgoingPacket {

	public SendLogout() {
		super(109, 0);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}

}
