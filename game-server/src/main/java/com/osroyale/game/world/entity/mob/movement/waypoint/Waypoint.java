package com.osroyale.game.world.entity.mob.movement.waypoint;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.object.GameObjectDefinition;
import com.osroyale.game.world.pathfinding.TraversalMap;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Objects;

public abstract class Waypoint extends Task {
    protected final Mob mob;
    protected Interactable target;
    private Position lastPosition;

    protected Waypoint(Mob mob, Interactable target) {
        super(true, 0);
        this.mob = mob;
        this.target = target;
    }

    protected abstract void onDestination();

    protected int getRadius() {
        return 1;
    }

    protected boolean withinDistance() {
        if (target instanceof final GameObject object) {
            Position position = mob.getPosition();
            int x = position.getX();
            int y = position.getY();

            GameObjectDefinition definition = object.getDefinition();
            int walkingFlag = definition.getWalkingFlag();
            int sizeX = definition.getWidth();
            int sizeY = definition.getLength();
            int rotation = object.getDirection().getId();
            if (rotation % 2 != 0) {
                sizeX = definition.getLength();
                sizeY = definition.getWidth();
            }
            if (rotation != 0) {
                walkingFlag = (walkingFlag << rotation & 0xF) + (walkingFlag >> 4 - rotation);
            }
            if (sizeX != 0 && sizeY != 0) {
                Position targetPosition = target.getPosition();
                int destX = targetPosition.getX();
                int destY = targetPosition.getY();
                int height = targetPosition.getHeight();

                int flag = TraversalMap.getFlags(destX, destY, height);

                int cornerX = destX + sizeX - 1;
                int cornerY = destY + sizeY - 1;

                if (destX <= x && cornerX >= x && y >= destY && y <= cornerY) {
                    return true;
                }
                if (x == destX - 1 && destY <= y && y <= cornerY && (0x8 & flag) == 0 && (0x8 & walkingFlag) == 0) {
                    return true;
                }
                if (x == cornerX + 1 && destY <= y && cornerY >= y && (flag & 0x80) == 0 && (0x2 & walkingFlag) == 0) {
                    return true;
                }
                if (y == destY - 1 && destX <= x && cornerX >= x && (0x2 & flag) == 0 && (0x4 & walkingFlag) == 0) {
                    return true;
                }
                if (y == cornerY + 1 && destX <= x && cornerX >= x && (flag & 0x20) == 0 && (0x1 & walkingFlag) == 0) {
                    return true;
                }
            }
        }
        return Utility.getDistance(mob, target) <= getRadius() && !mob.movement.needsPlacement();
    }

    @Override
    public void onSchedule() {
        if (mob.locking.locked(PacketType.MOVEMENT)) {
            return;
        }

        if (target instanceof Mob) {
            mob.interact((Mob) target);
        }

        if (!withinDistance()) {
            findRoute();
        }
    }

    @Override
    public void execute() {
        if (target instanceof Mob && Utility.inside(mob, target)) {
            if (!mob.locking.locked(PacketType.MOVEMENT)
                    && !mob.movement.needsPlacement()) {
                Mob targetMob = (Mob) target;
                if (targetMob.hasPriorityIndex(mob)
                        || (!targetMob.isFixingInside() && !targetMob.movement.needsPlacement())) {
                    Utility.fixInsidePosition(mob, target);
                }
            }
            return;
        } else {
            mob.setFixingInside(false);
        }

        if (withinDistance()) {
            onDestination();
            return;
        }

        if (target instanceof GameObject gameObject && mob.isPlayer()) {
            if (gameObject.distance() > 1 && withinDistance(mob.getPosition().getX(), mob.getPosition().getY(), target.getPosition().getX(), target.getPosition().getY(), target.width(), gameObject.length(), gameObject.distance())) {
                onDestination();
                return;
            }
        }

        if (target.getPosition().equals(lastPosition)) {
            return;
        }

        if (mob.locking.locked(PacketType.MOVEMENT)) {
            return;
        }

        lastPosition = target.getPosition();
        findRoute();
    }

    private void findRoute() {
        if (target instanceof Player && mob.equals(((Player) target).pet)) {
            int distance = Utility.getDistance(mob, target);
            if (distance > Region.VIEW_DISTANCE) {
                Npc pet = mob.getNpc();
                pet.move(target.getPosition());
                pet.instance = ((Player) target).instance;
                World.schedule(1, () -> {
                    pet.interact((Player) target);
                    pet.follow((Player) target);
                });
            }
        }

//        if (this instanceof CombatWaypoint) {
//            System.out.println(mob.getPosition());
//            System.out.println(target.getPosition());
//            System.out.println(Utility.getDelta(mob, target));
//            System.out.println();
//        }

        boolean smart = mob.isPlayer() || (mob.isNpc() && !(this instanceof CombatWaypoint));

        if (smart && mob.movement.dijkstraPath(target)) {
            return;
        }

        if (mob.movement.simplePath(target)) {
            return;
        }

        if (mob.isPlayer())
            mob.getPlayer().send(new SendMessage("I can't reach that!"));

        /* No path can be found, lets get out of here!!!! */
        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        mob.resetFace();

        if (target instanceof Mob other) {
            other.attributes.remove("mob-following");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Waypoint other) {
            return Objects.equals(mob, other.mob)
                    && Objects.equals(target, other.target);
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "target=" + target +
                '}';
    }

    public Interactable getTarget() {
        return target;
    }

    public void onChange() {
        execute();
//        mob.movement.processNextMovement();
    }

    boolean withinDistance(int x, int y, int x2, int y2, int width, int height, int distance) {
        if (x > x2) {
            x2 += Math.min(width, x - x2) - 1;
        }
        if (y > y2) {
            y2 += Math.min(height, y - y2) - 1;
        }
        return Math.abs(x - x2) <= distance && Math.abs(y - y2) <= distance;
    }
}
