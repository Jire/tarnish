package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.widget.DropdownMenuEvent;

/**
 * The {@code GamePacket} responsible for dropdown menus.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(255)
public class DropdownMenuPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int identification = packet.readInt();
        final int value = packet.readByte();

        if (identification < 0)
            return;
        if (value < 0)
            return;

        player.getEvents().widget(player, new DropdownMenuEvent(identification, value));
    }

}
