package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.widget.InputFieldEvent;

@PacketListenerMeta(142)
public class InputFieldPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int component = packet.readInt();
        final String context = packet.getRS2String();

        player.getEvents().widget(player, new InputFieldEvent(component, context));
    }
}
