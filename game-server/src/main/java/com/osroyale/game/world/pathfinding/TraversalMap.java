package com.osroyale.game.world.pathfinding;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.object.GameObjectDefinition;
import com.osroyale.game.world.object.ObjectDirection;
import com.osroyale.game.world.object.ObjectType;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.RandomUtils;

import java.util.LinkedList;
import java.util.List;

import static com.osroyale.game.world.object.ObjectType.*;

/**
 * Contains traversal data for a set of regions.
 *
 * @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class TraversalMap {

    private TraversalMap() {}

    /**
     * Marks a {@link GameObject} with the specified attributes on the specified
     * {@link Position} to the {@code TraversalMap}.
     *
     * @param object The game object.
     * @param add    The condition if the object is added.
     * @param list   the condition if the region object list will be affected.
     */
    public static void markObject(Region region, GameObject object, boolean add, boolean list) {
        if (object.getId() > GameObjectDefinition.MAX_DEFINITIONS) {
            return;
        }

        GameObjectDefinition def = object.getDefinition();

        if (def.isSolid()) {
            Position position = object.getPosition();

            //Sets the sizes.
            final int sizeX;
            final int sizeY;

            if (object.getDirection() == ObjectDirection.NORTH || object.getDirection() == ObjectDirection.SOUTH) {
                sizeX = def.getLength();
                sizeY = def.getWidth();
            } else {
                sizeX = def.getWidth();
                sizeY = def.getLength();
            }

            if (object.getObjectType() == GROUND_PROP) {
                if (def.hasActions() || def.isDecoration()) {
                    if (def.hasActions()) {
                        markOccupant(region, position.getHeight(), position.getX(), position.getY(), sizeX, sizeY, false, add);
                    }
                }
            } else if (object.getObjectType() == GENERAL_PROP || object.getObjectType() == WALKABLE_PROP) {
                markOccupant(region, position.getHeight(), position.getX(), position.getY(), sizeX, sizeY, def.isImpenetrable(), add);
            } else if (object.getObjectType().getId() >= 12) {
                markOccupant(region, position.getHeight(), position.getX(), position.getY(), sizeX, sizeY, def.isImpenetrable(), add);
            } else if (object.getObjectType() == DIAGONAL_WALL) {
                markOccupant(region, position.getHeight(), position.getX(), position.getY(), sizeX, sizeY, def.isImpenetrable(), add);
            } else if (object.getObjectType().getId() >= 0 && object.getObjectType().getId() <= 3) {
                if (add)
                    markWall(region, object.getDirection(), position.getHeight(), position.getX(), position.getY(), object.getObjectType(), def.isImpenetrable());
                else
                    unmarkWall(region, object.getDirection(), position.getHeight(), position.getX(), position.getY(), object.getObjectType(), def.isImpenetrable());
            }
        }

        if (list && (object.getId() == 11700 || object.getDefinition().hasActions())) {
            if (add) region.addObject(object);
            else region.removeObject(object);
        }
    }

    /**
     * Informs the region of an existing wall.
     *
     * @param orientation  The orientation of the wall.
     * @param height       The walls height.
     * @param x            The walls x coordinate.
     * @param y            The walls y coordinate.
     * @param type         The type of wall.
     * @param impenetrable Whether or not this wall can be passed through.
     */
    private static void markWall(Region reg, ObjectDirection orientation, int height, int x, int y, ObjectType type, boolean impenetrable) {
        switch (type) {
            case STRAIGHT_WALL:
                if (orientation == ObjectDirection.WEST) {
                    set(reg, height, x, y, TraversalConstants.WALL_WEST);
                    set(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                        set(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                    }
                }
                if (orientation == ObjectDirection.NORTH) {
                    set(reg, height, x, y, TraversalConstants.WALL_NORTH);
                    set(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                        set(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                    }
                }
                if (orientation == ObjectDirection.EAST) {
                    set(reg, height, x, y, TraversalConstants.WALL_EAST);
                    set(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                        set(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                    }
                }
                if (orientation == ObjectDirection.SOUTH) {
                    set(reg, height, x, y, TraversalConstants.WALL_SOUTH);
                    set(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        set(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                    }
                }
                break;

            case ENTIRE_WALL:
                if (orientation == ObjectDirection.WEST) {
                    set(reg, height, x, y, TraversalConstants.WALL_WEST | TraversalConstants.WALL_NORTH);
                    set(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
                    set(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
                        set(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                        set(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                    }
                }
                if (orientation == ObjectDirection.NORTH) {
                    set(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_NORTH);
                    set(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
                    set(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
                        set(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        set(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                    }
                }
                if (orientation == ObjectDirection.EAST) {
                    set(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH);
                    set(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
                    set(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        set(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                        set(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                    }
                }
                if (orientation == ObjectDirection.SOUTH) {
                    set(reg, height, x, y, TraversalConstants.WALL_WEST | TraversalConstants.WALL_SOUTH);
                    set(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
                    set(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        set(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                        set(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                    }
                }
                break;

            case DIAGONAL_CORNER_WALL:
            case WALL_CORNER:
                if (orientation == ObjectDirection.WEST) {
                    set(reg, height, x, y, TraversalConstants.WALL_NORTH_WEST);
                    set(reg, height, x - 1, y + 1, TraversalConstants.WALL_SOUTH_EAST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
                        set(reg, height, x - 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
                    }
                }
                if (orientation == ObjectDirection.NORTH) {
                    set(reg, height, x, y, TraversalConstants.WALL_NORTH_EAST);
                    set(reg, height, x + 1, y + 1, TraversalConstants.WALL_SOUTH_WEST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
                        set(reg, height, x + 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
                    }
                }
                if (orientation == ObjectDirection.EAST) {
                    set(reg, height, x, y, TraversalConstants.WALL_SOUTH_EAST);
                    set(reg, height, x + 1, y - 1, TraversalConstants.WALL_NORTH_WEST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
                        set(reg, height, x + 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
                    }
                }
                if (orientation == ObjectDirection.SOUTH) {
                    set(reg, height, x, y, TraversalConstants.WALL_SOUTH_WEST);
                    set(reg, height, x - 1, y - 1, TraversalConstants.WALL_NORTH_EAST);
                    if (impenetrable) {
                        set(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
                        set(reg, height, x - 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Informs the region of an existing wall being removed.
     *
     * @param orientation  The orientation of the wall.
     * @param height       The walls height.
     * @param x            The walls x coordinate.
     * @param y            The walls y coordinate.
     * @param type         The type of wall.
     * @param impenetrable Whether or not this wall can be passed through.
     */
    private static void unmarkWall(Region reg, ObjectDirection orientation, int height, int x, int y, ObjectType type, boolean impenetrable) {
        switch (type) {
            case STRAIGHT_WALL:
                if (orientation == ObjectDirection.WEST) {
                    unset(reg, height, x, y, TraversalConstants.WALL_WEST);
                    unset(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                        unset(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                    }
                }
                if (orientation == ObjectDirection.NORTH) {
                    unset(reg, height, x, y, TraversalConstants.WALL_NORTH);
                    unset(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                        unset(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                    }
                }
                if (orientation == ObjectDirection.EAST) {
                    unset(reg, height, x, y, TraversalConstants.WALL_EAST);
                    unset(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                        unset(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                    }
                }
                if (orientation == ObjectDirection.SOUTH) {
                    unset(reg, height, x, y, TraversalConstants.WALL_SOUTH);
                    unset(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        unset(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                    }
                }
                break;

            case ENTIRE_WALL:
                if (orientation == ObjectDirection.WEST) {
                    unset(reg, height, x, y, TraversalConstants.WALL_WEST | TraversalConstants.WALL_NORTH);
                    unset(reg, height, x - 1, y, TraversalConstants.WALL_EAST);
                    unset(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
                        unset(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST);
                        unset(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                    }
                }
                if (orientation == ObjectDirection.NORTH) {
                    unset(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_NORTH);
                    unset(reg, height, x, y + 1, TraversalConstants.WALL_SOUTH);
                    unset(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_NORTH);
                        unset(reg, height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        unset(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                    }
                }
                if (orientation == ObjectDirection.EAST) {
                    unset(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH);
                    unset(reg, height, x + 1, y, TraversalConstants.WALL_WEST);
                    unset(reg, height, x, y - 1, TraversalConstants.WALL_NORTH);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        unset(reg, height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST);
                        unset(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                    }
                }
                if (orientation == ObjectDirection.SOUTH) {
                    unset(reg, height, x, y, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH);
                    unset(reg, height, x, y - 1, TraversalConstants.WALL_WEST);
                    unset(reg, height, x - 1, y, TraversalConstants.WALL_NORTH);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
                        unset(reg, height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_WEST);
                        unset(reg, height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_NORTH);
                    }
                }
                break;

            case DIAGONAL_CORNER_WALL:
            case WALL_CORNER:
                if (orientation == ObjectDirection.WEST) {
                    unset(reg, height, x, y, TraversalConstants.WALL_NORTH_WEST);
                    unset(reg, height, x - 1, y + 1, TraversalConstants.WALL_SOUTH_EAST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
                        unset(reg, height, x - 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
                    }
                }
                if (orientation == ObjectDirection.NORTH) {
                    unset(reg, height, x, y, TraversalConstants.WALL_NORTH_EAST);
                    unset(reg, height, x + 1, y + 1, TraversalConstants.WALL_SOUTH_WEST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
                        unset(reg, height, x + 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
                    }
                }
                if (orientation == ObjectDirection.EAST) {
                    unset(reg, height, x, y, TraversalConstants.WALL_SOUTH_EAST);
                    unset(reg, height, x + 1, y - 1, TraversalConstants.WALL_NORTH_WEST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST);
                        unset(reg, height, x + 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST);
                    }
                }
                if (orientation == ObjectDirection.SOUTH) {
                    unset(reg, height, x, y, TraversalConstants.WALL_SOUTH_WEST);
                    unset(reg, height, x - 1, y - 1, TraversalConstants.WALL_NORTH_EAST);
                    if (impenetrable) {
                        unset(reg, height, x, y, TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST);
                        unset(reg, height, x - 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * Marks the specified set of coordinates blocked, unable to be passed
     * through.
     *
     * @param height     The height.
     * @param localX          The x coordinate.
     * @param localY          The y coordinate.
     */
    public static void block(Region region, int height, int localX, int localY) {
        region.setFlags(height, localX, localY, TraversalConstants.BLOCKED);
    }

    /**
     * Marks the specified coordinates occupied by some object.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param width        The width of the occupation.
     * @param length        The length of the occupation.
     * @param impenetrable Whether or not this occupation can be passed
     *                     through.
     * @param add          Flag if the occupant is added or removed.
     */
    public static void markOccupant(Region region, int height, int x, int y, int width, int length, boolean impenetrable, boolean add) {
        int flag = TraversalConstants.BLOCKED;
        if (impenetrable) {
            flag += TraversalConstants.IMPENETRABLE_BLOCKED;
        }
        for (int xPos = x; xPos < x + width; xPos++) {
            for (int yPos = y; yPos < y + length; yPos++) {
                if (add) set(region, height, xPos, yPos, flag);
                else unset(region, height, xPos, yPos, flag);
            }
        }
    }

    /**
     * Marks the specified coordinates a bridge.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     */
    public static void markBridge(Region region, int height, int x, int y) {
        set(region, height, x, y, TraversalConstants.BRIDGE);
    }

    /**
     * Tests if the specified position can be traversed north.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse north.
     * @return <code>true</code> if it is possible to traverse north otherwise
     * <code>false</code>
     */
    private static boolean isTraversableNorth(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableNorth(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed north.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse north otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableNorth(int height, int x, int y) {
        return isTraversableNorth(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed north.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse north otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableNorth(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x, y + 1, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_SOUTH);
        }
        return isInactive(height, x, y + 1, TraversalConstants.WALL_SOUTH | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed south.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse south.
     * @return <code>true</code> if it is possible to traverse south otherwise
     * <code>false</code>
     */
    private static boolean isTraversableSouth(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableSouth(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed south.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse south otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableSouth(int height, int x, int y) {
        return isTraversableSouth(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed south.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse south otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableSouth(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x, y - 1, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_NORTH);
        }
        return isInactive(height, x, y - 1, TraversalConstants.WALL_NORTH | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed east.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse east.
     * @return <code>true</code> if it is possible to traverse east otherwise
     * <code>false</code>
     */
    private static boolean isTraversableEast(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableEast(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed east.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse east otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableEast(int height, int x, int y) {
        return isTraversableEast(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed east.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse east otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableEast(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x + 1, y, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_WEST);
        }
        return isInactive(height, x + 1, y, TraversalConstants.WALL_WEST | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed west.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse west.
     * @return <code>true</code> if it is possible to traverse west otherwise
     * <code>false</code>
     */
    private static boolean isTraversableWest(int height, int x, int y, int size) {
        for (int width = 0; width < size; width++) {
            for (int length = 0; length < size; length++) {
                if (!isTraversableWest(height, x + width, y + length)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed west.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse west otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableWest(int height, int x, int y) {
        return isTraversableWest(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed west.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse west otherwise
     * <code>false</code>.
     */
    private static boolean isTraversableWest(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x - 1, y, TraversalConstants.IMPENETRABLE_BLOCKED | TraversalConstants.IMPENETRABLE_WALL_EAST);
        }
        return isInactive(height, x - 1, y, TraversalConstants.WALL_EAST | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed north east.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse north east.
     * @return <code>true</code> if it is possible to traverse north east
     * otherwise <code>false</code>
     */
    private static boolean isTraversableNorthEast(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableNorthEast(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed north east.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse north east
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableNorthEast(int height, int x, int y) {
        return isTraversableNorthEast(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed north east.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse north east
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableNorthEast(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x + 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_WALL_SOUTH_WEST) && isInactive(height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_BLOCKED);
        }
        return isInactive(height, x + 1, y + 1, TraversalConstants.WALL_WEST | TraversalConstants.WALL_SOUTH | TraversalConstants.WALL_SOUTH_WEST | TraversalConstants.BLOCKED) && isInactive(height, x + 1, y, TraversalConstants.WALL_WEST | TraversalConstants.BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.WALL_SOUTH | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed north west.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse north west.
     * @return <code>true</code> if it is possible to traverse north west
     * otherwise <code>false</code>
     */
    private static boolean isTraversableNorthWest(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableNorthWest(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed north west.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse north west
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableNorthWest(int height, int x, int y) {
        return isTraversableNorthWest(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed north west.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse north west
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableNorthWest(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x - 1, y + 1, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_WALL_SOUTH_EAST) && isInactive(height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.IMPENETRABLE_WALL_SOUTH | TraversalConstants.IMPENETRABLE_BLOCKED);
        }
        return isInactive(height, x - 1, y + 1, TraversalConstants.WALL_EAST | TraversalConstants.WALL_SOUTH | TraversalConstants.WALL_SOUTH_EAST | TraversalConstants.BLOCKED) && isInactive(height, x - 1, y, TraversalConstants.WALL_EAST | TraversalConstants.BLOCKED) && isInactive(height, x, y + 1, TraversalConstants.WALL_SOUTH | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed south east.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse south east.
     * @return <code>true</code> if it is possible to traverse south east
     * otherwise <code>false</code>
     */
    private static boolean isTraversableSouthEast(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableSouthEast(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed south east.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse south east
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableSouthEast(int height, int x, int y) {
        return isTraversableSouthEast(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed south east.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse south east
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableSouthEast(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x + 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_WALL_NORTH_WEST) && isInactive(height, x + 1, y, TraversalConstants.IMPENETRABLE_WALL_WEST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_BLOCKED);
        }
        return isInactive(height, x + 1, y - 1, TraversalConstants.WALL_WEST | TraversalConstants.WALL_NORTH | TraversalConstants.WALL_NORTH_WEST | TraversalConstants.BLOCKED) && isInactive(height, x + 1, y, TraversalConstants.WALL_WEST | TraversalConstants.BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.WALL_NORTH | TraversalConstants.BLOCKED);
    }

    /**
     * Tests if the specified position can be traversed south west.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param size   The size of the entity attempting to traverse south west.
     * @return <code>true</code> if it is possible to traverse south west
     * otherwise <code>false</code>
     */
    private static boolean isTraversableSouthWest(int height, int x, int y, int size) {
        for (int offsetX = 0; offsetX < size; offsetX++) {
            for (int offsetY = 0; offsetY < size; offsetY++) {
                if (!isTraversableSouthWest(height, x + offsetX, y + offsetY)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Tests if the specified position can be traversed south west.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @return <code>true</code> if it is possible to traverse south west
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableSouthWest(int height, int x, int y) {
        return isTraversableSouthWest(height, x, y, false);
    }

    /**
     * Tests if the specified position can be traversed south west.
     *
     * @param height       The height.
     * @param x            The x coordinate.
     * @param y            The y coordinate.
     * @param impenetrable Whether or not this occupation can be traversed.
     * @return <code>true</code> if it is possible to traverse south west
     * otherwise <code>false</code>.
     */
    private static boolean isTraversableSouthWest(int height, int x, int y, boolean impenetrable) {
        if (impenetrable) {
            return isInactive(height, x - 1, y - 1, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_WALL_NORTH_EAST) && isInactive(height, x - 1, y, TraversalConstants.IMPENETRABLE_WALL_EAST | TraversalConstants.IMPENETRABLE_BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.IMPENETRABLE_WALL_NORTH | TraversalConstants.IMPENETRABLE_BLOCKED);
        }
        return isInactive(height, x - 1, y - 1, TraversalConstants.WALL_EAST | TraversalConstants.WALL_NORTH | TraversalConstants.WALL_NORTH_EAST | TraversalConstants.BLOCKED) && isInactive(height, x - 1, y, TraversalConstants.WALL_EAST | TraversalConstants.BLOCKED) && isInactive(height, x, y - 1, TraversalConstants.WALL_NORTH | TraversalConstants.BLOCKED);
    }

    /**
     * Sets a flag on the specified position.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param flag   The flag to put on this tile.
     */
    public static void set(Region region, int height, int x, int y, int flag) {
        region.setFlags(height, x & 0x3F, y & 0x3F, flag);
    }

    /**
     * Checks whether or not the specified flag is not active on the specified
     * position.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param flag   The flag to check.
     * @return <code>true</code> if the specified flag is not active on the
     * specified position, otherwise <code>false</code>.
     */
    private static boolean isInactive(int height, int x, int y, int flag) {
        int localX = x & 0x3F;
        int localY = y & 0x3F;
        Region region = World.getRegions().getRegion(x, y);
        return (region.getFlags(height, localX, localY) & flag) == 0;
    }

    /**
     * Unsets the specified flag from the specified position.
     *
     * @param height The height.
     * @param x      The x coordinate.
     * @param y      The y coordinate.
     * @param flag   The flag to unset from the specified position.
     */
    private static void unset(Region region, int height, int x, int y, int flag) {
        region.unsetFlags(height, x & 0x3F, y & 0x3F, flag);
    }

    /**
     * Tests whether or not a specified position is traversable in the specified
     * direction.
     *
     * @param from      The position.
     * @param direction The direction to traverse.
     * @param size      The size of the entity attempting to traverse.
     * @return <code>true</code> if the direction is traversable otherwise
     * <code>false</code>.
     */
    public static boolean isTraversable(Position from, Direction direction, int size) {
        switch (direction) {
            case NORTH:
                return isTraversableNorth(from.getHeight(), from.getX(), from.getY(), size);
            case SOUTH:
                return isTraversableSouth(from.getHeight(), from.getX(), from.getY(), size);
            case EAST:
                return isTraversableEast(from.getHeight(), from.getX(), from.getY(), size);
            case WEST:
                return isTraversableWest(from.getHeight(), from.getX(), from.getY(), size);
            case NORTH_EAST:
                return isTraversableNorthEast(from.getHeight(), from.getX(), from.getY(), size);
            case NORTH_WEST:
                return isTraversableNorthWest(from.getHeight(), from.getX(), from.getY(), size);
            case SOUTH_EAST:
                return isTraversableSouthEast(from.getHeight(), from.getX(), from.getY(), size);
            case SOUTH_WEST:
                return isTraversableSouthWest(from.getHeight(), from.getX(), from.getY(), size);
            case NONE:
                return false;
            default:
                throw new IllegalArgumentException("direction: " + direction + " is not valid");
        }
    }

    /**
     * Tests whether or not a specified position is traversable in the specified
     * direction.
     *
     * @param from         The position.
     * @param direction    The direction to traverse.
     * @param impenetrable The condition if impenetrability must be checked.
     * @return <code>true</code> if the direction is traversable otherwise
     * <code>false</code>.
     */
    public static boolean isTraversable(Position from, Direction direction, boolean impenetrable) {
        switch (direction) {
            case NORTH:
                return isTraversableNorth(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case SOUTH:
                return isTraversableSouth(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case EAST:
                return isTraversableEast(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case WEST:
                return isTraversableWest(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case NORTH_EAST:
                return isTraversableNorthEast(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case NORTH_WEST:
                return isTraversableNorthWest(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case SOUTH_EAST:
                return isTraversableSouthEast(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case SOUTH_WEST:
                return isTraversableSouthWest(from.getHeight(), from.getX(), from.getY(), impenetrable);
            case NONE:
                return true;
            default:
                throw new IllegalArgumentException("direction: " + direction + " is not valid");
        }
    }

    /**
     * Returns a {@link List} of positions that are traversable from the
     * specified position.
     *
     * @param southWest The position moving from.
     * @param width      The size of the mob attempting to traverse.
     * @param length
     * @return A {@link List} of positions.
     */
    public static List<Position> getTraversableTiles(Position southWest, int width, int length) {
        List<Position> positions = new LinkedList<>();
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                Position from = southWest.transform(x, y);

                if (isTraversableNorth(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.north());

                if (isTraversableSouth(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.south());

                if (isTraversableEast(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.east());

                if (isTraversableWest(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.west());

                if (isTraversableNorthEast(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.northEast());

                if (isTraversableNorthWest(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.northWest());

                if (isTraversableSouthEast(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.southEast());

                if (isTraversableSouthWest(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.southWest());
            }
        }
        return positions;
    }

    public static Position getRandomTraversableTile(Position southWest, int width, int length) {
        List<Position> positions = new LinkedList<>();
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                Position from = southWest.transform(x, y);

                if (isTraversableNorth(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.north());

                if (isTraversableSouth(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.south());

                if (isTraversableEast(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.east());

                if (isTraversableWest(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.west());

                if (isTraversableNorthEast(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.northEast());

                if (isTraversableNorthWest(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.northWest());

                if (isTraversableSouthEast(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.southEast());

                if (isTraversableSouthWest(from.getHeight(), from.getX(), from.getY()))
                    positions.add(from.southWest());
            }
        }
        if (positions.isEmpty())
            return null;
        return RandomUtils.random(positions);
    }

    public static Position getRandomNonDiagonal(Position from) {
        List<Position> positions = new LinkedList<>();

        if (isTraversableNorth(from.getHeight(), from.getX(), from.getY()))
            positions.add(from.north());

        if (isTraversableSouth(from.getHeight(), from.getX(), from.getY()))
            positions.add(from.south());

        if (isTraversableEast(from.getHeight(), from.getX(), from.getY()))
            positions.add(from.east());

        if (isTraversableWest(from.getHeight(), from.getX(), from.getY()))
            positions.add(from.west());

        if (positions.isEmpty())
            return null;

        return RandomUtils.random(positions);
    }

    public static int getFlags(int x, int y, int height) {
        int localX = x & 0x3F;
        int localY = y & 0x3F;
        Region region = World.getRegions().getRegion(x, y);
        return region.getFlags(height, localX, localY);
    }

    public static int getFlags(Position position) {
        return getFlags(position.getX(), position.getY(), position.getHeight());
    }

    public static boolean blockedNorth(Position position) {
        return !isTraversableNorth(position.getHeight(), position.getX(), position.getY(), false);
    }

    public static boolean blockedEast(Position position) {
        return !isTraversableEast(position.getHeight(), position.getX(), position.getY(), false);
    }

    public static boolean blockedSouth(Position position) {
        return !isTraversableSouth(position.getHeight(), position.getX(), position.getY(), false);
    }

    public static boolean blockedWest(Position position) {
        return !isTraversableWest(position.getHeight(), position.getX(), position.getY(), false);
    }

}