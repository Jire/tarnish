package com.osroyale.net.packet.in;

import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * The {@link GamePacket} responsible for closing interfaces.
 * 
 * @author Daniel
 */
@PacketListenerMeta(130)
public class CloseInterfacePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {

		switch (packet.getOpcode()) {

		case ClientPackets.CLOSE_WINDOW:
			player.interfaceManager.close(false);
			break;
		}
	}
}