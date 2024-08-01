package com.osroyale.net.packet.out;

import com.osroyale.net.codec.ByteModification;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.OutgoingPacket;

public class SendObjectAnimation extends OutgoingPacket {

	private final int animation;
	private final GameObject object;

	public SendObjectAnimation(int animation, GameObject object) {
		super(160, 4);
		this.animation = animation;
		this.object = object;
	}

	@Override
	public boolean encode(Player player) {
		player.send(new SendCoordinate(object.getPosition()));
		builder.writeByte(0, ByteModification.SUB)
		.writeByte((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3), ByteModification.SUB)
		.writeShort(animation, ByteModification.ADD);
		return true;
	}

}
