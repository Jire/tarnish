package com.osroyale.game.world.position;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.region.Region;
import com.osroyale.util.Utility;

/**
 * Represents a single tile on the game world.
 *
 * @author Graham Edgecombe
 */
public class Position {

    /** The maximum amount of height-planes. */
    public static final int HEIGHT_LEVELS = 4;

    /** The x coordinate. */
    private final int x;

    /** The y coordinate. */
    private final int y;

    /** The height coordinate. */
    private final int height;

    /** Creates a location with a default height of 0. */
    public Position(int x, int y) {
        this(x, y, 0);
    }

    /**  Creates a location. */
    public Position(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height < 0 ? 0 : height % 4;
    }

    /**  Gets the absolute x coordinate. */
    public int getX() {
        return x;
    }

    /** Gets the absolute y coordinate.   */
    public int getY() {
        return y;
    }

    /** Gets the height coordinate, or height.   */
    public int getHeight() {
        return height;
    }

    /** Gets the local x coordinate relative to this region. */
    public int getLocalX() {
        return getLocalX(this);
    }

    /** Gets the local y coordinate relative to this region. */
    public int getLocalY() {
        return getLocalY(this);
    }

    /** Gets the local x coordinate relative to a specific region. */
    public int getLocalX(Position origin) {
        return x - 8 * origin.getChunkX();
    }

    /** Gets the local y coordinate relative to a specific region. */
    public int getLocalY(Position origin) {
        return y - 8 * origin.getChunkY();
    }

    /**  Gets the chunk x coordinate.  */
    public int getChunkX() {
        return (x >> 3) - 6;
    }

    /** Gets the chunk y coordinate.  */
    public int getChunkY() {
        return (y >> 3) - 6;
    }

    public int getRegionX() {
        return getChunkX() / 8;
    }

    public int getRegionY() {
        return getChunkY() / 8;
    }

    public Region getRegion() {
        return World.getRegions().getRegion(this);
    }

    public Position north() {
        return transform(0, 1);
    }

    public Position east() {
        return transform(1, 0);
    }

    public Position south() {
        return transform(0, -1);
    }

    public Position west() {
        return transform(-1, 0);
    }

    public Position northEast() {
        return transform(1, 1);
    }

    public Position northWest() {
        return transform(-1, 1);
    }

    public Position southEast() {
        return transform(1, -1);
    }

    public Position southWest() {
        return transform(-1, -1);
    }

    /**
     * Gets the manhattan distance between two interactable object.
     *
     * @param origin The originating location.
     * @param target The target location.
     * @return The distance between the origin and target.
     */
    public static int getManhattanDistance(Interactable origin, Interactable target) {
        return Utility.getDistance(origin, target);
    }

    /**
     * Checks if this location is within range of another.
     *
     * @param other  The other location.
     * @param radius The radius from the origin point.
     * @return {@code True} if the location is within the radius.
     */
    public boolean isWithinDistance(Position other, int radius) {
        if (height != other.height) {
            return false;
        }

        final int deltaX = Math.abs(other.x - x);
        final int deltaY = Math.abs(other.y - y);
        return deltaX <= radius && deltaY <= radius;
    }

    /**
     * Gets the distance between this location and another location.
     *
     * @param other The other location.
     * @return The distance between the two locations.
     */
    public double getDistance(Position other) {
        if (height != other.height) return 0;
        int dx = other.x - x;
        int dy = other.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Absolute distance between this Coordiante and another.
     *
     * @param other The other Coordiante.
     * @return The distance between the 2 Coordinates.
     */
    public int getDistances(Position other) {
        return (int) Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(height - other.height, 2));
    }

    public int getChevDistance(Position other) {
        return Math.max(Math.abs(other.getX() - getX()), Math.abs(other.getY() - getY()));
    }

    /**
     * Gets the Euclidean (straight-line) distance between two {@link Position}
     * s.
     *
     * @return The distance in tiles between the two locations.
     */
    public static double getDistance(Position first, Position second) {
        final int dx = second.getX() - first.getX();
        final int dy = second.getY() - first.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Gets the distance between this location and another location without
     * diagonals.
     *
     * @param other The other location.
     * @return The distance between the two locations.
     */
    public int getManhattanDistance(Position other) { //dont use this, it calcs wrong btw
        if (other == null || height != other.height) return Integer.MAX_VALUE;
        int dx = Math.abs(other.x - x);
        int dy = Math.abs(other.y - y);
        return dx + dy;
    }

    public boolean isViewableFrom(Position other) {
        if (this.getHeight() != other.getHeight())
            return false;
        Position p = this.getDelta(this, other);
        return p.x <= 14 && p.x >= -15 && p.y <= 14 &&
                p.y >= -15;
    }

    public Position getDelta(Position location, Position other) {
        return new Position(other.x - location.x, other.y - location.y, other.getHeight() - location.getHeight());
    }

    /**
     * Gets the Euclidean (straight-line) distance between two {@link Position}
     * s.
     *
     * @return The distance in tiles between the two locations.
     */
    public static int getManhattanDistance(Position first, Position second) {
        final int dx = Math.abs(second.getX() - first.getX());
        final int dy = Math.abs(second.getY() - first.getY());
        return dx + dy;
    }

    /**
     * Gets the longest horizontal or vertical delta between the two positions.
     *
     * @param other The other position.
     * @return The longest horizontal or vertical delta.
     */
    public int getLongestDelta(Position other) {
        if (height != other.height) return 0;
        int deltaX = Math.abs(getX() - other.getX());
        int deltaY = Math.abs(getY() - other.getY());
        return Math.max(deltaX, deltaY);
    }

    /**
     * Creates a location.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The height coordinate.
     * @return The location.
     */
    public static Position create(int x, int y, int z) {
        return new Position(x, y, z);
    }

    /**
     * Creates a location.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The location.
     */
    public static Position create(int x, int y) {
        return new Position(x, y);
    }

    /**
     * Creates a new location based on this location.
     *
     * @param diffX X difference.
     * @param diffY Y difference.
     * @param diffZ Z difference.
     * @return The new location.
     */
    public Position transform(int diffX, int diffY, int diffZ) {
        return new Position(x + diffX, y + diffY, height + diffZ);
    }

    /**
     * Creates a new location based on this location.
     *
     * @param diffX X difference.
     * @param diffY Y difference.
     * @return The new location.
     */
    public Position transform(int diffX, int diffY) {
        return new Position(x + diffX, y + diffY, height);
    }


    public boolean inLocation(Position southWest, Position northEast, boolean inclusive) {
        return !inclusive ? this.x > southWest.getX() && this.x < northEast.getX() && this.y > southWest.getY() && this.y < northEast.getY() : this.x >= southWest.getX() && this.x <= northEast.getX() && this.y >= southWest.getY() && this.y <= northEast.getY();
    }

    /**
     * Creates a new location based on this location.
     *
     * @param other The difference.
     * @return The new location.
     */
    public Position transform(Position other) {
        if (other == null) return this;
        return transform(other.getX(), other.getY(), other.getHeight());
    }

    /**
     * Creates a deep copy of this location.
     *
     * @return A deep copy of this location.
     */
    public Position copy() {
        return new Position(x, y, height);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (obj instanceof Position) {
            Position other = (Position) obj;
            return x == other.x && y == other.y && height == other.height;
        }

        return false;
    }

    public boolean matches(int x, int y) {
        return this.x == x && this.y == y;
    }

    public boolean matches(int x, int y, int z) {
        return this.x == x && this.y == y && this.height == z;
    }

    @Override
    public int hashCode() {
        return hash(x, y, height);
    }

    @Override
    public String toString() {
        return String.format("pos[x=%d, y=%d, z=%d]", x, y, height);
    }

    public static int hash(int x, int y, int z) {
        return (y << 16) | (x << 8) | z;
    }

    public static boolean isWithinDiagonalDistance(Mob attacker, Mob defender, int distance) {
        int attackerSize = 1;
        int defenderSize = 1;

        if(attacker.isNpc())
            attackerSize = attacker.getNpc().definition.getSize();
        if(defender.isNpc())
            defenderSize = defender.getNpc().definition.getSize();

        int e_offset_x = attackerSize - 1 + distance;
        int e_offset_y = attackerSize - 1 + distance;

        int o_offset_x = defenderSize - 1 + distance;
        int o_offset_y = defenderSize - 1 + distance;

        Position entity_pos = attacker.getPosition().copy();
        Position other_pos = defender.getPosition().copy();

        boolean inside_entity =
                (other_pos.getX() <= entity_pos.getX() + e_offset_x && other_pos.getX() >= (entity_pos.getX() - distance)) &&
                        (other_pos.getY() <= entity_pos.getY() + e_offset_y && other_pos.getY() >= (entity_pos.getY() - distance));

        boolean inside_other =
                (entity_pos.getX() <= other_pos.getX() + o_offset_x && entity_pos.getX() >= (other_pos.getX() - distance)) &&
                        (entity_pos.getY() <= other_pos.getY() + o_offset_y && entity_pos.getY() >= (other_pos.getY() - distance));


        return inside_entity || inside_other;
    }

    public int getChebyshevDistance(Position other) {
        return getChebyshevDistance(getX(), getY(), other.getX(), other.getY());
    }

    public int getChebyshevDistance(int x1, int y1, int x2, int y2) {
        return Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public int getCoordFaceX(final int sizeX) {
        return getCoordFaceX(sizeX, -1, -1);
    }

    public int getCoordFaceX(final int sizeX, final int sizeY, final int rotation) {
        return getX() + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
    }

    public int getCoordFaceY(final int sizeY) {
        return getCoordFaceY(-1, sizeY, -1);
    }

    public int getCoordFaceY(final int sizeX, final int sizeY, final int rotation) {
        return getY() + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
    }

}
