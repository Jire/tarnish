package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendMultiIcon extends OutgoingPacket {

    private final int icon;

    public SendMultiIcon(int icon) {
        super(61, 1);
        this.icon = icon;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(icon);
        return true;
    }

}
