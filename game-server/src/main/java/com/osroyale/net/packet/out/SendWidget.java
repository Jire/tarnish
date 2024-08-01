package com.osroyale.net.packet.out;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.OutgoingPacket;

public class SendWidget extends OutgoingPacket {

	private final WidgetType type;
	private final int seconds;

	public SendWidget(WidgetType type, int seconds) {
		super(178, 3);
		this.type = type;
		this.seconds = seconds;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(type.icon)
		.writeShort(seconds * 50);
		return true;
	}
	
	public enum WidgetType {
		ANTI_FIRE(1),
		VENGEANCE(2),
		FROZEN(3),
		TELEBLOCK(4),
		SKULL(5),
		CLAN(6),
		STUN(7),
		POISON(1);

		private final int icon;
		WidgetType(int icon) {
			this.icon = icon;
		}
	}

}
