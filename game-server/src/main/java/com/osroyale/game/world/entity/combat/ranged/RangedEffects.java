package com.osroyale.game.world.entity.combat.ranged;

import com.google.common.collect.ImmutableSet;
import com.osroyale.game.Graphic;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.PoisonType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.attack.FormulaFactory;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.util.RandomUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.osroyale.game.world.entity.combat.CombatUtil.getHitDelay;

public enum RangedEffects {
    OPAL_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.success(0.05);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            hit.setDamage((int) (baseHit * 1.10));
            defender.graphic(new Graphic(749, 0, 20));
        }
    }),
    PEARL_BOLTS(new CombatImpact() {
        final ImmutableSet<Integer> affectedNpcs = ImmutableSet.of(941, 55, 54, 53, 50, 5362, 1590, 1591, 1592, 5363, 110, 1633, 1634, 1635, 1636, 1019, 2591, 2592, 2593, 2594, 2595, 2596, 2597, 2598, 2599, 2600, 2601, 2602, 2603, 2604, 2605, 2606, 2607, 2608, 2609, 2610, 2611, 2612, 2613, 2614, 2615, 2616, 2627, 2628, 2629, 2630, 2631, 2631, 2734, 2735, 2736, 2737, 2738, 2739, 2740, 2741, 2742, 2743, 2744, 2745, 2746);

        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            if (defender.isPlayer() && defender.getPlayer().equipment.containsAny(1383, 1395, 1403, 21006)) {
                return false;
            }

            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.success(0.06);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            boolean multiply = false;
            int baseHit = hit.getDamage();

            if (defender.isNpc() && affectedNpcs.contains(defender.getNpc().id)) {
                multiply = true;
            }

            if (defender.isPlayer() && defender.getPlayer().equipment.containsAny(1387, 1393, 1401)) {
                multiply = true;
            }

            hit.setDamage((int) ((baseHit * 1.075) * (multiply ? 1.20 : 1.0)));
            defender.graphic(new Graphic(750, 0, 20));
        }
    }),
    JADE_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.success(0.06);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            defender.locking.lock(1199, TimeUnit.MILLISECONDS, LockType.WALKING);
            defender.graphic(new Graphic(755, 0, 20));
        }
    }),
    TOPAZ_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && !defender.isNpc() && RandomUtils.success(0.04);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            defender.skills.get(Skill.MAGIC).removeLevel(1);
            defender.graphic(new Graphic(757, 0, 20));
        }
    }),
    SAPPHIRE_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && !defender.isNpc() && RandomUtils.success(0.04);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
            int percentage = (int) (baseHit * 0.05);
            defender.skills.get(Skill.PRAYER).removeLevel(percentage);
            attacker.skills.get(Skill.PRAYER).addLevel(percentage);
            defender.graphic(new Graphic(759, 100, 20));
        }
    }),
    EMERALD_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            if (defender.isPoisoned()) return false;
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.success(0.54);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            defender.poison(PoisonType.SUPER_RANGED);
        }
    }),
    RUBY_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            if (attacker.getCurrentHealth() / 5 <= 0) return false;
            if (defender.isPlayer() && !RandomUtils.success(0.11)) return false;
            if (defender.isNpc() && !RandomUtils.success(0.06)) return false;
            return hit.isAccurate() && hit.getDamage() > 0;
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            int damage = defender.getCurrentHealth() / 5;

            if (damage > 100)
                damage = 100;

            hit.setDamage(damage);
            defender.graphic(new Graphic(754, 0, 20));

            int inflict = attacker.getCurrentHealth() / 10;
            attacker.damage(new Hit(inflict, HitIcon.DEFLECT));
        }
    }),
    DIAMOND_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            if (defender.isPlayer() && !RandomUtils.success(0.05)) return false;
            if (defender.isNpc() && !RandomUtils.success(0.10)) return false;
            return hit.isAccurate() && hit.getDamage() > 0;
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            hit.setDamage(hit.getDamage() + RandomUtils.inclusive(5, 14));
            defender.graphic(new Graphic(758, 0, 20));
        }
    }),
    DRAGON_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            if (defender.isPlayer() && defender.getPlayer().equipment.containsAny(1540, 11283)) {
                return false;
            }

            if (defender.isPlayer() && defender.getPlayer().getAntifireDetails().isPresent()) {
                return false;
            }

            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.success(0.06);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            int baseHit = hit.getDamage();
//            hit.setDamage((int) (baseHit * 1.14));
            hit.setDamage((int) (baseHit * 1.58));
            defender.graphic(new Graphic(756, 0, 20));
        }
    }),
    ONYX_BOLTS(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return hit.isAccurate() && hit.getDamage() > 0 && RandomUtils.success(0.10);
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            // TODO spell shouldn't work on undead
            int damage = hit.getDamage() * 6 / 5;
            hit.setDamage(damage);
            attacker.heal(damage / 4);
            defender.graphic(new Graphic(756, 0, 20));
        }
    }),

    GUAM_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            Player player = attacker.getPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.skills.get(Skill.MAGIC);
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 15 / 80) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender, 1);
                hit.setAs(magicHit);
            }
        }
    }),
    MARRENTIL_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            Player player = attacker.getPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.skills.get(Skill.MAGIC);
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 123 / 640) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender, 1);
                hit.setAs(magicHit);
            }
        }
    }),
    TARROMIN_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            Player player = attacker.getPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.skills.get(Skill.MAGIC);
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 141 / 640) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender, 1);
                hit.setAs(magicHit);
            }
        }
    }),
    HARRALANDER_TAR(new CombatImpact() {
        @Override
        public boolean canAffect(Mob attacker, Mob defender, Hit hit) {
            return attacker.isPlayer();
        }

        @Override
        public void impact(Mob attacker, Mob defender, Hit hit, List<Hit> hits) {
            Player player = attacker.getPlayer();
            FightType fightType = player.getCombat().getFightType();
            CombatType type = fightType.equals(FightType.FLARE) ? CombatType.RANGED : fightType.equals(FightType.SCORCH) ? CombatType.MELEE : CombatType.MAGIC;

            if (type.equals(CombatType.MAGIC)) {
                Skill magic = player.skills.get(Skill.MAGIC);
                int max = (int) (Math.floor(0.5 + magic.getLevel() * 156 / 640) * 10);
                Hit magicHit = FormulaFactory.nextMagicHit(attacker, defender, max);
                hit.setAs(magicHit);
            } else if (type.equals(CombatType.MELEE)) {
                Hit magicHit = FormulaFactory.nextMeleeHit(attacker, defender, 1);
                hit.setAs(magicHit);
            }
        }
    }),

    CHINCHOMPA((attacker, defender, hit, hits) -> {
        int baseHit = hit.getDamage();
        CombatUtil.areaAction(defender,
            actor -> {
                int min = baseHit - 5;
                if (min < 0) min = 0;
                hitEvent(attacker, defender, actor, RandomUtils.inclusive(min, baseHit + 5), hits);
            });
    }),
    RED_CHINCHOMPA((attacker, defender, hit, hits) -> {
        CombatUtil.areaAction(defender, actor -> hitEvent(attacker, defender, actor, hit.getDamage(), hits));
    });

    private final CombatImpact effect;

    RangedEffects(CombatImpact effect) {
        this.effect = effect;
    }

    public CombatImpact getEffect() {
        return effect;
    }

    private static void hitEvent(Mob attacker, Mob defender, Mob actor, int damage, List<Hit> extra) {
        if (!defender.equals(actor)) {
            int hitDelay = getHitDelay(attacker, defender, CombatType.RANGED);
            CombatHit hit = new CombatHit(new Hit(damage, HitIcon.RANGED), hitDelay, 0);
            attacker.getCombat().submitHits(actor, hit);
            if (extra != null) extra.add(hit);
        }
    }

}
