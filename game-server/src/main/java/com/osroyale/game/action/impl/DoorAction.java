package com.osroyale.game.action.impl;

import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.function.Predicate;

/**
 * Handles going through a door. (Cheap fix doesn't actually open the door)
 *
 * @author Daniel
 */
public final class DoorAction extends Action<Player> {
    private int count;
    private final GameObject door;
    private final Position position;
    private final Predicate<Player> condition;
    private final String message;
    private final Direction face;

    public DoorAction(Player player, GameObject door, Position destination, Direction face) {
        this(player, door, destination, face, null, null);
    }

    public DoorAction(Player player, GameObject door, Position destination, Direction face, Predicate<Player> condition, String message) {
        super(player, 1, true);
        this.count = 0;
        this.door = door;
        this.position = destination;
        this.face = face;
        this.condition = condition;
        this.message = message;
    }

    @Override
    public boolean canSchedule() {
        if (condition != null && !condition.test(getMob())) {
            getMob().send(new SendMessage(message, MessageColor.RED));
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        if (!Utility.within(getMob().getPosition(), door.getPosition(), 1)) {
            if (!getMob().movement.isMoving())
                getMob().walk(door.getPosition());
            return;
        }

        if (getMob().getCombat().inCombat())
            getMob().getCombat().reset();

        if (count == 0) {
            getMob().face(face);
            getMob().locking.lock();
        } else if (count == 1) {
            getMob().move(position);
            getMob().locking.unlock();
            getMob().face(Direction.getOppositeDirection(face));
            cancel();
        }
        count++;
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().locking.unlock();
    }

    @Override
    public String getName() {
        return "Open door";
    }

    @Override
    public boolean prioritized() {
        return false;
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }
}