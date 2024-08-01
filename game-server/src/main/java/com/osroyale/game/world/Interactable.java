package com.osroyale.game.world;

import com.osroyale.game.world.position.Position;

/**
 * An object implementing {@code Interactable} has uses.
 *
 * @author Michael | Chex
 */
public interface Interactable {

    /** @return the current location */
    Position getPosition();

    /** @return the x coordinate of the {@link #getPosition()} */
    default int getX() {
        return getPosition().getX();
    }

    /** @return the y coordinate of the {@link #getPosition()} */
    default int getY() {
        return getPosition().getY();
    }

    /** @return the z coordinate of the {@link #getPosition()} */
    default int getHeight() {
        return getPosition().getHeight();
    }

    /** @return the width */
    int width();

    /** @return the length */
    int length();

    /**
     * Creates a new instance of an {@link Interactable}.
     *
     * @param position the position.
     * @return a new instance
     */
    static Interactable create(Position position) {
        return new Interactable() {
            @Override
            public Position getPosition() {
                return position;
            }

            @Override
            public int width() {
                return 1;
            }

            @Override
            public int length() {
                return 1;
            }
        };
    }

    /**
     * Creates a new instance of an {@link Interactable}.
     *
     * @param position the position.
     * @param width    the width
     * @param length   the length
     * @return a new instance
     */
    static Interactable create(Position position, int width, int length) {
        return new Interactable() {
            @Override
            public Position getPosition() {
                return position;
            }

            @Override
            public int width() {
                return width;
            }

            @Override
            public int length() {
                return length;
            }
        };
    }

}
