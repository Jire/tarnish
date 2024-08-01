package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendPlayerDialogueHead extends OutgoingPacket {

	private final int interfaceId;

	public SendPlayerDialogueHead(int interfaceId) {
		super(185, 2);
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(interfaceId, ByteModification.ADD, ByteOrder.LE);
		return true;
	}

}
