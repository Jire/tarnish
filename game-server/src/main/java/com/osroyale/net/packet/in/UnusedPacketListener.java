package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;

@PacketListenerMeta({3, 35, 36, 58, 77, 78, 85, 86, 156, 200, 226, 238, 230})
public class UnusedPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
//        if (player.debug)
//            player.send(new SendMessage("[UnusedPacketListener] Opcode: " + packet.getOpcode()));
    }
}
