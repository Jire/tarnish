package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.relations.PrivateMessageListStatus;
import com.osroyale.net.packet.OutgoingPacket;

public class SendPrivateMessageListStatus extends OutgoingPacket {

	private final PrivateMessageListStatus status;

	public SendPrivateMessageListStatus(PrivateMessageListStatus status) {
		super(221, 1);
		this.status = status;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(status.ordinal());
		return true;
	}

}