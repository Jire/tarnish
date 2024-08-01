package com.osroyale.game.world.entity.combat.hit;

import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * Holds variables to generate combat data of an actor.
 *
 * @author Michael | Chex
 */
public final class CombatData<T extends Mob> {

    /** The attacker. */
    private final T attacker;

    /** The defender. */
    private final Mob defender;

    /** The combat hit. */
    private final CombatHit hit;

    /** The combat strategy. */
    private final CombatStrategy<? super T> strategy;

    /** Whether or not this hit is the last one. */
    private final boolean lastHit;

    /** Constructs a new {@code CombatHitData} object. */
    public CombatData(T attacker, Mob defender, CombatHit hit, CombatStrategy<? super T> strategy, boolean lastHit) {
        this.attacker = attacker;
        this.defender = defender;
        this.hit = hit;
        this.strategy = strategy;
        this.lastHit = lastHit;
    }

    /** @return the attacker */
    public T getAttacker() {
        return attacker;
    }

    /** @return the defender */
    public Mob getDefender() {
        return defender;
    }

    /** @return the hit */
    public CombatHit getHit() {
        return hit;
    }

    /** @return the hit delay. */
    public int getHitDelay() {
        return hit.getHitDelay();
    }

    /** @return the hitsplat delay. */
    public int getHitsplatDelay() {
        return hit.getHitsplatDelay();
    }

    /** @return the combat strategy */
    public CombatStrategy<? super T> getStrategy() {
        return strategy;
    }

    /** @return {@code true} if this hit data is the last hit */
    public boolean isLastHit() {
        return lastHit;
    }

}
