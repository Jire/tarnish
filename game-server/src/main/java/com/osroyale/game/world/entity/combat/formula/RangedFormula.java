package com.osroyale.game.world.entity.combat.formula;

import com.osroyale.game.world.entity.combat.FormulaModifier;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.containers.equipment.Equipment;

public final class RangedFormula implements FormulaModifier<Mob> {

    @Override
    public int modifyAccuracy(Mob attacker, Mob defender, int roll) {
        FightType fightType = attacker.getCombat().getFightType();
        int level = attacker.skills.getLevel(Skill.RANGED);
        int effectiveAccuracy = attacker.getCombat().modifyRangedLevel(defender, level);
        return 8 + effectiveAccuracy + fightType.getStyle().getAccuracyIncrease();
    }

    @Override
    public int modifyAggressive(Mob attacker, Mob defender, int roll) {
        int level = attacker.skills.getLevel(Skill.RANGED);
        return 8 + attacker.getCombat().modifyRangedLevel(defender, level);
    }

    @Override
    public int modifyDefensive(Mob attacker, Mob defender, int roll) {
        FightType fightType = defender.getCombat().getFightType();
        int level = defender.skills.getLevel(Skill.DEFENCE);
        int effectiveDefence = defender.getCombat().modifyDefenceLevel(attacker, level);
        return 8 + effectiveDefence + fightType.getStyle().getDefensiveIncrease();
    }

    @Override
    public int modifyOffensiveBonus(Mob attacker, Mob defender, int bonus) {
        bonus = attacker.getBonus(Equipment.RANGED_OFFENSE);
        return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Mob attacker, Mob defender, int bonus) {
        bonus = attacker.getBonus(Equipment.RANGED_STRENGTH);
        return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Mob attacker, Mob defender, int bonus) {
        bonus = defender.getBonus(Equipment.RANGED_DEFENSE);
        return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
    }

    @Override
    public int modifyDamage(Mob attacker, Mob defender, int damage) {
        damage = attacker.getCombat().modifyDamage(defender, damage);
        if (defender.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
            damage *= !attacker.isPlayer() || defender.isNpc() ? 0.0 : 0.6;
        }
        return damage;
    }

}
