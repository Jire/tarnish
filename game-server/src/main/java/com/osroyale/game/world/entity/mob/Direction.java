package com.osroyale.game.world.entity.mob;

import com.google.common.collect.ImmutableList;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

/**
 * Represents the enumerated directions an entity can walk or face.
 *
 * @author SeVen
 */
public enum Direction {
    NORTH(0, 1),
    NORTH_EAST(1, 1),
    EAST(1, 0),
    SOUTH_EAST(1, -1),
    SOUTH(0, -1),
    SOUTH_WEST(-1, -1),
    WEST(-1, 0),
    NORTH_WEST(-1, 1),
    NONE(0, 0);

    private final int directionX;
    private final int directionY;
    private final Position faceLocation;
    Direction(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
        this.faceLocation = new Position(directionX, directionY);
    }


    public static final ImmutableList<Direction> DIRECTIONS = ImmutableList.of(NORTH_WEST, NORTH, NORTH_EAST, WEST, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST);
    private static final Direction[] VALID = new Direction[]{NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST};

    public Position getFaceLocation() {
        return faceLocation;
    }

    /**
     * Difference in X coordinates for directions array.
     */
    public static final byte[] DELTA_X = new byte[]{-1, 0, 1, -1, 1, -1, 0, 1};

    /**
     * Difference in Y coordinates for directions array.
     */
    public static final byte[] DELTA_Y = new byte[]{1, 1, 1, 0, 0, -1, -1, -1};

    /**
     * Gets the direction between two locations.
     *
     * @return The direction of the other position.
     */
    public static Direction getDirection(int deltaX, int deltaY) {
        if (deltaY >= 1) {
            if (deltaX >= 1) {
                return NORTH_EAST;
            } else if (deltaX == 0) {
                return NORTH;
            } else if (deltaX <= -1) {
                return NORTH_WEST;
            }
        } else if (deltaY == 0) {
            if (deltaX >= 1) {
                return EAST;
            } else if (deltaX == 0) {
                return NONE;
            } else if (deltaX <= -1) {
                return WEST;
            }
        } else if (deltaY <= -1) {
            if (deltaX >= 1) {
                return SOUTH_EAST;
            } else if (deltaX == 0) {
                return SOUTH;
            } else if (deltaX <= -1) {
                return SOUTH_WEST;
            }
        }

        return Direction.NONE;

    }

    /**
     * Gets the direction between two locations.
     *
     * @param position The position that will be the viewpoint.
     * @param other    The other position to get the direction of.
     * @return The direction of the other position.
     */
    public static Direction getDirection(Position position, Position other) {
        final int deltaX = other.getX() - position.getX();
        final int deltaY = other.getY() - position.getY();

        if (deltaY >= 1) {
            if (deltaX >= 1) {
                return NORTH_EAST;
            } else if (deltaX == 0) {
                return NORTH;
            } else if (deltaX <= -1) {
                return NORTH_WEST;
            }
        } else if (deltaY == 0) {
            if (deltaX >= 1) {
                return EAST;
            } else if (deltaX == 0) {
                return NONE;
            } else if (deltaX <= -1) {
                return WEST;
            }
        } else if (deltaY <= -1) {
            if (deltaX >= 1) {
                return SOUTH_EAST;
            } else if (deltaX == 0) {
                return SOUTH;
            } else if (deltaX <= -1) {
                return SOUTH_WEST;
            }
        }

        return Direction.NONE;

    }

    public static Direction getRandomDirection() {
        int random = Utility.random(4);
        switch (random) {
            case 1: return NORTH;
            case 2: return EAST;
            case 3: return SOUTH;
            case 4: return WEST;
            default: return SOUTH;
        }
    }

    /**
     * Gets the direction between two locations, ignoring corners.
     *
     * @param position The position that will be the viewpoint.
     * @param other    The other position to get the direction of.
     * @return The direction of the other position.
     */
    public static Direction getManhattanDirection(Position position, Position other) {
        final int deltaX = other.getX() - position.getX();
        final int deltaY = other.getY() - position.getY();
        if (deltaY >= 1) {
            if (deltaX == 0) {
                return NORTH;
            }
        } else if (deltaY == 0) {
            if (deltaX >= 1) {
                return Direction.EAST;
            } else if (deltaX == 0) {
                return Direction.SOUTH;
            } else if (deltaX <= -1) {
                return Direction.WEST;
            }
        } else if (deltaY <= -1) {
            if (deltaX == 0) {
                return SOUTH;
            }
        }
        return Direction.SOUTH;

    }

    public static Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case EAST:
                return WEST;
            case NORTH:
                return SOUTH;
            case NORTH_EAST:
                return SOUTH_WEST;
            case NORTH_WEST:
                return SOUTH_EAST;
            case SOUTH:
                return NORTH;
            case SOUTH_EAST:
                return NORTH_WEST;
            case SOUTH_WEST:
                return NORTH_EAST;
            case WEST:
                return EAST;
            default:
                return NONE;
        }
    }

    public static Direction getFollowDirection(Position source, Position target) {
        Direction dir = getDirection(source, target);
        int deltaX = Math.abs(source.getX() - target.getX());
        int deltaY = Math.abs(source.getY() - target.getY());
        boolean vertical = deltaY > deltaX;
        switch (dir) {
            case NORTH_EAST:
                if (vertical) return NORTH;
                else return EAST;
            case NORTH_WEST:
                if (vertical) return NORTH;
                else return WEST;
            case SOUTH_EAST:
                if (vertical) return SOUTH;
                else return EAST;
            case SOUTH_WEST:
                if (vertical) return SOUTH;
                else return WEST;
            default: return dir;
        }
    }

    /**
     * Gets the orientation for door.
     *
     * @param direction The direction of this object.
     * @return The orientation.
     */
    public static int getDoorOrientation(Direction direction) {
        switch (direction) {
            case WEST:
                return 0;
            case NORTH:
                return 1;
            case EAST:
                return 2;
            case SOUTH:
                return 3;
            default:
                return 3;
        }
    }

    /**
     * Gets the Manhattan direction of an orientation. Manhattan direction does
     * not support diagnal directions, so the orientation must be between [0, 3]
     * inclusive.
     *
     * @param orientation
     * @return
     */
    public static Direction ofManhattan(int orientation) {
        switch (orientation) {
            case 0:
                return EAST;
            case 1:
                return SOUTH;
            case 2:
                return WEST;
            case 3:
                return NORTH;
        }
        return NONE;
    }

    public int getManhattanOrientation() {
        switch (this) {
            case NORTH_WEST:
                return 0;
            case NORTH:
                return 1;
            case NORTH_EAST:
                return 2;
            case WEST:
                return 3;
            case EAST:
                return 4;
            case SOUTH_WEST:
                return 5;
            case SOUTH:
                return 6;
            case SOUTH_EAST:
                return 7;
            default:
                return -1;
        }
    }

    public static int direction(int dx, int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 5;
            } else if (dy > 0) {
                return 0;
            } else {
                return 3;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return 7;
            } else if (dy > 0) {
                return 2;
            } else {
                return 4;
            }
        } else {
            if (dy < 0) {
                return 6;
            } else if (dy > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public int getDirectionX() {
        return directionX;
    }
    public int getDirectionY() {
        return directionY;
    }

    public static Direction[] valid() {
        return VALID;
    }

}
