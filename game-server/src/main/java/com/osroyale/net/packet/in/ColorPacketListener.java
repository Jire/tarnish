package com.osroyale.net.packet.in;

import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.content.tittle.PlayerTitle;
import com.osroyale.util.MessageColor;

@PacketListenerMeta(187)
public class ColorPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		int identification = packet.readShort(ByteOrder.LE);
		int value = packet.readInt();
		
		if (player.right.equals(PlayerRight.OWNER)) {
			player.send(new SendMessage("[ColorPacket] - Identification: " + identification + " Value: " + value, MessageColor.DEVELOPER));
		}

		switch (identification) {
		
		case 0:
			player.playerTitle = PlayerTitle.create(player.playerTitle.getTitle(), value);
			player.updateFlags.add(UpdateFlag.APPEARANCE);
			break;
			
		case 1:
			//yell
			break;
		}
	}
}
