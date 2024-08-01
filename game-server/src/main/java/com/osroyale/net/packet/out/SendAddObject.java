package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.OutgoingPacket;

public class SendAddObject extends OutgoingPacket {

    private final GameObject object;

    public SendAddObject(GameObject object) {
        super(151, 4);
        this.object = object;
    }

    @Override
    public boolean encode(Player player) {
        if (object.getInstancedHeight() != player.instance) {
            return false;
        }
        player.send(new SendCoordinate(object.getPosition()));
        builder.writeByte(0, ByteModification.ADD);
        builder.writeShort(object.getId(), ByteOrder.LE);
        builder.writeByte((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteModification.SUB);
        return true;
    }

}