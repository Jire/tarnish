package com.osroyale.game.world.entity.combat.effect;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.entity.mob.Mob;

/**
 * The {@link Task} implementation that provides processing for {@link
 * CombatEffect}s.
 *
 * @author lare96 <http://github.org/lare96>
 */
final class CombatEffectTask extends Task {

    /** The mob that this task is for. */
    private final Mob mob;

    /** The combat effect that is being processed. */
    private final CombatEffect effect;

    /**
     * Creates a new {@link CombatEffectTask}.
     *
     * @param mob    the mob that this task is for.
     * @param effect the combat effect that is being processed.
     */
    CombatEffectTask(Mob mob, CombatEffect effect) {
        super(false, effect.getDelay());
        super.attach(mob);
        this.mob = mob;
        this.effect = effect;
    }

    @Override
    public void execute() {
        if (effect.removeOn(mob) || !mob.isValid()) {
            cancel();
            return;
        }

        effect.process(mob);
    }

}
