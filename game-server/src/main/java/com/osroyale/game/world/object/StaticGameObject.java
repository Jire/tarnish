package com.osroyale.game.world.object;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendAddObject;
import com.osroyale.net.packet.out.SendRemoveObject;
import com.osroyale.util.Utility;
import com.osroyale.util.generic.GenericAttributes;

import java.util.Objects;

import static com.osroyale.game.world.object.ObjectDirection.*;

/**
 * Represents a static game object loaded from the map fs.
 *
 * @author Michael | Chex
 */
public class StaticGameObject implements GameObject {

    /** The object definition. */
    private GameObjectDefinition definition;

    /** The generic attributes. */
    private GenericAttributes genericAttributes;

    /** The object position coordinates. */
    private final Position position;

    /** The object type. */
    private ObjectType type;

    /** A byte holding the object rotation. */
    private ObjectDirection direction;

    /** Creates the game object. */
    public StaticGameObject(GameObjectDefinition definition, Position position, ObjectType type, ObjectDirection direction) {
        this.definition = definition;
        this.position = position;
        this.type = type;
        this.direction = direction;
    }

    @Override
    public GenericAttributes getGenericAttributes() {
        if (genericAttributes == null) {
            genericAttributes = new GenericAttributes();
        }
        return genericAttributes;
    }

    @Override
    public GameObjectDefinition getDefinition() {
        return definition;
    }

    @Override
    public Position getPosition() {
        return position;
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
        Region objectRegion = getPosition().getRegion();
        if (!objectRegion.containsObject(getHeight(), this)) {
            TraversalMap.markObject(objectRegion, this, true, true);
            for (Region region : World.getRegions().getSurroundingRegions(getPosition())) {
                for (Player other : region.getPlayers(getHeight())) {
                    if (other.instance != getInstancedHeight())
                        continue;
                    if (Utility.withinViewingDistance(this, other, Region.VIEW_DISTANCE)) {
                        other.send(new SendAddObject(this));
                    }
                }
            }
        }
    }

    @Override
    public void unregister() {
        Region objectRegion = getPosition().getRegion();
        if (objectRegion.containsObject(getHeight(), this)) {
            TraversalMap.markObject(objectRegion, this, false, true);
            for (Region region : World.getRegions().getSurroundingRegions(getPosition())) {
                for (Player other : region.getPlayers(getHeight())) {
                    if (other.instance != getInstancedHeight())
                        continue;
                    if (Utility.withinViewingDistance(this, other, Region.VIEW_DISTANCE)) {
                        other.send(new SendRemoveObject(this));
                    }
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
    public boolean active() {
        return getPosition().getRegion().containsObject(getHeight(), this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition.getId(), position);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof StaticGameObject) {
            StaticGameObject other = (StaticGameObject) obj;
            return definition == other.definition && position.equals(other.position);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("StaticGameObject[id=%s, loc=%s, width=%s, len=%s, rot=%s, type=%s]", getId(), getPosition(), width(), length(), getDirection(), getObjectType());
    }
}
