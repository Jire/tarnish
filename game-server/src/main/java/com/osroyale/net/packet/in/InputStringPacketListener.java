package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.widget.InputStringEvent;

/**
 * The {@link GamePacket} responsible for reciving a string sent by the
 * client.
 * 
 * @author Michael | Chex
 */
@PacketListenerMeta(60)
public class InputStringPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final long inputLong = packet.readLong();
		player.getEvents().widget(player, new InputStringEvent(inputLong));
	}

}