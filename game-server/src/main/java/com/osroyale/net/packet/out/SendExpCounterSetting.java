package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendExpCounterSetting extends OutgoingPacket {

	private final int type;
	private final int modification;

	public SendExpCounterSetting(int type, int modification) {
		super(103, 8);
		this.type = type;
		this.modification = modification;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeInt(type)
		.writeInt(modification);
		return true;
	}

}
