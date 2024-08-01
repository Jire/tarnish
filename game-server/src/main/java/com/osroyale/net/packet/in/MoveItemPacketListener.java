package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.widget.MoveItemEvent;

@PacketListenerMeta(ClientPackets.MOVE_ITEM)
public class MoveItemPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int interfaceId = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int inserting = packet.readByte(ByteModification.NEG);
        final int fromSlot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int toSlot = packet.readShort(ByteOrder.LE);

        player.idle = false;

        player.getEvents().widget(player, new MoveItemEvent(interfaceId, inserting, fromSlot, toSlot));
    }

}
