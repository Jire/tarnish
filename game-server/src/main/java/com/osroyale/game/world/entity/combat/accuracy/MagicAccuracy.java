package com.osroyale.game.world.entity.combat.accuracy;

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
public class MagicAccuracy {

    public static final SecureRandom srand = new SecureRandom();

    public static boolean successful(Mob attacker, Mob defender) {
        double attackRoll = getAttackRoll(attacker);
        if (attacker instanceof Player player) {
            if (FormulaUtils.voidMagic(player)) {
                attackRoll *= 1.45;
            }
        }

        double defenceRoll = getDefenceRoll(defender);

        double chance;
        if (attackRoll > defenceRoll)
            chance = 1D - (defenceRoll + 2D) / (2D * (attackRoll + 1D));
        else
            chance = attackRoll / (2D * (defenceRoll + 1D));

        double roll = srand.nextDouble();
        return chance > roll;
    }

    public static int getAttackBonus(Mob attacker) {
        return attacker.getBonus(Equipment.MAGIC_OFFENSE);
    }

    public static int getDefenceBonus(Mob defender) {
        return defender.getBonus(Equipment.MAGIC_DEFENSE);
    }

    public static double getDefenceRoll(Mob defender) {
        double magicLevel = getMagicLevel(defender);
        double defenceBonus = getDefenceBonus(defender);

        double effectiveLevel;
        if (defender instanceof Player) {
            magicLevel *= getPrayerBonusDefender(defender);

            double defenceLevel = defender.skills.getLevel(Skill.DEFENCE);
            switch (defender.getCombat().getFightType().getStyle()) {
                case DEFENSIVE -> defenceLevel += 3;
                case CONTROLLED -> defenceLevel += 1;
            }

            effectiveLevel = magicLevel * 0.7 + defenceLevel * 0.3 + 8;
        } else {
            effectiveLevel = magicLevel + 9;
        }

        return effectiveLevel * (defenceBonus + 64);
    }

    public static int getMagicLevel(Mob mob) {
        int magicLevel = 1;
        if (mob instanceof Npc npc) {
            int[] skills = npc.definition.getSkills();
            if (skills != null) {
                magicLevel = skills[Skill.MAGIC];
            }
        } else {
            magicLevel = mob.skills.getLevel(Skill.MAGIC);
        }
        return magicLevel;
    }

    public static double getPrayerBonus(Mob attacker) {
        double prayerBonus = 1;
        if (attacker.prayer.isActive(Prayer.MYSTIC_WILL))
            prayerBonus *= 1.05D; // 5% magic level boost
        else if (attacker.prayer.isActive(Prayer.MYSTIC_LORE))
            prayerBonus *= 1.10D; // 10% magic level boost
        else if (attacker.prayer.isActive(Prayer.MYSTIC_MIGHT))
            prayerBonus *= 1.15D; // 15% magic level boost
        else if (attacker.prayer.isActive(Prayer.AUGURY))
            prayerBonus *= 1.25D; // 25% magic level boost
        return prayerBonus;
    }

    public static double getPrayerBonusDefender(Mob defender) {
        double prayerBonus = 1;
        if (defender.prayer.isActive(Prayer.AUGURY))
            prayerBonus *= 1.25D;
        return prayerBonus;
    }

    public static double getAttackRoll(Mob attacker) {
        double magicLevel = getMagicLevel(attacker);
        double attackBonus = getAttackBonus(attacker);

        double effectiveLevel;
        if (attacker instanceof Player) {
            effectiveLevel = magicLevel * getPrayerBonus(attacker) + 8;
        } else {
            effectiveLevel = magicLevel + 9;
        }

        return effectiveLevel * (attackBonus + 64);
    }

}
