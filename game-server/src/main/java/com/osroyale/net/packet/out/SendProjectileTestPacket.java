package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.OutgoingPacket;

public class SendProjectileTestPacket extends OutgoingPacket {
    private final Position position;
    private final Position offsetX, offsetY;
    private int lockon, gfxMoving, startHeight, endHeight, time, speed, angle, creatorSize, startDistanceOffset;

    public SendProjectileTestPacket(Position position, Position offset,
                          int angle, int speed, int gfxMoving, int startHeight, int endHeight,
                          int lockon, int time, int creatorSize, int startDistanceOffset) {
        super(117, 15);
        this.position = position;
        this.offsetY = offset;
        this.offsetX = offset;
        this.lockon = lockon;
        this.gfxMoving = gfxMoving;
        this.startHeight = startHeight;
        this.endHeight = endHeight;
        this.time = time;
        this.speed = speed;
        this.angle = angle;
        this.creatorSize = creatorSize;
        this.startDistanceOffset = startDistanceOffset;
    }

    @Override
    public boolean encode(Player player) {
        player.send(new SendCoordinate(position));
        builder.writeByte(0)
                .writeByte(angle)
                .writeByte(offsetY.getY())
                .writeByte(offsetY.getX())
                .writeShort(lockon)
                .writeShort(gfxMoving)
                .writeByte(startHeight)
                .writeByte(endHeight)
                .writeShort(time)
                .writeShort(speed)
                .writeByte(16) //slope
                .writeByte((creatorSize * 64) + (startDistanceOffset * 64));
        return true;
    }
}