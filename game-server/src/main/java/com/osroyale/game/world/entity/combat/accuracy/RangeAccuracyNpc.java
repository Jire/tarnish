package com.osroyale.game.world.entity.combat.accuracy;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.FormulaUtils;
import com.osroyale.game.world.entity.combat.attack.FightStyle;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.util.Items;

import java.security.SecureRandom;


/**
 * @Author Origin
 * 2/22/2023
 */
public class RangeAccuracyNpc {
    public static final SecureRandom srand = new SecureRandom();

    public static boolean successful(Mob attacker, Mob defender, CombatType style) {
        double attackBonus = RangeAccuracy.getAttackRoll(attacker);//getAttackRoll(attacker, defender, style);
        double defenceBonus = RangeAccuracy.getDefenceRoll(defender);//getDefenceRoll(defender, style);
        double successfulRoll;
        double selectedChance = srand.nextDouble();

        if (attackBonus > defenceBonus)
            successfulRoll = 1D - (defenceBonus + 2D) / (2D * (attackBonus + 1D));
        else
            successfulRoll = attackBonus / (2D * (defenceBonus + 1D));


        return successfulRoll > selectedChance;
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

    public static int getDefenceNpc(Mob defender) {
        return defender.getNpc().definition.getSkills()[1];
    }

    public static double getEffectiveRanged(Mob attacker, CombatType style) {
        double effectiveLevel = Math.floor(getRangeLevel(attacker)) * getPrayerAttackBonus(attacker);
        FightStyle fightStyle = attacker.getCombat().getFightType().getStyle();
        switch (fightStyle) {
            case ACCURATE:
                effectiveLevel += 3.0D;
                break;
        }

        effectiveLevel += 8;

        if(attacker.isPlayer()) {
            if (style.equals(CombatType.RANGED)) {
                if (FormulaUtils.voidRanger((Player) attacker)) {
                    effectiveLevel *= 1.10D;
                    effectiveLevel = Math.floor(effectiveLevel);
                }
            }
                if (((Player) attacker).equipment.contains(Items.SALVE_AMULET)) {
                    effectiveLevel *= 1.10D;
            }//END OF UNDEAD
            if (Area.inWilderness(attacker)) {
                if (((Player) attacker).equipment.contains(22550)) {
                    effectiveLevel *= 1.50D;
                }
            }//END OF WILDERNESS BUFFS
            if (attacker.isNpc()) {
                if (((Player) attacker).equipment.contains(Items.SLAYER_HELMET)) {
                    effectiveLevel *= 1.05D;
                }
                if (((Player) attacker).equipment.contains(Items.BLACK_SLAYER_HELMET) || ((Player) attacker).equipment.contains(Items.GREEN_SLAYER_HELMET)  || ((Player) attacker).equipment.contains(Items.PURPLE_SLAYER_HELMET) || ((Player) attacker).equipment.contains(Items.RED_SLAYER_HELMET) || ((Player) attacker).equipment.contains(21888)) {
                    effectiveLevel *= 1.10D;
                }

            }
        }
        return Math.floor(effectiveLevel);
    }

    public static double getEffectiveLevelDefender(Mob defender) {
        return getDefenceNpc(defender) + 9;
    }

    public static int getRangeLevel(Mob attacker) {
        int rangeLevel = 1;
        if (attacker instanceof Npc npc) {
            if (npc.definition.getSkills() != null)
                rangeLevel = npc.definition.getSkills()[4];
        } else {
            rangeLevel = attacker.skills.getLevel(Skill.RANGED);
        }
        return rangeLevel;
    }

    public static double twistedBowBonus(Mob attacker, Mob defender) {
        double bonus = 1;
        double magicLevel = 1;
        int getMagicLevel = defender.skills.getMaxLevel(Skill.MAGIC);
        Player player = (Player) attacker;
        final Item weapon = player.equipment.getWeapon();
        if (weapon != null) {
            if (player.equipment.contains(20997)) {
                if (attacker.isPlayer()) {
                    if (defender instanceof Npc n) {
                        if (n.definition.getSkills() != null)
                            magicLevel = defender.skills.getMaxLevel(Skill.MAGIC) > 350 ? 350 :defender.skills.getMaxLevel(Skill.MAGIC) > 250D ? 250D : defender.skills.getMaxLevel(Skill.MAGIC);
                    } else {
                            magicLevel = getMagicLevel;
                    }
                    bonus += ((140 + (((10 * 3) * magicLevel) - 10) / 100D) - ((((3 * magicLevel) - 100D) * 2)) / 100D);
                    if (bonus > 2.4D)
                        bonus = (int) 2.4;
                }
            }
        }
        return bonus;
    }

    public static int getGearAttackBonus(Mob attacker, Mob defender, CombatType style) {
        int bonus = 1;
        if (style == CombatType.RANGED) {
            bonus = attacker.getBonus(4);
        }
        if (attacker.getPlayer().equipment.contains(20997)) {
            bonus *= twistedBowBonus(attacker, defender);
        }
        return bonus;
    }

    public static int getRangeDefenceLevelNpc(Mob defender, CombatType style) {
        int bonus = 1;
        if (defender instanceof Npc) {
            if (style == CombatType.RANGED) {
                bonus = defender.getNpc().getBonus(9);
            }
        }
        return bonus;
    }

    public static double getAttackRoll(Mob attacker, Mob defender, CombatType style) {
        double effectiveRangeLevel = getEffectiveRanged(attacker, style);

        double equipmentRangeBonus = getGearAttackBonus(attacker, defender, style);

        double maxRoll = effectiveRangeLevel * (equipmentRangeBonus);

        return (int) (maxRoll);
    }

    public static double getDefenceRoll(Mob defender, CombatType style) {
        double effectiveDefenceLevel = getEffectiveLevelDefender(defender);

        int equipmentRangeBonus = getRangeDefenceLevelNpc(defender, style);

        double maxRoll = effectiveDefenceLevel * (equipmentRangeBonus);

        return (int) maxRoll;
    }

}
