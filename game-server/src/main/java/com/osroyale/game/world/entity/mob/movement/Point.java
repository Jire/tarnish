package com.osroyale.game.world.entity.mob.movement;

/**
 * Represents a single point in the queue.
 * 
 * @author Graham Edgecombe
 */
public class Point {

	/** The x-coordinate. */
	public final int x;

	/** The y-coordinate. */
	public final int y;

	/** The direction to walk to this point. */
	public final int dir;

	/**
	 * Creates a point.
	 * 
	 * @param x
	 *            X coord.
	 * @param y
	 *            Y coord.
	 * @param dir
	 *            Direction to walk to this point.
	 */
	public Point(int x, int y, int dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
}