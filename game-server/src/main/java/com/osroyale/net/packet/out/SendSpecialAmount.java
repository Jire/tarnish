package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

/**
 * Handles sending the special attack amount (used for the orb).
 *
 * @author Daniel
 */
public final class SendSpecialAmount extends OutgoingPacket {

    public SendSpecialAmount() {
        super(137, 1);
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(player.getSpecialPercentage().get());
        return true;
    }

}
