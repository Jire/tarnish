package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;

public final class SendKillFeed extends OutgoingPacket {

    private static final int OPCODE = 173;

    private final int killerId;
    private final int victimId;

    public SendKillFeed(final int killerId,
                        final int victimId) {
        super(OPCODE, PacketType.FIXED, 4);
        this.killerId = killerId;
        this.victimId = victimId;
    }

    public SendKillFeed(final Mob killer,
                        final Mob victim) {
        this(
                SendEntityFeed.getOpponent(killer),
                SendEntityFeed.getOpponent(victim)
        );
    }

    @Override
    public boolean encode(final Player player) {
        final int killerId = this.killerId;
        final int victimId = this.victimId;
        if (killerId == -1 || victimId == -1) {
            return false;
        }
        builder.writeShort(killerId)
                .writeShort(victimId);
        return true;
    }

}
