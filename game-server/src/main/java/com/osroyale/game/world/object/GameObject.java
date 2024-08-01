package com.osroyale.game.world.object;

import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.generic.GenericAttributes;

/**
 * Represents a game object.
 *
 * @author Michael | Chex
 */
public interface GameObject extends Interactable {

    default int getInstancedHeight() {
        return Entity.DEFAULT_INSTANCE;
    }

    /** Gets the generic attributes. */
    GenericAttributes getGenericAttributes();

    /** Gets the object definition. */
    GameObjectDefinition getDefinition();

    /** Gets the regional location. */
    Position getPosition();

    /** Gets the width. */
    int width();

    /** Gets the length. */
    int length();

    int distance();

    /** Gets the object type. */
    ObjectType getObjectType();

    /** Gets the rotation. */
    ObjectDirection getDirection();

    /** Registers the game object. */
    void register();

    /** Unregisters the game object. */
    void unregister();

    /** Determines if this object is active on the world. */
    boolean active();

    /** Gets the object id. */
    default int getId() {
        return getDefinition().getId();
    }

    void transform(int id);

    void rotate(ObjectDirection direction);

}
