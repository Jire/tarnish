package com.osroyale.game.world.object;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.Entity;
import com.osroyale.game.world.entity.EntityType;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendAddObject;
import com.osroyale.net.packet.out.SendRemoveObject;
import com.osroyale.util.generic.GenericAttributes;

import java.util.Objects;

import static com.osroyale.game.world.object.ObjectDirection.NORTH;
import static com.osroyale.game.world.object.ObjectDirection.SOUTH;

/**
 * Represents a static game object loaded from the map fs.
 *
 * @author Michael | Chex
 */
public class CustomGameObject extends Entity implements GameObject {

    /** The object definition. */
    private GameObjectDefinition definition;

    /** The object direction. */
    private ObjectDirection direction;

    /** The object type. */
    private ObjectType type;

    /** The generic attributes. */
    private GenericAttributes genericAttributes;

    public CustomGameObject(int id, int instance, Position position, ObjectDirection direction, ObjectType type) {
        super(position);
        this.definition = GameObjectDefinition.forId(id);
        this.instance = instance;
        this.direction = direction;
        this.type = type;
    }

    public CustomGameObject(int id, Position position, ObjectDirection direction, ObjectType type) {
        this(id, Mob.DEFAULT_INSTANCE, position, direction, type);
    }

    public CustomGameObject(int id, int instance, Position position) {
        this(id, instance, position, NORTH, ObjectType.GENERAL_PROP);
    }

    public CustomGameObject(int id, Position position) {
        this(id, Mob.DEFAULT_INSTANCE, position, NORTH, ObjectType.GENERAL_PROP);
    }

    @Override
    public int getInstancedHeight() {
        return instance;
    }

    @Override
    public GenericAttributes getGenericAttributes() {
        if (genericAttributes == null)
            genericAttributes = new GenericAttributes();
        return genericAttributes;
    }

    @Override
    public GameObjectDefinition getDefinition() {
        return definition;
    }

    @Override
    public int width() {
        if (direction == NORTH || direction == SOUTH) {
            return definition.getLength();
        }
        return definition.getWidth();
    }

    @Override
    public int length() {
        if (direction == NORTH || direction == SOUTH) {
            return definition.getWidth();
        }
        return definition.getLength();
    }

    @Override
    public int distance() {
        return definition.getDistance();
    }

    @Override
    public ObjectType getObjectType() {
        return type;
    }

    @Override
    public ObjectDirection getDirection() {
        return direction;
    }

    @Override
    public void register() {
        if (!isRegistered()) {
            Region region = getRegion();
            setRegistered(true);

            if (region == null) {
                setPosition(getPosition());
            } else if (!region.containsObject(getHeight(), this)) {
                addToRegion(region);
            }

            Region.ACTIVE_OBJECT.put(getPosition(), this);
        }
    }

    @Override
    public void unregister() {
        if (isRegistered()) {
            Region.ACTIVE_OBJECT.remove(getPosition(), this);
            removeFromRegion(getRegion());
            destroy();
        }
    }

    @Override
    public boolean active() {
        return isRegistered();
    }

    @Override
    public void addToRegion(Region objectRegion) {
        if (!objectRegion.containsObject(getHeight(), this)) {
            TraversalMap.markObject(objectRegion, this, true, true);
            for (Region region : World.getRegions().getSurroundingRegions(getPosition())) {
                for (Player other : region.getPlayers(getHeight())) {
                    if (other.instance != getInstancedHeight())
                        continue;
                    other.send(new SendAddObject(this));
                }
            }
        }
    }

    @Override
    public void removeFromRegion(Region objectRegion) {
        if (objectRegion.containsObject(getHeight(), this)) {
            TraversalMap.markObject(objectRegion, this, false, true);
            for (Region region : World.getRegions().getSurroundingRegions(getPosition())) {
                for (Player other : region.getPlayers(getHeight())) {
                    if (other.instance != getInstancedHeight()) {
                        continue;
                    }
                    other.send(new SendRemoveObject(this));
                }
            }
        }
    }

    @Override
    public void transform(int id) {
        unregister();
        definition = GameObjectDefinition.forId(id);
        register();
    }

    @Override
    public void rotate(ObjectDirection direction) {
        unregister();
        this.direction = direction;
        register();
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public EntityType getType() {
        return EntityType.CUSTOM_OBJECT;
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition.getId(), getPosition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomGameObject) {
            CustomGameObject other = (CustomGameObject) obj;
            return definition == other.definition && getPosition().equals(other.getPosition()) && getInstancedHeight() == other.getInstancedHeight();
        }
        return obj == this;
    }

    @Override
    public String toString() {
        return String.format("CustomGameObject[id=%s, loc=%s, width=%s, len=%s, rot=%s, type=%s]", getId(), getPosition(), width(), length(), getDirection(), getObjectType());
    }
}
