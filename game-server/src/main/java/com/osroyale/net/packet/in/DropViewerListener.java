package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.widget.DropViewerEvent;

@PacketListenerMeta({ClientPackets.NPC_DROP_VIEWER})
public class DropViewerListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final String context = packet.getRS2String();
        if (context == null || context.isEmpty() || context.equalsIgnoreCase("null")) {
            return;
        }

        player.getEvents().widget(player, new DropViewerEvent(context));
    }
}
