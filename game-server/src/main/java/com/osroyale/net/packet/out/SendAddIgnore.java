package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendAddIgnore extends OutgoingPacket {

	private final long usernameLong;

	public SendAddIgnore(long usernameLong) {
		super(214, 8);
		this.usernameLong = usernameLong;
	}

	@Override
	protected boolean encode(Player player) {
		builder.writeLong(usernameLong);
		return true;
	}

}
