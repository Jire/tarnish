package com.osroyale.content.skill.impl.hunter.trap;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;

public class BoxTrap extends Trap {

    private TrapState state;

    public BoxTrap(GameObject obj, TrapState state, int ticks, Player p) {
        super(obj, state, ticks, p);
    }

    public TrapState getState() {
        return state;
    }

    public void setState(TrapState state) {
        this.state = state;
    }
}
