package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;

public class SendTooltip extends OutgoingPacket {

	private final String string;
	private final int id;

	public SendTooltip(int id, String string) {
		super(203, PacketType.VAR_SHORT);
		this.string = string;
		this.id = id;
	}


	public SendTooltip(String string, int id) {
		this(id, string);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeString(string)
		.writeShort(id, ByteModification.ADD);
		return true;
	}

}
