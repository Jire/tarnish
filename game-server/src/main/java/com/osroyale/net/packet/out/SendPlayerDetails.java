package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendPlayerDetails extends OutgoingPacket {

	public SendPlayerDetails() {
		super(249, 3);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(1, ByteModification.ADD)
		.writeShort(player.getIndex(), ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
