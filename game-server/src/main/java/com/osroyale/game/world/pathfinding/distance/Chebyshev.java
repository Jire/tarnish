package com.osroyale.game.world.pathfinding.distance;

import com.osroyale.game.world.position.Position;

/**
 * The Chebyshev heuristic, ideal for a system that allows for 8-directional movement.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class Chebyshev implements Distance {

	@Override
	public int calculate(Position to, Position from) {
		int dx = Math.abs(from.getX() - to.getX());
		int dy = Math.abs(from.getX() - to.getY());
		return dx >= dy ? dx : dy;
	}
}
