package com.osroyale.game.world.pathfinding.path;

import com.osroyale.game.world.entity.mob.movement.Movement;
import com.osroyale.game.world.position.Position;

import java.util.Deque;

/**
 * Represents a single path in the path finding system.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public class Path {

    /** The deque of all the moves the path has. */
    private final Deque<Position> moves;

    /**
     * Creates a new {@code Path}.
     *
     * @param moves All of the path moves.
     */
    public Path(Deque<Position> moves) {
        this.moves = moves;
    }

    /**
     * Gets the starting location of the path.
     *
     * @return the starting position.
     */
    public Position getOrigin() {
        return moves.getFirst();
    }

    /**
     * Gets the ending location of the path.
     *
     * @return the ending position.
     */
    public Position getDestination() {
        return moves.peekLast();
    }

    /**
     * Gets the condition if the path is possible.
     *
     * @return the condition if the path is possible to be done.
     */
    public boolean isPossible() {
        return moves != null && !moves.isEmpty();
    }

    /**
     * Gets all moves of the {@code Path}.
     *
     * @return the deque containing the coordinates.
     */
    public Deque<Position> getMoves() {
        return moves;
    }

    /**
     * Removing one of the last(but first in the queue) walking points.
     *
     * @return true if removed, false otherwise.
     */
    public Position poll() {
        return moves.pollFirst();
    }

    public void addSteps(final Movement movement) {
        movement.reset();
        while (!moves.isEmpty()) {
            final Position next = moves.poll();
            if (next == null) break;

            movement.addStep(next.getX(), next.getY());
        }
        movement.finish();
    }

}