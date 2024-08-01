package com.osroyale.game.world.entity.combat.formula;

import com.osroyale.game.world.entity.combat.FormulaModifier;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.strategy.npc.boss.CorporealBeast;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.containers.equipment.Equipment;

import java.util.concurrent.TimeUnit;

public final class MeleeFormula implements FormulaModifier<Mob> {

    @Override
    public int modifyAccuracy(Mob attacker, Mob defender, int roll) {
        FightType fightType = attacker.getCombat().getFightType();
        int level = attacker.skills.getLevel(Skill.ATTACK);
        int effectiveAccuracy = attacker.getCombat().modifyAttackLevel(defender, level);
        return 8 + effectiveAccuracy + fightType.getStyle().getAccuracyIncrease();
    }

    @Override
    public int modifyAggressive(Mob attacker, Mob defender, int roll) {
        FightType fightType = attacker.getCombat().getFightType();
        int level = attacker.skills.getLevel(Skill.STRENGTH);
        int effectiveStrength = attacker.getCombat().modifyStrengthLevel(defender, level);
        return 8 + effectiveStrength + fightType.getStyle().getStrengthIncrease();
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
        FightType fightType = attacker.getCombat().getFightType();
        bonus = attacker.getBonus(fightType.getBonus());
        return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Mob attacker, Mob defender, int bonus) {
        bonus = attacker.getBonus(Equipment.STRENGTH_BONUS);
        return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Mob attacker, Mob defender, int bonus) {
        FightType fightType = attacker.getCombat().getFightType();
        bonus = defender.getBonus(fightType.getCorrespondingBonus());
        return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
    }

    @Override
    public int modifyDamage(Mob attacker, Mob defender, int damage) {
        damage = attacker.getCombat().modifyDamage(defender, damage);
        if(defender.isNpc() && defender.getNpc().id == 319 && (attacker.isPlayer() && !attacker.getPlayer().equipment.containsAny(CorporealBeast.weaponsAllowed))) {
            damage /= 2;
        }
        if (defender.isPlayer() && !defender.getPlayer().staffOfDeadSpecial.elapsed(1, TimeUnit.MINUTES) && defender.getPlayer().equipment.containsAny(11791, 12904)) {
            damage /= 2;
        }
        if (defender.prayer.isActive(Prayer.PROTECT_FROM_MELEE) && !defender.attributes.has("VERACS-EFFECT")) {
            damage *= !attacker.isPlayer() || defender.isNpc() ? 0.0 : 0.6;
        }
        return damage;
    }

}
