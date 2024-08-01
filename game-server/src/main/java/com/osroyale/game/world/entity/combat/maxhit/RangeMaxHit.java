package com.osroyale.game.world.entity.combat.maxhit;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.FormulaUtils;
import com.osroyale.game.world.entity.combat.attack.FightStyle;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.containers.equipment.Equipment;

/**
 * @Author Origin
 */
public class RangeMaxHit {

    public static double getBaseDamage(Mob player) {
        int rangestr = player.getBonus(Equipment.RANGED_STRENGTH);
        return (1.3 + (getEffectiveRanged(player) / 10) + (rangestr / 80D) + (getEffectiveRanged(player) * rangestr / 640));
    }

    public static int getRangedlevel(Mob player) {
        return player.skills.getLevel(Skill.RANGED);
    }

    public static double getEffectiveRanged(Mob player) {
        return Math.floor(((getRangedlevel(player)) * getPrayerBonus(player)) * getOtherBonus(player)) + getStyleBonus(player);
    }

    public static double getPrayerBonus(Mob player) {
        double prayerBonus = 1;
        if (player.getStrategy().getCombatType().equals(CombatType.RANGED)) {
            if (player.prayer.isActive(Prayer.SHARP_EYE))
                prayerBonus *= 1.05D; // 5% range level boost
            else if (player.prayer.isActive(Prayer.HAWK_EYE))
                prayerBonus *= 1.10D; // 10% range level boost
            else if (player.prayer.isActive(Prayer.EAGLE_EYE))
                prayerBonus *= 1.15D; // 15% range level boost
            else if (player.prayer.isActive(Prayer.RIGOUR))
                prayerBonus *= 1.20D; // 20% range level boost
        }
        return prayerBonus;
    }

    public static int getStyleBonus(Mob player) {
        FightStyle style = player.getCombat().getFightType().getStyle();
        return style.equals(FightStyle.ACCURATE) ? 3 : 0;
    }

    public static double getOtherBonus(Mob player) {
        double otherBonus = 1.0;



        Mob target = player.getCombat().getDefender();


            if (FormulaUtils.voidRanger((Player) player)) {
                otherBonus *= 1.10;
            }


            if (FormulaUtils.wearingEliteVoid((Player) player)) {
                otherBonus *= 1.125;
            }

            /*
            * BOWFA, MIGHT ADD COLORED ONES INTO IT. IF SO, ADD HERE.
             */

            if (((Player) player).equipment.contains(23971) && ((Player) player).equipment.contains(25865)) {
                otherBonus *= 1.025;//2.5% damage boost
            }

            if (((Player) player).equipment.contains(23975) && ((Player) player).equipment.contains(25865)) {
                otherBonus *= 1.075;//7.5% damage boost
            }

            if (((Player) player).equipment.contains(23979) && ((Player) player).equipment.contains(25865)) {
                otherBonus *= 1.05;//5.0% damage boost
            }

            if (FormulaUtils.hasCrawsBow((Player) player) && target != null && target.isNpc()) {
                otherBonus += 0.50;
            }

        return otherBonus;
    }

    public static int maxHit(Mob player) {
        int maxHit;
        maxHit = (int) getBaseDamage(player);
        return maxHit;
    }
}
