package com.osroyale.game.world.object;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Map;
import java.util.Optional;

/**
 * The enumerated type whose elements represent all of the object types.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Maxi <http://www.rune-server.org/members/maxi/>
 * @author lare96 <http://github.com/lare96>
 * @author Artem batutin <artembatutin@gmail.com>
 */
public enum ObjectType {

    /** Represents straight walls, fences etc. */
    STRAIGHT_WALL(0, ObjectGroup.WALL),

    /** Represents diagonal walls corner, fences etc connectors. */
    DIAGONAL_CORNER_WALL(1, ObjectGroup.WALL),

    /** Represents entire walls, fences etc corners. */
    ENTIRE_WALL(2, ObjectGroup.WALL),

    /** Represents straight wall corners, fences etc connectors. */
    WALL_CORNER(3, ObjectGroup.WALL),

    /** Represents straight inside wall decorations. */
    STRAIGHT_INSIDE_WALL_DECORATION(4, ObjectGroup.WALL_DECORATION),

    /** Represents straight outside wall decorations. */
    STRAIGHT_OUTSIDE_WALL_DECORATION(5, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal outside wall decorations. */
    DIAGONAL_OUTSIDE_WALL_DECORATION(6, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal inside wall decorations. */
    DIAGONAL_INSIDE_WALL_DECORATION(7, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal in wall decorations. */
    DIAGONAL_INTERIOR_WALL_DECORATION(8, ObjectGroup.WALL_DECORATION),

    /** Represents diagonal walls, fences etc. */
    DIAGONAL_WALL(9, ObjectGroup.WALL),

    /** Represents all kinds of objects, trees, statues, signs, fountains etc. */
    GENERAL_PROP(10, ObjectGroup.INTERACTABLE_OBJECT),

    /** Represents ground objects like daisies etc. */
    WALKABLE_PROP(11, ObjectGroup.INTERACTABLE_OBJECT),

    /** Represents straight sloped roofs. */
    STRAIGHT_SLOPED_ROOF(12, ObjectGroup.ROOFING),

    /** Represents diagonal sloped roofs. */
    DIAGONAL_SLOPED_ROOF(13, ObjectGroup.ROOFING),

    /** Represents diagonal slope connecting roofs. */
    DIAGONAL_SLOPED_CONNECTING_ROOF(14, ObjectGroup.ROOFING),

    /** Represents straight sloped corner connecting roofs. */
    STRAIGHT_SLOPED_CORNER_CONNECTING_ROOF(15, ObjectGroup.ROOFING),

    /** Represents straight sloped corner roofs. */
    STRAIGHT_SLOPED_CORNER_ROOF(16, ObjectGroup.ROOFING),

    /** Represents straight flat top roofs. */
    STRAIGHT_FLAT_TOP_ROOF(17, ObjectGroup.ROOFING),

    /** Represents straight bottom edge roofs. */
    STRAIGHT_BOTTOM_EDGE_ROOF(18, ObjectGroup.ROOFING),

    /** Represents diagonal bottom edge connecting roofs. */
    DIAGONAL_BOTTOM_EDGE_CONNECTING_ROOF(19, ObjectGroup.ROOFING),

    /** Represents straight bottom edge connecting roofs. */
    STRAIGHT_BOTTOM_EDGE_CONNECTING_ROOF(20, ObjectGroup.ROOFING),

    /** Represents straight bottom edge connecting corner roofs. */
    STRAIGHT_BOTTOM_EDGE_CONNECTING_CORNER_ROOF(21, ObjectGroup.ROOFING),

    /**
     * Represents ground decoration + map signs (quests, water fountains, shops,
     * etc)
     */
    GROUND_PROP(22, ObjectGroup.GROUND_DECORATION);

    /** The identification of this type. */
    private final int id;

    /** The {@link ObjectGroup} this type is associated to. */
    private final ObjectGroup group;

    /**
     * Creates a new {@link ObjectType}.
     *
     * @param id    the identification of this type
     * @param group the group of this type.
     */
    ObjectType(int id, ObjectGroup group) {
        this.id = id;
        this.group = group;
    }

    /**
     * Gets the identification of this type.
     *
     * @return the identification of this type.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the group of this type.
     *
     * @return the group of this type.
     */
    public final ObjectGroup getGroup() {
        return group;
    }

    private static final ObjectType[] values = values();

    /** A mutable {@link Map} of {@code int} keys to {@link ObjectType} values. */
    private static final Int2ObjectMap<ObjectType> idToType = new Int2ObjectOpenHashMap<>(values.length);

    /* Populates the {@link #values} cache. */
    static {
        for (ObjectType type : values) {
            idToType.put(type.getId(), type);
        }
    }

    /**
     * Returns a {@link ObjectType} wrapped in an {@link Optional} for the
     * specified {@code id}.
     *
     * @param id The game object type id.
     * @return The optional game object type.
     */
    public static Optional<ObjectType> valueOf(final int id) {
        return Optional.ofNullable(idToType.get(id));
    }

}
