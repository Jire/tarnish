package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.OutgoingPacket;

public class SendCoordinate extends OutgoingPacket {

	private final Position position;

	public SendCoordinate(Position position) {
		super(85, 2);
		this.position = position;
	}

	@Override
	public boolean encode(Player player) {
		final int y = position.getLocalY(player.lastPosition);
		final int x = position.getLocalX(player.lastPosition);
		builder.writeByte(y, ByteModification.NEG);
		builder.writeByte(x, ByteModification.NEG);
		return true;
	}

}
