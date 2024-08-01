package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.packet.OutgoingPacket;

public class SendPoison extends OutgoingPacket {

    public enum PoisonType {
        NO_POISON,
        REGULAR,
        VENOM
    }

    private final PoisonType type;

    public SendPoison(PoisonType type) {
        super(182, 1);
        this.type = type;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(type.ordinal(), ByteModification.NEG);
        return true;
    }

}
