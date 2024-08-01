package com.osroyale.net.packet.out;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;

public class SendMarquee extends OutgoingPacket {

    private final String[] strings;
    private final int id;

    public SendMarquee(int id, String... strings) {
        super(205, PacketType.VAR_SHORT);
        this.strings = strings;
        this.id = id;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeInt(id);
        for (int index = 0; index < 5; index++) {
            builder.writeString(index >= strings.length
                    ? ""
                    : strings[index].replace("#players", World.getPlayerCount() + ""));
        }
        return true;
    }
}
