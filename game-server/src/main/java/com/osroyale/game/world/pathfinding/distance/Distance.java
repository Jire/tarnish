package com.osroyale.game.world.pathfinding.distance;

import com.osroyale.game.world.position.Position;

/**
 * An interface to calculate the distance between two nodes in a {@link
 * Position}
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public interface Distance {

    /**
     * Calculates the heuristic value of the defined two positions.
     *
     * @param from The first position from which we calculate.
     * @param to   The second position to where we calculate.
     * @return Distance value of the two definitions.
     */
    int calculate(Position from, Position to);

    /**
     * The Chebyshev heuristic, ideal for a system that allows for 8-directional
     * movement.
     *
     * @author Artem Batutin <artembatutin@gmail.com>
     */
    final class Chebyshev implements Distance {

        @Override
        public int calculate(Position to, Position from) {
            int dx = Math.abs(from.getX() - to.getX());
            int dy = Math.abs(from.getX() - to.getY());
            return dx >= dy ? dx : dy;
        }
    }

    /**
     * Since Euclidean distance is shorter than Manhattan or diagonal distance,
     * you will still get shortest paths, but the pathfinder will take longer to
     * run.
     *
     * @author Artem Batutin <artembatutin@gmail.com>
     */
    class Euclidean implements Distance {

        @Override
        public int calculate(Position to, Position from) {
            int deltaX = from.getX() - to.getX();
            int deltaY = from.getY() - to.getY();
            return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        }

    }

    /**
     * The Manhattan Distance is the distance between two points measured along
     * axes at right angles. The name alludes to the grid layout of the streets
     * of Manhattan, which causes the shortest path a car could take between two
     * points in the city. Perfect for 4 dimensional movements.
     *
     * @author Artem Batutin <artembatutin@gmail.com>
     */
    class Manhattan implements Distance {

        @Override
        public int calculate(Position to, Position from) {
            int deltaX = Math.abs(from.getX() - to.getX());
            int deltaY = Math.abs(from.getY() - to.getY());
            return deltaX + deltaY;
        }

    }
}
