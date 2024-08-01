package com.osroyale.game.world.pathfinding.path.impl;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.pathfinding.path.Path;
import com.osroyale.game.world.pathfinding.path.PathFinder;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.Utility;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.osroyale.game.world.entity.mob.Direction.NONE;
import static com.osroyale.game.world.entity.mob.Direction.getDirection;

/**
 * Represents a simple path finder which determines a straight path to the first
 * blocked tile or it's destination. Mostly used by {@link Mob} following and
 * movement.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Michael | Chex
 */
public final class SimplePathFinder extends PathFinder {

    /**
     * A default method to find a path for the specified position.
     *
     * @return A {@link Deque} of {@link Position steps} to the specified
     * destination.
     */
    @Override
    public Path find(Mob source, Position end, int targetWidth, int targetLength) {
        int approximation = (int) (source.getPosition().getLongestDelta(end) * 1.5);
        Deque<Position> positions = new ArrayDeque<>(approximation);
        return new Path(addWalks(source, end, targetWidth, targetLength, positions));
    }

    /**
     * Performs the path finding calculations to find the path using the A*
     * algorithm.
     *
     * @param source    The path finder's start position.
     * @param target    The path finder's destination.
     * @param positions The current searched deque of moves.
     * @return The path to pursue to reach the destination.
     */
    private Deque<Position> addWalks(Mob source, Position target, int targetWidth, int targetLength, Deque<Position> positions) {
        Position current = source.getPosition();
        Position targ = target;
        Interactable targInt = Interactable.create(target, targetWidth, targetLength);

        if (targetWidth > 0 || targetLength > 0)
            target = Utility.findBestInside(source, targInt);

        boolean projectiles = source.pathfinderProjectiles;

        while (!Region.reachable(targ, targetWidth, targetLength, current, source.width(), source.length())) {
            Direction direction = getDirection(current, target);

            if (!traversable(source, current, targInt, direction, projectiles)) {
                if (direction == Direction.NORTH_WEST) {
                    if (traversable(source, current, targInt, Direction.WEST, projectiles)) {
                        direction = Direction.WEST;
                    } else if (traversable(source, current, targInt, Direction.NORTH, projectiles)) {
                        direction = Direction.NORTH;
                    } else
                        break;
                } else if (direction == Direction.NORTH_EAST) {
                    if (traversable(source, current, targInt, Direction.NORTH, projectiles)) {
                        direction = Direction.NORTH;
                    } else if (traversable(source, current, targInt, Direction.EAST, projectiles)) {
                        direction = Direction.EAST;
                    } else
                        break;
                } else if (direction == Direction.SOUTH_WEST) {
                    if (traversable(source, current, targInt, Direction.SOUTH, projectiles)) {
                        direction = Direction.SOUTH;
                    } else if (traversable(source, current, targInt, Direction.WEST, projectiles)) {
                        direction = Direction.WEST;
                    } else
                        break;
                } else if (direction == Direction.SOUTH_EAST) {
                    if (traversable(source, current, targInt, Direction.SOUTH, projectiles)) {
                        direction = Direction.SOUTH;
                    } else if (traversable(source, current, targInt, Direction.EAST, projectiles)) {
                        direction = Direction.EAST;
                    } else
                        break;
                } else
                    break;
            }

            if (direction == NONE)
                break;

            current = current.transform(direction.getFaceLocation());
            positions.addLast(current);
        }
        return positions;
    }

    private boolean traversable(Mob source, Position current, Interactable target, Direction direction, boolean projectiles) {
        Position next = current.transform(direction.getFaceLocation());
        return (projectiles ? projectileCheck(current, next) : traversable(current, source.width(), direction))
                && !Utility.inside(next, source.width(), source.length(), target.getPosition(), target.width(), target.length());
    }

}
