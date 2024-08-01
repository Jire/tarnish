package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendSpecialEnabled extends OutgoingPacket {
	
	private final int id;

	public SendSpecialEnabled(int id) {
		super(183, 1);
		this.id = id;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(id, ByteModification.NEG);
		return true;
	}
}

