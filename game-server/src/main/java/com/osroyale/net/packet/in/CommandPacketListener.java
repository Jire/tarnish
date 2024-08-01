package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.relations.ChatMessage;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;

/**
 * The {@code GamePacket} responsible for handling user commands send from the
 * client.
 *
 * @author Michael | Chex
 */
@PacketListenerMeta(ClientPackets.PLAYER_COMMAND)
public final class CommandPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final String input = packet.getRS2String().trim().toLowerCase();
        if (input.isEmpty() || input.length() > ChatMessage.CHARACTER_LIMIT) {
            return;
        }

        player.getEvents().widget(player, new org.jire.tarnishps.event.widget.CommandEvent(input));
    }

}
