package com.osroyale.game.world.entity;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;

/**
 * Represents a character in the game world, i.e. a {@code Player} or an
 * {@code NPC}.
 *
 * @author Michael | Chex
 */
public abstract class Entity implements Interactable {
    public static final int DEFAULT_INSTANCE = 0;

    private int index;
    private int width;
    private int length;
    private boolean registered;
    private boolean visible;
    private Position position;
    private Region currentRegion;
    public int instance = DEFAULT_INSTANCE;

    public Entity(Position position) {
       this(position, true);
    }

    public Entity(Position position, boolean visible) {
        setPosition(position);
        setVisible(visible);
        this.width = 1;
        this.length = 1;
    }

    public int getIndex() {
        return index;
    }

    public Position getPosition() {
        return position;
    }

    public Position getCenterPosition() {
        final int width = width();
        final int length = length();
        final Position position = getPosition();
        return width == 1 && length == 1 ? position
                : new Position(position.getCoordFaceX(width), position.getCoordFaceY(getHeight()), position.getHeight());
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int length() {
        return length;
    }

    public final boolean isRegistered() {
        return registered;
    }

    public boolean isVisible() {
        return visible;
    }

    public Region getRegion() {
        return currentRegion;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getHeight() {
        return position.getHeight();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    protected void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPosition(Position position) {
        Region region = position.getRegion();

        if (!registered || (region == currentRegion && position.getHeight() == getHeight())) {
            this.position = position;
            return;
        }

        if (currentRegion != null) {
            removeFromRegion(currentRegion);
        }

        this.position = position;
        addToRegion(currentRegion = region);
    }

    public void setX(int x) {
        position = new Position(x, getY(), getHeight());
    }

    public void setY(int y) {
        position = new Position(getX(), y, getHeight());
    }

    public void setZ(int z) {
        position = new Position(getX(), getY(), z);
    }

    public boolean is(EntityType type) {
        return getType() == type;
    }

    /**
     * Destroys this entity.
     *
     * @return This entity.
     */
    protected Entity destroy() {
        removeFromRegion(currentRegion);
        setRegistered(false);
        return this;
    }

    /**
     * Validates this npc based on its current region and registered state.
     *
     * @return {@code True} if this npc is a valid log in the game world.
     */
    public boolean isValid() {
        return isRegistered() && currentRegion != null;

    }

    /** Registers an entity to the {@code World}. */
    public abstract void register();

    /** Unregisters an entity from the {@code World}. */
    public abstract void unregister();

    /**
     * Adds this entity to the specified region.
     *
     * @param region The region.
     */
    public abstract void addToRegion(Region region);

    /**
     * Removes this entity from the specified region.
     *
     * @param region The region.
     */
    public abstract void removeFromRegion(Region region);

    public void onStep() {
    }

    /**
     * Gets the name of this entity.
     *
     * @return The entity's name.
     */
    public abstract String getName();

    /**
     * Gets the {@code EntityType}.
     *
     * @return The {@code EntityType}.
     */
    public abstract EntityType getType();


    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return String.format("Entity[registered=%s, visible=%s, position=%s, type=%s", isRegistered(), isVisible(), getPosition(), getType());
    }

    /** Check if an entity is an object */
    public final boolean isStaticObject() {
        return getType() == EntityType.STATIC_OBJECT;
    }

    /** Check if an entity is an object */
    public final boolean isCustomObject() {
        return getType() == EntityType.CUSTOM_OBJECT;
    }

}