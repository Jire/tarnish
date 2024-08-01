package com.osroyale.game.world.object;

import java.util.Arrays;
import java.util.Optional;

/**
 * The group of an object, which indicates its general class (e.g. if it's a
 * wall, or a floor decoration).
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Major
 * @author Scu11
 */
public enum ObjectGroup {

    /** The wall object group, which may block a tile. */
    WALL(0),

    /** The wall decoration object group, which never blocks a tile. */
    WALL_DECORATION(1),

    /**
     * The interactable object group, for objects that can be clicked and
     * interacted with.
     */
    INTERACTABLE_OBJECT(2),

    /** The ground decoration object group, which may block a tile. */
    GROUND_DECORATION(3),

    /** The roof parts that disappear on a camera angle. */
    ROOFING(4);

    /**
     * Attempts to find the ObjectGroup with the specified integer value.
     *
     * @param value The integer value of the ObjectGroup.
     * @return The {@link Optional} possibly containing the ObjectGroup, if
     * found.
     */
    public static Optional<ObjectGroup> valueOf(int value) {
        return Arrays.stream(values()).filter(group -> group.value == value).findAny();
    }

    /** The integer value of the group. */
    private final int value;

    /**
     * Creates the ObjectGroup.
     *
     * @param value The integer value of the group. Must be unique.
     */
    ObjectGroup(int value) {
        this.value = value;
    }

    /**
     * Gets the value of this ObjectGroup.
     *
     * @return The value.
     */
    public int getValue() {
        return value;
    }

}