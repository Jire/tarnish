package com.osroyale.game.action.impl;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * Handles performing an emote action
 *
 * @author Daniel
 */
public final class EmoteAction extends Action<Player> {
    /**
     * The animation identification.
     */
    private final int animation;

    /**
     * The graphic identification.
     */
    private final int graphic;

    /**
     * Constructs a new <code>EmoteAction</code>.
     *
     * @param player    The player instance.
     * @param animation The animation identification.
     * @param graphic   The graphic identification.
     */
    public EmoteAction(Player player, int animation, int graphic) {
        super(player, 3, true);
        this.animation = animation;
        this.graphic = graphic;
    }

    @Override
    public void execute() {
        if (animation != -1) {
            getMob().animate(new Animation(animation));
        }

        if (graphic != -1) {
            getMob().graphic(new Graphic(graphic));
        }

        cancel();
    }

    @Override
    public String getName() {
        return "Emote";
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