package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendSideBarInterface extends OutgoingPacket {

	private final int tabId;
	private final int interfaceId;

	public SendSideBarInterface(int tabId, int interfaceId) {
		super(71, 3);
		this.tabId = tabId;
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(interfaceId)
		.writeByte(tabId, ByteModification.ADD);
		return true;
	}

}
