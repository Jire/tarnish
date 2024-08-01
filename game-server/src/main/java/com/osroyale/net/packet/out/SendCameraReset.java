package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

/**
 * The {@code OutgoingPacket} resets the camera position for {@code Player}.
 * 
 * @author Daniel | Obey
 */
public class SendCameraReset extends OutgoingPacket {

	public SendCameraReset() {
		super(107, 0);
	}

	@Override
	public boolean encode(Player player) {
		return true;
	}
	
}
