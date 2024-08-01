package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} that opens an itemcontainer for {@code Player}.
 * 
 * @author Daniel | Obey
 */
public class SendInterface extends OutgoingPacket {

	private final int interfaceId;

	public SendInterface(int interfaceId) {
		super(97, 4);
		this.interfaceId = interfaceId;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeInt(interfaceId);
		return true;
	}
}
