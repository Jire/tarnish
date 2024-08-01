package com.osroyale.game.world.entity.combat.formula;

import com.osroyale.game.world.entity.combat.FormulaModifier;
import com.osroyale.game.world.entity.mob.Mob;

import java.util.Deque;
import java.util.LinkedList;

public class CombatFormula<T extends Mob> implements FormulaModifier<T> {
    private final Deque<FormulaModifier<? super T>> modifiers = new LinkedList<>();

    public void addFirst(FormulaModifier<? super T> modifier) {
        modifiers.addFirst(modifier);
    }

    public void removeFirst() {
        modifiers.removeFirst();
    }

    public boolean add(FormulaModifier<? super T> modifier) {
        return !modifiers.contains(modifier) && modifiers.add(modifier);
    }

    public boolean remove(FormulaModifier<? super T> modifier) {
        return modifiers.remove(modifier);
    }

    @Override
    public int modifyAttackLevel(T attacker, Mob defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyAttackLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyStrengthLevel(T attacker, Mob defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyStrengthLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyDefenceLevel(Mob attacker, T defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyDefenceLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyRangedLevel(T attacker, Mob defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyRangedLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyMagicLevel(T attacker, Mob defender, int level) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            level = modifier.modifyMagicLevel(attacker, defender, level);
        }
        return level;
    }

    @Override
    public int modifyAccuracy(T attacker, Mob defender, int roll) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            roll = modifier.modifyAccuracy(attacker, defender, roll);
        }
        return roll;
    }

    @Override
    public int modifyAggressive(T attacker, Mob defender, int roll) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            roll = modifier.modifyAggressive(attacker, defender, roll);
        }
        return roll;
    }

    @Override
    public int modifyDefensive(Mob attacker, T defender, int roll) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            roll = modifier.modifyDefensive(attacker, defender, roll);
        }
        return roll;
    }

    @Override
    public int modifyDamage(T attacker, Mob defender, int damage) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            damage = modifier.modifyDamage(attacker, defender, damage);
        }
        return damage;
    }

    @Override
    public int modifyOffensiveBonus(T attacker, Mob defender, int bonus) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            bonus = modifier.modifyOffensiveBonus(attacker, defender, bonus);
        }
        return bonus;
    }

    @Override
    public int modifyAggressiveBonus(T attacker, Mob defender, int bonus) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            bonus = modifier.modifyAggressiveBonus(attacker, defender, bonus);
        }
        return bonus;
    }

    @Override
    public int modifyDefensiveBonus(Mob attacker, T defender, int bonus) {
        for (FormulaModifier<? super T> modifier : modifiers) {
            bonus = modifier.modifyDefensiveBonus(attacker, defender, bonus);
        }
        return bonus;
    }

}
