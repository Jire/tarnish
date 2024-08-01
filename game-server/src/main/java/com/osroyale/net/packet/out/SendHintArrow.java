package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.OutgoingPacket;

public class SendHintArrow extends OutgoingPacket {

    private final Position position;
    private final int type;

    public SendHintArrow(int type) {
        super(254, 6);
        this.position = new Position(0, 0, 0);
        this.type = type;
    }

    public SendHintArrow(Position position) {
        super(254, 6);
        this.position = position;
        this.type = 2;
    }

    public SendHintArrow(Position position, int type) {
        super(254, 6);
        this.position = position;
        this.type = type;
    }

    @Override
    protected boolean encode(Player player) {
        builder.writeByte(type)
                .writeShort(position.getX())
                .writeShort(position.getY())
                .writeByte(position.getHeight());

        return true;
    }
}
