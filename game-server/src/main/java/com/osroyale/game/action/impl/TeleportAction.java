package com.osroyale.game.action.impl;

import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.ConsecutiveAction;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;

/**
 * Teleports an entity to another part of the world.
 *
 * @author Daniel
 * @author Michael | Chex
 */
public final class TeleportAction extends ConsecutiveAction<Mob> {

    /** The teleport position. */
    private final Position position;

    /** The teleportType */
    private final TeleportationData type;

    /** The destination randomevent */
    private final Runnable onDestination;

    /** Constructs a new {@code TeleportAction} object. */
    public TeleportAction(Mob entity, Position position, TeleportationData type, Runnable onDestination) {
        super(entity);
        this.position = position;
        this.type = type;
        this.onDestination = onDestination;
    }

    @Override
    public void onSchedule() {
        if (getMob().isPlayer() && getMob().getPlayer().isTeleblocked()) {
            cancel();
            getMob().getPlayer().message("You are currently under the affects of a teleblock spell and can not teleport!");
            return;
        }

        if (!valid(this)) {
            cancel();
        } else {
            init();
        }
    }

    private void init() {
        add(this::start);
        add(this::move);
        if (type != TeleportationData.TABLET)
            add(this::end);
        add(this::reset);
    }

    private boolean valid(Action<Mob> action) {
        if (type == TeleportationData.HOME && action.getMob().getCombat().inCombat()) {
            if (action.getMob().isPlayer()) {
                action.getMob().getPlayer().send(new SendMessage("You can't teleport home while in combat!"));
            }
            cancel();
            return false;
        }
        return true;
    }

    private void start(Action<Mob> action) {
        if (type != TeleportationData.HOME)
            action.getMob().inTeleport = true;

        action.setDelay(type.getDelay());
        action.getMob().movement.reset();
        type.getStartAnimation().ifPresent(action.getMob()::animate);
        type.getStartGraphic().ifPresent(action.getMob()::graphic);

        if (type.lockMovement()) {
            action.getMob().locking.lock();
        }

        if (type == TeleportationData.TABLET) {
            addFirst(this::startTablet);
        }
    }

    private void startTablet(Action<Mob> action) {
        action.setDelay(type.getDelay());
        type.getEndAnimation().ifPresent(action.getMob()::animate);
    }

    private void move(Action<Mob> action) {
        if (valid(action)) {
            action.setDelay(1);
            action.getMob().move(position);
            action.getMob().inTeleport = true;
            action.getMob().teleporting = true;

            if (action.getMob().isPlayer()) {
                action.getMob().getPlayer().send(new SendString("[CLOSE_MENU]", 0));
            }
        } else {
            reset(action);
            onDestination.run();
        }
    }

    private void end(Action<Mob> action) {
        type.getEndGraphic().ifPresent(action.getMob()::graphic);
        type.getEndAnimation().ifPresent(action.getMob()::animate);

        if (type.getEndAnimation().isPresent() || type.getEndGraphic().isPresent()) {
            action.setDelay(type.getDelay());
        }

        onDestination.run();
        action.getMob().onStep();

        //This is for overrides - updates appearance after teleport to account for duel arena
        action.getMob().updateFlags.add(UpdateFlag.APPEARANCE);
    }

    private void reset(Action<Mob> action) {
        action.getMob().inTeleport = false;
        cancel();
    }

    @Override
    public void onCancel(boolean logout) {
        getMob().animate(Animation.RESET, true);
        getMob().graphic(Graphic.RESET, true);
        getMob().inTeleport = false;
        getMob().teleporting = false;

        if (type == TeleportationData.TABLET) {
            getMob().getPlayer().pvpInstance = false;
            getMob().instance = Mob.DEFAULT_INSTANCE;
            if (getMob().getPlayer().pet != null) {
                getMob().getPlayer().pet.instance = getMob().instance;
            }
        }
    }

    @Override
    public String getName() {
        return "teleport_action";
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
