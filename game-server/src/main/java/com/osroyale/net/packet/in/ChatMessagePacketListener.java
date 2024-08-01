package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.relations.ChatColor;
import com.osroyale.game.world.entity.mob.player.relations.ChatEffect;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;
import org.jire.tarnishps.event.widget.ChatMessageEvent;


/**
 * The {@code GamePacket} responsible for chat messages.
 * 
 * @author Daniel
 */
@PacketListenerMeta(ClientPackets.CHAT)
public class ChatMessagePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final int effect = packet.readByte(false, ByteModification.SUB);
		final int color = packet.readByte(false, ByteModification.SUB);
		final int size = packet.getSize() - 2;

		if (effect < 0 || effect >= ChatEffect.values.length || color < 0 || color >= ChatColor.values.length || size <= 0) {
			return;
		}

		if (player.locking.locked(PacketType.CHAT)) {
			return;
		}

		player.idle = false;

		if (player.punishment.isMuted()) {
			player.send(new SendMessage("You are currently muted and can not talk!"));
			return;
		}

		final byte[] bytes = packet.readBytesReverse(size, ByteModification.ADD);
		player.getEvents().widget(player, new ChatMessageEvent(effect, color, size, bytes));
	}

}