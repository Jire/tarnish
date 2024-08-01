package com.osroyale.game.world.entity.combat.accuracy;

import com.osroyale.content.skill.impl.slayer.SlayerTask;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.FormulaUtils;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.util.Items;

import java.security.SecureRandom;

/**
 * @Author Origin
 * 2/22/2023
 */
public class MagicAccuracyNpc {

    public static final SecureRandom srand = new SecureRandom();

    public static boolean successful(Mob attacker, Mob defender, CombatType style) {
        double attackRoll = MagicAccuracy.getAttackRoll(attacker);//getAttackRoll(attacker, style, null); // XXX: task needs to be grabbed in the inverse direction (player attacking NPC)
        double defenceRoll = MagicAccuracy.getDefenceRoll(defender);//getDefenceRoll(defender, style);

        double chance;
        if (attackRoll > defenceRoll)
            chance = 1D - (defenceRoll + 2D) / (2D * (attackRoll + 1D));
        else
            chance = attackRoll / (2D * (defenceRoll + 1D));

        double roll = srand.nextDouble();
        return chance > roll;
    }

    public static double getPrayerBonus(Mob attacker, CombatType style) {
        double prayerBonus = 1D;
        if (style == CombatType.MAGIC) {
            if (attacker.prayer.isActive(Prayer.MYSTIC_WILL))
                prayerBonus *= 1.05D; // 5% magic level boost
            else if (attacker.prayer.isActive(Prayer.MYSTIC_LORE))
                prayerBonus *= 1.10D; // 10% magic level boost
            else if (attacker.prayer.isActive(Prayer.MYSTIC_MIGHT))
                prayerBonus *= 1.15D; // 15% magic level boost
            else if (attacker.prayer.isActive(Prayer.AUGURY))
                prayerBonus *= 1.25D; // 25% magic level boost
        }
        return prayerBonus;
    }

    public static int getEquipmentBonus(Mob attacker, CombatType style, SlayerTask task) {
        int bonus = 1;
        if (attacker.isPlayer()) {
            if (style == CombatType.MAGIC) {
                bonus = attacker.getPlayer().getBonus(3);
            }
        }

        if (attacker.isPlayer()) {
            if (style.equals(CombatType.MAGIC)) {
                if (FormulaUtils.voidMagic((Player) attacker)) {
                    bonus *= 1.45D; //45%
                }
                if (((Player) attacker).equipment.contains(27275)) {
                    bonus *= 3;
                }
                if (((Player) attacker).equipment.contains(Items.SALVE_AMULET)) {
                    bonus *= 1.10D;
                }
                if (attacker.isNpc()) {
                    if (((Player) attacker).equipment.contains(Items.SLAYER_HELMET)) {
                        bonus *= 1.05D;
                    }
                    if (((Player) attacker).equipment.contains(Items.BLACK_SLAYER_HELMET) || ((Player) attacker).equipment.contains(Items.GREEN_SLAYER_HELMET)
                            || ((Player) attacker).equipment.contains(Items.PURPLE_SLAYER_HELMET) || ((Player) attacker).equipment.contains(Items.RED_SLAYER_HELMET) || ((Player) attacker).equipment.contains(21888)) {
                        bonus *= 1.10D;
                    }
                    if (((Player) attacker).equipment.contains(19720)) {
                        bonus *= 1.05D;
                    }
                    if (((Player) attacker).equipment.contains(22555)) {
                        bonus *= 1.50D;
                    }
                }
            }
            if (task != null && task.valid(task.getName())) {
                if (((Player) attacker).equipment.contains(Items.SLAYER_HELMET)) {
                    bonus *= 1.15D;
                }
                if (((Player) attacker).equipment.contains(11865)) {
                    bonus *= 1.18D;
                }
                if (((Player) attacker).equipment.contains(Items.BLACK_SLAYER_HELMET) || ((Player) attacker).equipment.contains(Items.GREEN_SLAYER_HELMET) || ((Player) attacker).equipment.contains(Items.PURPLE_SLAYER_HELMET)
                        || ((Player) attacker).equipment.contains(Items.RED_SLAYER_HELMET) || ((Player) attacker).equipment.contains(21888)) {
                    bonus *= 1.20D;
                }
            }
        }
        //some of these are custom
        return bonus;
    }

    public static int getMagicLevelNpc(Mob defender) {
        return defender.getNpc().definition.getSkills()[6];
    }

    public static int getMagicDefenceLevelNpc(Mob defender, CombatType style) {
        int bonus = 0;
        if (defender instanceof Npc npc) {
            if (style == CombatType.MAGIC) {
                bonus = npc.getBonus(8);
            }
        }
        return bonus + 64;
    }

    public static double getEffectiveLevelDefender(Mob defender) {
        return getMagicLevelNpc(defender);
    }

    public static double getDefenceRoll(Mob defender, CombatType style) {
        return Math.floor((getEffectiveLevelDefender(defender) + 9) * (getMagicDefenceLevelNpc(defender, style) + 64));
    }

    public static int getMagicLevel(Mob attacker) {
        return attacker.skills.getLevel(Skill.MAGIC);
    }

    public static double getEffectiveLevelAttacker(Mob attacker, CombatType style) {
        return getMagicLevel(attacker) * (getPrayerBonus(attacker, style) + 9D);
    }

    public static double getAttackRoll(Mob attacker, CombatType style, SlayerTask task) {
        return Math.round(getEffectiveLevelAttacker(attacker, style) * (getEquipmentBonus(attacker, style, task)) + 64D);
    }
}

