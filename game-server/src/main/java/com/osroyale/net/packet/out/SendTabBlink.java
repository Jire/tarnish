package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

/**
 * Makes a tab blink/flash.
 *
 * @author Daniel
 */
public class SendTabBlink extends OutgoingPacket {

    private int tab;

    public SendTabBlink(int tab) {
        super(24, 1);
        this.tab = tab;
    }

    @Override
    public boolean encode(Player player) {
        builder.writeByte(tab);
        return true;
    }

}
