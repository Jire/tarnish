package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

/**
 * The {@link GamePacket}'s responsible for changing a players region. Used when
 * a player enters a new map region or when the map region has been successfully
 * loaded.
 *
 * @author Daniel
 */
@PacketListenerMeta({ClientPackets.LOADED_REGION, ClientPackets.ENTER_REGION})
public class RegionChangePacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        switch (packet.getOpcode()) {
            case ClientPackets.ENTER_REGION:
                int a = packet.readInt();
                if (player.debug && PlayerRight.isDeveloper(player)) {
                    player.send(new SendMessage("[REGION] Entered new region: " + a, MessageColor.DEVELOPER));
                }
                if (a != 0x3f008edd) {
                    player.getEvents().setLogOut(true);
                }
                break;

            case ClientPackets.LOADED_REGION:
                player.getEvents().setLoadRegion(true);
                break;
        }
    }
}