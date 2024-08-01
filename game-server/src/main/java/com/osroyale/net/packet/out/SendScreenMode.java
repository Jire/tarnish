package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.util.ScreenMode;

/** Sends the screen mode state for the player. */
public class SendScreenMode extends OutgoingPacket {
		private final int width;
		private final int length;

	public SendScreenMode(int width, int length) {
		super(108, 6);
		this.width = width;
		this.length = length;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(width, ByteModification.ADD, ByteOrder.LE).writeInt(length);
		return true;
	}
}

