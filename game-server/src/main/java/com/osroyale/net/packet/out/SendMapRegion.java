package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendMapRegion extends OutgoingPacket {

	public SendMapRegion() {
		super(73, 4);
	}

	@Override
	public boolean encode(Player player) {
		player.lastPosition = player.getPosition().copy();
		builder.writeShort(player.getPosition().getChunkX() + 6, ByteModification.ADD)
		.writeShort(player.getPosition().getChunkY() + 6);
		return true;
	}

}
