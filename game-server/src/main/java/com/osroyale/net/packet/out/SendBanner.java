package com.osroyale.net.packet.out;


import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;

public class SendBanner extends OutgoingPacket {

    private final String title;
    private final String message;
    private final int color;

    public SendBanner(Object title, Object message, int color) {
        super(202, PacketType.VAR_BYTE);
        this.title = String.valueOf(title);
        this.message = String.valueOf(message);
        this.color = color;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeString(title).writeString(message).writeInt(color);
        return true;
    }

}
