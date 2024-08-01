package com.osroyale.net.packet.in;

import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.ClickButtonInteractionEvent;
import com.osroyale.game.event.impl.ButtonClickEvent;
import com.osroyale.game.plugin.PluginManager;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;

/**
 * The {@code GamePacket} responsible for clicking buttons on the client.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(ClientPackets.BUTTON_CLICK)
public class ButtonClickPacketListener implements PacketListener {

    @Override
    public void handlePacket(final Player player, GamePacket packet) {
        final int button = packet.readShort();

        if (player.isDead()) {
            return;
        }

        if (player.locking.locked(PacketType.CLICK_BUTTON, button)) {
            return;
        }


        // player.message("Currently not available");

        if (PlayerRight.isDeveloper(player) || PlayerRight.isOwner(player)) {
            player.send(new SendMessage(String.format("[%s]: button=%d", ButtonClickPacketListener.class.getSimpleName(), button)));
            System.out.println(String.format("[%s]: button=%d", ButtonClickPacketListener.class.getSimpleName(), button));
        }//save it plz theres no save button with intellij


        if (EventDispatcher.execute(player, new ClickButtonInteractionEvent(button))) {
            return;
        }

        PluginManager.getDataBus().publish(player, new ButtonClickEvent(button));
    }
}
