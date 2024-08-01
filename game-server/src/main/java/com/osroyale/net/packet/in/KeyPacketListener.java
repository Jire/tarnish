package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import org.jire.tarnishps.event.widget.KeyPacketEvent;

/**
 * The {@code GamePacket} responsible for clicking keyboard buttons.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(186)
public class KeyPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int key = packet.readShort();

        if (key < 0)
            return;
        if (player.locking.locked(PacketType.KEY))
            return;

        player.getEvents().widget(player, new KeyPacketEvent(key));
    }
}
