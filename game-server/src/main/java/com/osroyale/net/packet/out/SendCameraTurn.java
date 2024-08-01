package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendCameraTurn extends OutgoingPacket {
    private final int x;
    private final int y;
    private final int z;
    private final int constantSpeed;
    private final int variableSpeed;

    public SendCameraTurn(int x, int y, int z, int constantSpeed, int variableSpeed) {
        super(177, 7);
        this.x = x ;
        this.y = y;
        this.z = z;
        this.constantSpeed = constantSpeed;
        this.variableSpeed = variableSpeed;
    }
    @Override
    protected boolean encode(Player player) {
        builder.writeByte(x);
        builder.writeByte(y);
        builder.writeShort(z);
        builder.writeByte(constantSpeed);
        builder.writeByte(variableSpeed);
        return true;

    }
}
