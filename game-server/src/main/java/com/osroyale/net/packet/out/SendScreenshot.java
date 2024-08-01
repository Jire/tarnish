package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

/**
 * @author Daniel | Obey
 */
public class SendScreenshot extends OutgoingPacket {
	private final int state;

	public SendScreenshot(int state) {
		super(111, 2);
		this.state = state;
	}

	public SendScreenshot() {
		this(0);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeShort(state);
		return true;
	}
}
