package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public final class SendRunEnergy extends OutgoingPacket {

	public SendRunEnergy() {
		super(110, 1);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(player.runEnergy);
		return true;
	}

}
