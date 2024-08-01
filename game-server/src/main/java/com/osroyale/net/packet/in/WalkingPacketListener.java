
package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.player.WalkEvent;

/**
 * A packet which handles walking requests.
 *
 * @author Graham Edgecombe
 */
@PacketListenerMeta({ClientPackets.WALK_ON_COMMAND, ClientPackets.REGULAR_WALK, ClientPackets.MAP_WALK})
public class WalkingPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int targetX = packet.readShort(ByteOrder.LE);
        final int targetY = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final boolean runQueue = packet.readByte(ByteModification.NEG) == 1;

        player.getEvents().interact(player, new WalkEvent(targetX, targetY, runQueue));
    }

}