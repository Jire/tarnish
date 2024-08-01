package com.osroyale.game.world.entity.combat;

import com.osroyale.game.world.entity.combat.formula.MagicFormula;
import com.osroyale.game.world.entity.combat.formula.MeleeFormula;
import com.osroyale.game.world.entity.combat.formula.RangedFormula;
import com.osroyale.game.world.entity.mob.Mob;

public enum CombatType {
    MELEE(new MeleeFormula(), 0),
    RANGED(new RangedFormula(), 1),
    MAGIC(new MagicFormula(), 2);

    private final int hitsplatDelay;

    final FormulaModifier<Mob> formula;

    CombatType(FormulaModifier<Mob> formula, int hitsplatDelay) {
        this.formula = formula;
        this.hitsplatDelay = hitsplatDelay;
    }

    public FormulaModifier<Mob> getFormula() {
        return formula;
    }

    public int getHitsplatDelay() {
        return hitsplatDelay;
    }

    public boolean match(final CombatType type) {
        return equals(type);
    }

    public boolean match(final CombatType... types) {
        for (final CombatType type : types) {
            if (match(type)) {
                return true;
            }
        }
        return false;
    }

}
