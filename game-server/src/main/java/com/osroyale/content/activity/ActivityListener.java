package com.osroyale.content.activity;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.listener.CombatListener;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * A combat listener that is applied to all mobs added to (or removed from) the
 * activity.
 *
 * @param <T> the activity type
 */
public abstract class ActivityListener<T extends Activity> implements CombatListener<Mob> {

    /** The parent activity. */
    protected final T activity;

    /**
     * Constructs a new {@code ActivityListener} for a activity.
     *
     * @param activity the parent activity
     */
    public ActivityListener(T activity) {
        this.activity = activity;
    }

    @Override
    public boolean withinDistance(Mob attacker, Mob defender) {
        return true;
    }

    @Override
    public boolean canAttack(Mob attacker, Mob defender) {
        return true;
    }

    @Override
    public boolean canOtherAttack(Mob attacker, Mob defender) {
        return true;
    }

    @Override
    public void onDamage(Mob defender, Hit hit) {
    }

    @Override
    public void start(Mob attacker, Mob defender, Hit[] hits) {
    }

    @Override
    public void attack(Mob attacker, Mob defender, Hit hit) {
    }

    @Override
    public void hit(Mob attacker, Mob defender, Hit hit) {
    }

    @Override
    public void block(Mob attacker, Mob defender, Hit hit, CombatType combatType) {
    }

    @Override
    public void preDeath(Mob attacker, Mob defender, Hit hit) {
    }

    @Override
    public void onDeath(Mob attacker, Mob defender, Hit hit) {
    }

    @Override
    public void hitsplat(Mob attacker, Mob defender, Hit hit) {
    }

    @Override
    public void finishOutgoing(Mob attacker, Mob defender) {
    }

    @Override
    public void finishIncoming(Mob attacker, Mob defender) {
    }

    @Override
    public void preKill(Mob attacker, Mob defender, Hit hit) {
    }

    @Override
    public void onKill(Mob attacker, Mob defender, Hit hit) {
    }
}
