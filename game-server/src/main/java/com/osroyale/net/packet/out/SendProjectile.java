package com.osroyale.net.packet.out;

import com.osroyale.game.Projectile;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.OutgoingPacket;

public class SendProjectile extends OutgoingPacket {

	private final Projectile projectile;
	private final Position position;
	private final int lock;
	private final byte offsetX;
	private final byte offsetY;

	public SendProjectile(Projectile projectile, Position position, int lock, byte offsetX, byte offsetY) {
		super(117, 15);
		this.projectile = projectile;
		this.lock = lock;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.position = position;
	}

	@Override
	public boolean encode(Player player) {
		player.send(new SendCoordinate(position));
		builder.writeByte(((projectile.getOffsetX() & 7) << 3) | (projectile.getOffsetY() & 7))
		.writeByte(offsetX)
		.writeByte(offsetY)
		.writeShort(lock)
		.writeShort(projectile.getId())
		.writeByte(projectile.getStartHeight())
		.writeByte(projectile.getEndHeight())
		.writeShort(projectile.getDelay())
		.writeShort(projectile.getDuration())
		.writeByte(projectile.getCurve())
		.writeByte(projectile.getDistance());
		return true;
	}
	
}