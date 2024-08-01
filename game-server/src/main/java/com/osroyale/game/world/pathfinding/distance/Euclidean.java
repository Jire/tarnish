package com.osroyale.game.world.pathfinding.distance;

import com.osroyale.game.world.position.Position;

/**
 * Since Euclidean distance is shorter than Manhattan or diagonal distance, you will still get shortest paths,
 * but the pathfinder will take longer to run.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Euclidean implements Distance {

	@Override
	public int calculate(Position to, Position from) {
		int deltaX = from.getX() - to.getX();
		int deltaY = from.getY() - to.getY();
		return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

}
