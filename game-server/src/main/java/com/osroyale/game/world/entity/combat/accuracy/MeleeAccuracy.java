package com.osroyale.game.world.entity.combat.accuracy;

import com.osroyale.game.world.entity.combat.FormulaUtils;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;

import java.security.SecureRandom;

/**
 * @Author Origin
 * 2/22/2023
 */
public class MeleeAccuracy {

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

    public static double getAttackRoll(Mob attacker) {
        double effectiveLevel = getEffectiveLevel(attacker);
        double attackBonus = getAttackBonus(attacker);

        double roll = effectiveLevel * (attackBonus + 64);

        if (attacker instanceof Player player) {
            // TODO equipment bonuses
        }

        return Math.floor(roll);
    }

    public static double getEffectiveLevel(Mob attacker) {
        double effectiveLevel = attacker.skills.getLevel(Skill.ATTACK);
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
            if (FormulaUtils.voidMelee(player)) {
                effectiveLevel *= 1.10;
            }
        }

        return Math.floor(effectiveLevel);
    }

    private static double getPrayerDefenseBonus(Mob defender) {
        double prayerBonus = 1;
        if (defender.prayer.isActive(Prayer.THICK_SKIN))
            prayerBonus += 1.05D; // 5% def level boost
        else if (defender.prayer.isActive(Prayer.ROCK_SKIN))
            prayerBonus += 1.10D; // 10% def level boost
        else if (defender.prayer.isActive(Prayer.STEEL_SKIN))
            prayerBonus += 1.15D; // 15% def level boost
        else if (defender.prayer.isActive(Prayer.CHIVALRY))
            prayerBonus += 1.20D; // 20% def level boost
        else if (defender.prayer.isActive(Prayer.PIETY))
            prayerBonus += 1.25D; // 25% def level boost
        return prayerBonus;
    }

    private static int getDefenceBonus(Mob defender) {
        return defender.getBonus(defender.getCombat().getFightType().getCorrespondingBonus());
    }

    private static int getAttackBonus(Mob attacker) {
        return attacker.getBonus(attacker.getCombat().getFightType().getBonus());
    }

    private static double getPrayerAttackBonus(Mob attacker) {
        double prayerBonus = 1;
        if (attacker.prayer.isActive(Prayer.THICK_SKIN))
            prayerBonus += 1.05D; // 5% def level boost
        else if (attacker.prayer.isActive(Prayer.ROCK_SKIN))
            prayerBonus += 1.10D; // 10% def level boost
        else if (attacker.prayer.isActive(Prayer.STEEL_SKIN))
            prayerBonus += 1.15D; // 15% def level boost
        else if (attacker.prayer.isActive(Prayer.CHIVALRY))
            prayerBonus += 1.20D; // 20% def level boost
        else if (attacker.prayer.isActive(Prayer.PIETY))
            prayerBonus += 1.25D; // 25% def level boost
        return prayerBonus;
    }

}
