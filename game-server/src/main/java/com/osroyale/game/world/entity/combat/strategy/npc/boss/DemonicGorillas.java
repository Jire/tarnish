package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.HashSet;
import java.util.Set;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * Created by Daniel on 2017-11-29.
 */
public class DemonicGorillas extends MultiStrategy {
    private static final Melee MELEE = new Melee();
    private static final Magic MAGIC = new Magic();
    private static final Ranged RANGED = new Ranged();
    private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(MAGIC, MELEE, RANGED);
    private static final Integer[] FORMS = { 7147, 7148, 7149 };
    private int attackCount = 0;
    private int damageDealt = 0;
    private int form = -1;

    public DemonicGorillas() {
        this.currentStrategy = randomStrategy(STRATEGIES);
    }

    private CombatStrategy getNextStrategy() {
        return RandomUtils.randomExclude(STRATEGIES, currentStrategy);
    }

    private int getNextForm() {
        return RandomUtils.randomExclude(FORMS, form);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        int nextForm = -1;

        if (form == -1) {
            nextForm = defender.id;
        }

        damageDealt += hit.getDamage();
        if (damageDealt >= 50) {
            nextForm = getNextForm();
            damageDealt = 0;
            attackCount = 0;
        }

        if (nextForm > -1) {
            defender.transform(nextForm);
            form = nextForm;

            if (form == 7147 && !defender.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
                defender.prayer.toggle(Prayer.PROTECT_FROM_MELEE);
            } else if (form == 7148 && !defender.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
                defender.prayer.toggle(Prayer.PROTECT_FROM_RANGE);
            } else if (form == 7149 && !defender.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                defender.prayer.toggle(Prayer.PROTECT_FROM_MAGIC);
            }
        }
    }

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
        if (hit.getDamage() == 0) {
            attackCount++;
            if (attackCount >= 3) {
                currentStrategy = getNextStrategy();
                attackCount = 0;
            }
        }
    }

    @Override
    public int modifyDefensive(Mob attacker, Npc defender, int roll) {
        return (int) (roll * 2.3);
    }

    private static final class Melee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(7226, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 2;
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender)};
        }
    }

    private static class Magic extends NpcMagicStrategy {
        private static final Animation ANIMATION = new Animation(7238, UpdatePriority.HIGH);

        private Magic() {
            super(getDefinition("Demonic Gorilla Magic"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 31);
            combatHit.setAccurate(true);
            return new CombatHit[]{combatHit};
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }
    }

    private static class Ranged extends NpcRangedStrategy {
        private static final Animation ANIMATION = new Animation(7227, UpdatePriority.HIGH);

        private Ranged() {
            super(getDefinition("Demonic Gorilla Ranged"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 31)};
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return ANIMATION;
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 10;
        }
    }

}
