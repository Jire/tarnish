package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.Deque;
import java.util.LinkedList;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;

/**
 * The combat Script for Ice Demon Boss.
 *
 * @author TJ#5762
 */
public class MutantTarn extends MultiStrategy {
    private static final NpcMeleeStrategy MELEE = NpcMeleeStrategy.get();
    private static final MagicAttack MAGIC = new MagicAttack();
    private static final RangedAttack RANGED = new RangedAttack();

    private static final FrozenSpecial FROZEN_SPECIAL = new FrozenSpecial();
    private static final IceSpecial ICE_SPECIAL = new IceSpecial();

    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(MELEE, MAGIC, RANGED);
    private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, RANGED);
    private final CombatStrategy<Npc>[] SPECIALS = createStrategyArray(ICE_SPECIAL, FROZEN_SPECIAL);

    private final Deque<CombatStrategy<Npc>> strategyQueue = new LinkedList<>();
    private int specialIndex;

    public MutantTarn() {
        currentStrategy = MELEE;
    }

    @Override
    public void init(Npc attacker, Mob defender) {
        if (strategyQueue.isEmpty()) {
            for (int index = 0; index < 6; index++) {
                strategyQueue.add(RandomUtils.random(FULL_STRATEGIES));
            }
            strategyQueue.add(SPECIALS[specialIndex++ % SPECIALS.length]);
        }
        currentStrategy = strategyQueue.poll();
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (currentStrategy == MELEE && !MELEE.canAttack(attacker, defender)) {
            currentStrategy = RandomUtils.random(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public boolean withinDistance(Npc attacker, Mob defender) {
        if (currentStrategy == MELEE && !MELEE.withinDistance(attacker, defender)) {
            currentStrategy = RandomUtils.random(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    private static class IceSpecial extends NpcMagicStrategy {
        IceSpecial() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 30;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 0);
            combatHit.setAccurate(true);
            combatHit.setDamage(-1);
            return new CombatHit[] { combatHit };
        }
    }

    private static class RangedAttack extends NpcMagicStrategy {

        RangedAttack() {
            super(CombatProjectile.getDefinition("Mutant Tarn Ranged"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(5617, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[] { nextRangedHit(attacker, defender, 32) };
        }
    }

    private static class MagicAttack extends NpcMagicStrategy {

        MagicAttack() {
            super(CombatProjectile.getDefinition("Shadow Rush"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(5617, UpdatePriority.HIGH);
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[] { nextRangedHit(attacker, defender, 32) };
        }

    }

    private static class FrozenSpecial extends NpcMagicStrategy {
        private final Projectile PROJECTILE = new Projectile(1324, 5, 85, 85, 25);

        FrozenSpecial() {
            super(CombatProjectile.getDefinition("Mutant Frozen Special"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(5617, UpdatePriority.HIGH);
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            World.schedule(4, () -> {
                int randomHit = Utility.random(1, 30);
                Npc shadow = new Npc(7586, new Position(2524, 4655, 0)) {

                    @Override
                    public void appendDeath() {
                        super.appendDeath();

                    }
                };
                shadow.register();
                shadow.walkTo(defender, () -> {
                   // World.sendGraphic(new Graphic(1460, true), shadow.getPosition());
                    defender.damage(new Hit(randomHit * shadow.getCurrentHealth() / shadow.getMaximumHealth()));
                    defender.graphic(287);
                    defender.graphic(910);
                    shadow.unregister();
                });
            });

        }

        @Override
        public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
            return 15;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, -1);
            combatHit.setAccurate(false);
            return new CombatHit[] { combatHit };
        }
    }
}
