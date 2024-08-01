package com.osroyale.game.world.entity.combat.formula;

import com.osroyale.game.world.entity.combat.FormulaModifier;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.containers.equipment.Equipment;

public final class MagicFormula implements FormulaModifier<Mob> {

    @Override
    public int modifyAccuracy(Mob attacker, Mob defender, int roll) {
        int level = attacker.skills.getLevel(Skill.MAGIC);
        return 8 + attacker.getCombat().modifyMagicLevel(defender, level);
    }

    @Override
    public int modifyAggressive(Mob attacker, Mob defender, int roll) {
        int level = attacker.skills.getLevel(Skill.MAGIC);
        return 8 + attacker.getCombat().modifyMagicLevel(defender, level);
    }

    @Override
    public int modifyDefensive(Mob attacker, Mob defender, int roll) {
        //FightType fightType = defender.getCombat().getFightType();

        int magic = defender.skills.getLevel(Skill.MAGIC);
        magic = defender.getCombat().modifyMagicLevel(defender, magic);

        int defence = defender.skills.getLevel(Skill.DEFENCE);
        defence = defender.getCombat().modifyDefenceLevel(attacker, defence);

        double eD;
        if (attacker instanceof Player) {
            eD = magic * 0.7 + defence * 0.3;
        } else {
            eD = magic + 9;
        }
        /*int effectiveLevel = 8 + fightType.getStyle().getDefensiveIncrease();
        effectiveLevel += 0.70 * magic + 0.30 * defence;*/

        return (int) eD;
    }

    @Override
    public int modifyDamage(Mob attacker, Mob defender, int damage) {
        int bonus = attacker.getBonus(Equipment.MAGIC_STRENGTH);

        if (attacker.isPlayer()) {
            if (attacker.getPlayer().isSingleCast()) {
                damage = attacker.getPlayer().singleCast.getCombatProjectile().getMaxHit();
            } else if (attacker.getPlayer().isAutocast()) {
                damage = attacker.getPlayer().autocast.getCombatProjectile().getMaxHit();
            }
        }

        damage += damage * bonus / 100;
        damage = attacker.getCombat().modifyDamage(defender, damage);

        if (defender.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
            damage *= !attacker.isPlayer() || defender.isNpc() ? 0.0 : 0.6;
        }

        return damage;
    }

    @Override
    public int modifyOffensiveBonus(Mob attacker, Mob defender, int bonus) {
        bonus = attacker.getBonus(Equipment.MAGIC_OFFENSE);
        return attacker.getCombat().modifyOffensiveBonus(defender, bonus);
    }

    @Override
    public int modifyAggressiveBonus(Mob attacker, Mob defender, int bonus) {
        return attacker.getCombat().modifyAggresiveBonus(defender, bonus);
    }

    @Override
    public int modifyDefensiveBonus(Mob attacker, Mob defender, int bonus) {
        bonus = defender.getBonus(Equipment.MAGIC_DEFENSE);
        return defender.getCombat().modifyDefensiveBonus(attacker, bonus);
    }

}
