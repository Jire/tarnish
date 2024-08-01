package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendScrollbar extends OutgoingPacket {

	private final int scrollbar;
	private final int size;
	
	public SendScrollbar(int scrollbar, int size) {
		super(204, 8);
		this.scrollbar = scrollbar;
		this.size = size;
	}
	
	@Override
	public boolean encode(Player player) {
		builder.writeInt(scrollbar)
		.writeInt(size);
		return true;
	}

}
