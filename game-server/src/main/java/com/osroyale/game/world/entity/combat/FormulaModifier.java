package com.osroyale.game.world.entity.combat;

import com.osroyale.game.world.entity.mob.Mob;

public interface FormulaModifier<T extends Mob> {

    default int modifyAttackLevel(T attacker, Mob defender, int level) {
        return level;
    }

    default int modifyStrengthLevel(T attacker, Mob defender, int level) {
        return level;
    }

    default int modifyDefenceLevel(Mob attacker, T defender, int level) {
        return level;
    }

    default int modifyRangedLevel(T attacker, Mob defender, int level) {
        return level;
    }

    default int modifyMagicLevel(T attacker, Mob defender, int level) {
        return level;
    }

    default int modifyAccuracy(T attacker, Mob defender, int roll) {
        return roll;
    }

    default int modifyAggressive(T attacker, Mob defender, int roll) {
        return roll;
    }

    default int modifyDefensive(Mob attacker, T defender, int roll) {
        return roll;
    }

    default int modifyDamage(T attacker, Mob defender, int damage) {
        return damage;
    }

    default int modifyOffensiveBonus(T attacker, Mob defender, int bonus) {
        return bonus;
    }

    default int modifyAggressiveBonus(T attacker, Mob defender, int bonus) {
        return bonus;
    }

    default int modifyDefensiveBonus(Mob attacker, T defender, int bonus) {
        return bonus;
    }

}
