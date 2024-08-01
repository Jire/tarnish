package com.osroyale.game.world.entity.combat.attack;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.FormulaModifier;
import com.osroyale.game.world.entity.combat.accuracy.*;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.combat.maxhit.MeleeMaxHit;
import com.osroyale.game.world.entity.combat.maxhit.RangeMaxHit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.util.RandomUtils;

/**
 * Supplies factory methods useful for combat.
 *
 * @author Michael | Chex
 */
public final class FormulaFactory {

    public static Hit nextMeleeHit(Mob attacker, Mob defender) {
        int max = getMaxHit(attacker, defender, CombatType.MELEE);
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE, false, CombatType.MELEE);
    }

    public static Hit nextMeleeHit(Mob attacker, Mob defender, int max) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE, false, CombatType.MELEE);
    }

    public static Hit nextMeleeHit(Mob attacker, Mob defender, int max, boolean multipleHitsAllowed) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MELEE, multipleHitsAllowed, CombatType.MELEE);
    }

    public static Hit nextRangedHit(Mob attacker, Mob defender, int max) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.RANGED, false, CombatType.RANGED);
    }

    public static Hit nextMagicHit(Mob attacker, Mob defender, int max) {
        return nextHit(attacker, defender, max, Hitsplat.NORMAL, HitIcon.MAGIC, false, CombatType.MAGIC);
    }

    private static Hit nextHit(Mob attacker, Mob defender, int max, Hitsplat hitsplat, HitIcon icon, boolean multipleHitsAllowed, CombatType type) {
        final CombatStrategy<? super Mob> strategy = attacker.getStrategy();
        final int totalHits = defender.isNpc() && defender.getNpc().definition.getSize() > 1 ? 3 : 1;

        if (strategy.toString().contains("ScytheOfVitur") && totalHits > 1) {
            final Hit[] hits = new Hit[totalHits];

            for (int index = 0; index < hits.length; index++) {
                final Hit hit = hits[index] = new Hit(max < 0 ? -1 : 0, hitsplat, icon,
                        isAccurate(attacker, defender, type, strategy));

                if (max > 0) {
                    max = type.getFormula().modifyDamage(attacker, defender, max);
                    int verdict = RandomUtils.inclusive(0, max);

                    switch (index) {
                        case 1:
                            verdict /= 2;
                            break;
                        case 2:
                            verdict /= 3;
                            break;
                    }

                    if (verdict > defender.getCurrentHealth()) {
                        verdict = defender.getCurrentHealth();
                    }

                    hit.setDamage(verdict);
                }
            }

            return new Hit(hits);
        }

        final Hit hit = new Hit(max < 0 ? -1 : 0, hitsplat, icon,
                isAccurate(attacker, defender, type, strategy));
        if (max > 0) {
            max = type.getFormula().modifyDamage(attacker, defender, max);
            int verdict = RandomUtils.inclusive(0, max);

            if (verdict > defender.getCurrentHealth()) {
                verdict = defender.getCurrentHealth();
            }

            hit.setDamage(verdict);
        }

        return hit;
    }

    private static boolean isAccurate(Mob attacker, Mob defender, CombatType type, CombatStrategy<? super Mob> strategy) {
        if (strategy != null && strategy.isAlwaysAccurate()) {
            return true;
        }

        double attackRoll = rollOffensive(attacker, defender, type.getFormula());
        double defenceRoll = rollDefensive(attacker, defender, type.getFormula());
        boolean isNpc = attacker.isNpc();
        switch (type) {
            case RANGED -> {
                return isNpc ? RangeAccuracyNpc.successful(attacker, defender, type) : RangeAccuracy.successful(attacker, defender);
            }
            case MAGIC -> {
                return isNpc ? MagicAccuracyNpc.successful(attacker, defender, type) : MagicAccuracy.successful(attacker, defender);
            }
            case MELEE -> {
                return MeleeAccuracy.successful(attacker, defender);
            }
            default -> {
                return RandomUtils.success(attackRoll / (attackRoll + defenceRoll));
            }
        }
    }

    public static int rollOffensive(Mob attacker, Mob defender, FormulaModifier<Mob> formula) {
        int roll = formula.modifyAccuracy(attacker, defender, 0);
        int bonus = formula.modifyOffensiveBonus(attacker, defender, 0);
        return attacker.getCombat().modifyAccuracy(defender, roll * (bonus + 64));
    }

    public static int rollDefensive(Mob attacker, Mob defender, FormulaModifier<Mob> formula) {
        int roll = formula.modifyDefensive(attacker, defender, 0);
        int bonus = formula.modifyDefensiveBonus(attacker, defender, 0);
        return attacker.getCombat().modifyDefensive(defender, roll * (bonus + 64));
    }

    public static int getMaxHit(Mob attacker, Mob defender, CombatType type) {
        FormulaModifier<Mob> formula = type.getFormula();
        int level = formula.modifyAggressive(attacker, defender, 0);
        int bonus = formula.modifyAggressiveBonus(attacker, defender, 0);
        switch (attacker.getStrategy().getCombatType()) {
            case MELEE -> MeleeMaxHit.maxHit(attacker);
        }
        return maxHit(level, bonus);
    }

    public static int getModifiedMaxHit(Mob attacker, Mob defender, CombatType type) {
        FormulaModifier<Mob> formula = type.getFormula();
        int level = formula.modifyAggressive(attacker, defender, 0);
        int bonus = formula.modifyAggressiveBonus(attacker, defender, 0);
        switch (attacker.getStrategy().getCombatType()) {
            case MELEE -> MeleeMaxHit.maxHit(attacker);
        }
        if (attacker.getStrategy().getCombatType().equals(CombatType.RANGED)) {
            return (int) Math.floor(RangeMaxHit.maxHit(attacker));
        }
        return formula.modifyDamage(attacker, defender, maxHit(level, bonus));
    }

    private static int maxHit(int level, int bonus) {
        return (320 + level * (bonus + 64)) / 640;
    }

}
