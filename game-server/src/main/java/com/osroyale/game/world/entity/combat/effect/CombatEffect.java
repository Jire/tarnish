package com.osroyale.game.world.entity.combat.effect;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Some sort of temporary effect applied to a {@link Mob} during
 * combat. Combat effects include but are not limited to; being poisoned,
 * skulled, and teleblocked.
 *
 * @author lare96 <http://github.org/lare96>
 */
public abstract class CombatEffect {

    /** The map of all of the combat effect types mapped to their respective listeners. */
    public static final Map<CombatEffectType, CombatEffect> EFFECTS = new HashMap<>();

    /** The delay for this individual combat effect. */
    private final int delay;

    /** Creates a new {@link CombatEffect}. */
    public CombatEffect(int delay) {
        this.delay = delay;
    }

    /** Starts this combat effect by scheduling a task utilizing the abstract methods in this class. */
    public final boolean start(Mob mob) {
        if (apply(mob)) {
            World.schedule(new CombatEffectTask(mob, this));
            return true;
        }
        return false;
    }

    /** Applies this effect to {@code mob}. */
    public abstract boolean apply(Mob mob);

    /** Removes this effect from {@code mob} if needed. */
    public abstract boolean removeOn(Mob mob);

    /** Provides processing for this effect on {@code mob}. */
    public abstract void process(Mob mob);

    /** Executed on login, primarily used to re-apply the effect to {@code mob}. */
    public abstract boolean onLogin(Mob mob);

    /** Gets the delay for this individual combat effect. */
    protected final int getDelay() {
        return delay;
    }

    /** Returns an unmodifiable view of the combat effect listeners. */
    public static Collection<CombatEffect> values() {
        return Collections.unmodifiableCollection(EFFECTS.values());
    }
}
