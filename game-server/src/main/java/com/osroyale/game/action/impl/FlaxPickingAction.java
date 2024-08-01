package com.osroyale.game.action.impl;

import com.osroyale.Config;
import com.osroyale.game.Animation;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.util.Utility;

/**
 * Handles picking up a flax.
 * @author Daniel
 */
public final class FlaxPickingAction extends Action<Player> {

    /** The flax game object. */
    private final GameObject object;

    /** The ticks. */
    private boolean pickup;

    /**
     * Constructs a new <code>FlaxPickingAction</code>.
     *
     * @param player The player instance.
     * @param object The flax game object.
     */
    public FlaxPickingAction(Player player, GameObject object) {
        super(player, 2, true);
        this.object = object;
    }

    @Override
    public void execute() {
        Player player = getMob().getPlayer();

        if (pickup) {
            player.inventory.add(new Item(1779, 1));
//            if (Utility.random(6) == 1) {
//                World.submit(new ObjectReplacementEvent(object, 20));
//            }
            cancel();
        } else {
            player.animate(new Animation(827));
            pickup = true;
            setDelay(1);
        }
    }

    @Override
    public String getName() {
        return "Flax picking";
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