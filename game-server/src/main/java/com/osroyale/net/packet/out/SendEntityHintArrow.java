package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendEntityHintArrow extends OutgoingPacket {

    private final Mob entity;
    private final boolean reset;

    public SendEntityHintArrow(Mob entity) {
        this(entity, false);
    }

    public SendEntityHintArrow(Mob entity, boolean reset) {
        super(254, 6);
        this.entity = entity;
        this.reset = reset;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(entity.isPlayer() ? reset ? -1 : 10 : reset ? -1 : 1)
        .writeShort(entity.getIndex())
        .writeByte(0 >> 16)
        .writeByte(0 >> 8)
        .writeByte(0);
        return true;
    }

}
