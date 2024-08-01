package com.osroyale.game.world.pathfinding.distance;

import com.osroyale.game.world.position.Position;

/**
 * The Manhattan Distance is the distance between two points measured along axes
 * at right angles. The name alludes to the grid layout of the streets of
 * Manhattan, which causes the shortest path a car could take between two points
 * in the city. Perfect for 4 dimensional movements.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Manhattan implements Distance {

    @Override
    public int calculate(Position to, Position from) {
        int deltaX = Math.abs(from.getX() - to.getX());
        int deltaY = Math.abs(from.getY() - to.getY());
        return deltaX + deltaY;
    }

}
