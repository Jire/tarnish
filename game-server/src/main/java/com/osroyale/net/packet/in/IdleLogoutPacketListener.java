package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;

/**
 * The {@link GamePacket} responsible logging out a player after a certain
 * amount of time.
 * 
 * @author Daniel
 */
@PacketListenerMeta(202)
public class IdleLogoutPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        player.idle = true;
    }

}