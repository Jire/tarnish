package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendInterfaceVisibility extends OutgoingPacket {
    private final int interfaceId;
    private final boolean isHidden;

    public SendInterfaceVisibility(int interfaceId, boolean isHidden) {
        super(5, 5);
        this.interfaceId = interfaceId;
        this.isHidden = isHidden;
    }

    @Override
    protected boolean encode(Player player) {
        builder.writeInt(interfaceId)
                .writeByte(isHidden ? 1 : 0);
        return true;
    }
}
