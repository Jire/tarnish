package com.osroyale.content.skill.impl.hunter.trap;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;

public class Trap {

    /** The possible states a trap can be in */
    public enum TrapState {
        SET, CAUGHT
    }

    /** The WorldObject linked to this HunterObject */
    private GameObject gameObject;

    /** The amount of ticks this object should stay for */
    private int ticks;

    /** This trap's state */
    private TrapState trapState;

    /** Reconstructs a new Trap */
    Trap(GameObject object, TrapState state, int ticks, Player owner) {
        this.gameObject = object;
        this.trapState = state;
        this.ticks = ticks;
        this.player = owner;
    }

    /** Gets the GameObject */
    GameObject getGameObject() {
        return gameObject;
    }

    /** @return the ticks */
    public int getTicks() {
        return ticks;
    }

    /** Gets a trap's state */
    TrapState getTrapState() {
        return trapState;
    }

    /**  the ticks to set */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    private Player player;

    public Player getOwner() {
        return player;
    }

    public void setOwner(Player player) {
        this.player = player;
    }
}
