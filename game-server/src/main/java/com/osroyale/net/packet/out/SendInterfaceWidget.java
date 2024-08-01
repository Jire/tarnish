package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;
import com.osroyale.util.MessageColor;

public class SendInterfaceWidget extends OutgoingPacket {

	private final int interfaceID;
	private final int modelID;

	public SendInterfaceWidget(int interfaceID, int modelID) {
		super(8, PacketType.EMPTY);
		this.interfaceID = interfaceID;
		this.modelID = modelID;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(interfaceID, ByteModification.ADD, ByteOrder.LE);
		builder.writeShort(modelID);
		return true;
	}
}
