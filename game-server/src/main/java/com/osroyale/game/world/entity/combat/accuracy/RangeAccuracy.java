package com.osroyale.game.world.entity.combat.accuracy;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.FormulaUtils;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.containers.equipment.Equipment;

import java.security.SecureRandom;

/**
 * @Author Origin
 * 2/22/2023
 */
public class RangeAccuracy {

    public static final SecureRandom srand = new SecureRandom();

    public static boolean successful(Mob attacker, Mob defender) {
        double attackRoll = getAttackRoll(attacker);
        double defenceRoll = getDefenceRoll(defender);

        double chance;
        if (attackRoll > defenceRoll)
            chance = 1D - (defenceRoll + 2D) / (2D * (attackRoll + 1D));
        else
            chance = attackRoll / (2D * (defenceRoll + 1D));

        double roll = srand.nextDouble();


        return chance > roll;
    }

    public static double getPrayerAttackBonus(Mob attacker) {
        double prayerBonus = 1;
        if (attacker.getStrategy().getCombatType().equals(CombatType.RANGED)) {
            if (attacker.prayer.isActive(Prayer.SHARP_EYE))
                prayerBonus *= 1.05D; // 5% range level boost
            else if (attacker.prayer.isActive(Prayer.HAWK_EYE))
                prayerBonus *= 1.10D; // 10% range level boost
            else if (attacker.prayer.isActive(Prayer.EAGLE_EYE))
                prayerBonus *= 1.15D; // 15% range level boost
            else if (attacker.prayer.isActive(Prayer.RIGOUR))
                prayerBonus *= 1.20D; // 20% range level boost
        }
        return prayerBonus;
    }

    public static double getPrayerDefenseBonus(Mob defender) {
        double prayerBonus = 1;
        if (defender.prayer.isActive(Prayer.RIGOUR)) {
            prayerBonus *= 1.25D;
        }
        return prayerBonus;
    }

    public static double getEffectiveLevel(Mob attacker) {
        double effectiveLevel = getRangeLevel(attacker);
        if (attacker instanceof Player) {
            effectiveLevel *= getPrayerAttackBonus(attacker);
            effectiveLevel = Math.floor(effectiveLevel);

            switch (attacker.getCombat().getFightType().getStyle()) {
                case ACCURATE -> effectiveLevel += 3;
                case CONTROLLED -> effectiveLevel += 1;
            }
        }
        effectiveLevel += 8;

        if (attacker instanceof Player player) {
            if (FormulaUtils.voidRanger(player)) {
                effectiveLevel *= 1.10;
            }
        }

        return Math.floor(effectiveLevel);
    }

    public static int getRangeLevel(Mob attacker) {
        int rangeLevel = 1;
        if (attacker instanceof Npc npc) {
            int[] skills = npc.definition.getSkills();
            if (skills != null) {
                rangeLevel = skills[Skill.RANGED];
            }
        } else {
            rangeLevel = attacker.skills.getLevel(Skill.RANGED);
        }
        return rangeLevel;
    }

    public static int getRangeBonus(Mob attacker) {
        return attacker.getBonus(Equipment.RANGED_OFFENSE);
    }

    public static int getDefenceBonus(Mob defender) {
        return defender.getBonus(Equipment.RANGED_DEFENSE);
    }

    public static double getAttackRoll(Mob attacker) {
        double effectiveLevel = getEffectiveLevel(attacker);
        double bonus = getRangeBonus(attacker);

        double roll = effectiveLevel * (bonus + 64);

        if (attacker instanceof Player player) {
            final Equipment equipment = player.equipment;
            if (equipment.contains(25865)
                    || equipment.contains(25867)
                    || equipment.contains(25884)
                    || equipment.contains(25886)
                    || equipment.contains(25888)
                    || equipment.contains(25890)
                    || equipment.contains(25892)
                    || equipment.contains(25894)
                    || equipment.contains(25896)) {
                if (equipment.contains(23971)) {
                    roll *= 1.05;
                }
                if (equipment.contains(23975)) {
                    roll *= 1.15;
                }
                if (equipment.contains(23975)) {
                    roll *= 1.10;
                }
            }
        }

        return Math.floor(roll);
    }

    public static double getDefenceRoll(Mob defender) {
        double defenceLevel = defender.skills.getLevel(Skill.DEFENCE);
        double defenceBonus = getDefenceBonus(defender);

        if (defender instanceof Player) {
            defenceLevel *= getPrayerDefenseBonus(defender);

            switch (defender.getCombat().getFightType().getStyle()) {
                case DEFENSIVE -> defenceLevel += 3;
                case CONTROLLED -> defenceLevel += 1;
            }
        } else {
            defenceLevel += 9;
        }

        return defenceLevel * (defenceBonus + 64);
    }

}
