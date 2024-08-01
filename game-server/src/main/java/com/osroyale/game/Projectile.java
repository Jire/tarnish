package com.osroyale.game;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.position.Position;

public class Projectile {

	/** Magic combat projectile delays. */
	public static final int[] MAGIC_DELAYS = { 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5 };

	/** Ranged combat projectile delays. */
	public static final int[] RANGED_DELAYS = { 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4 };

	private int id;
	private int delay;
	private int duration;
	private int startHeight;
	private int endHeight;
	private int curve;
	private int distance;
	private int offsetX;
	private int offsetY;

	public Projectile(int id, int delay, int duration, int startHeight, int endHeight, int curve, int distance, int offsetX, int offsetY) {
		setId(id);
		setDelay(delay);
		setDuration(duration);
		setStartHeight(startHeight);
		setEndHeight(endHeight);
		setCurve(curve);
		setDistance(distance);
		setOffsetX(offsetX);
		setOffsetY(offsetY);
	}

	public Projectile(int id, int delay, int duration, int startHeight, int endHeight, int curve, int distance) {
		this(id, delay, duration, startHeight, endHeight, curve, distance, 0, 0);
	}

	public Projectile(int id, int delay, int duration, int startHeight, int endHeight, int curve) {
		this(id, delay, duration, startHeight, endHeight, curve, 64);
	}

	public Projectile(int id, int delay, int duration, int startHeight, int endHeight) {
		this(id, delay, duration, startHeight, endHeight, 16);
	}

	public Projectile(int id) {
		this(id, 51, 68, 43, 31);
	}

	public void send(Mob source, Mob target) {
		World.sendProjectile(source, target, this);
	}

	public void send(Mob source, Position target) {
		World.sendProjectile(source, target, this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public void setStartHeight(int startHeight) {
		this.startHeight = startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public void setEndHeight(int endHeight) {
		this.endHeight = endHeight;
	}

	public int getCurve() {
		return curve;
	}

	public void setCurve(int curve) {
		this.curve = curve;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public Projectile copy() {
		return new Projectile(id, delay, duration, startHeight, endHeight, curve, distance, offsetX, offsetY);
	}

	public int getClientTicks() {
		return /*getDelay() + */getDuration();
	}

}
