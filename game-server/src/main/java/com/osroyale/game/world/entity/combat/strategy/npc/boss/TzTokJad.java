package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
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

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
public class TzTokJad extends MultiStrategy {
    private static MeleeAttack MELEE = new MeleeAttack();
    private static RangedAttack RANGED = new RangedAttack();
    private static MagicAttack MAGIC = new MagicAttack();
    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(MELEE, RANGED, MAGIC);
    private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(RANGED, MAGIC);

    public TzTokJad() {
        currentStrategy = randomStrategy(NON_MELEE);
    }

    @Override
    public boolean withinDistance(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(NON_MELEE);
        }
        return currentStrategy.withinDistance(attacker, defender);
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.canAttack(attacker, defender)) {
            currentStrategy = randomStrategy(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        currentStrategy.finishOutgoing(attacker, defender);
        if (MELEE.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        } else {
            currentStrategy = randomStrategy(NON_MELEE);
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static class MeleeAttack extends NpcMeleeStrategy {

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(2655, UpdatePriority.HIGH);
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            if (defender.prayer.isActive(Prayer.PROTECT_FROM_MELEE)) {
                hit.modifyDamage(damage -> 0);
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender, 97, 0, 0, false)};
        }
    }

    private static class RangedAttack extends NpcRangedStrategy {

        RangedAttack() {
            super(getDefinition("EMPTY"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(2652, UpdatePriority.HIGH);
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            World.sendGraphic(new Graphic(451, 30, UpdatePriority.HIGH), defender.getPosition());
            defender.graphic(new Graphic(157, 90, true, UpdatePriority.HIGH));

            if (defender.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
                hit.modifyDamage(damage -> 0);
            }
        }

        @Override
        public void hitsplat(Npc attacker, Mob defender, Hit hit) {
            if (defender.prayer.isActive(Prayer.PROTECT_FROM_RANGE)) {
                hit.modifyDamage(damage -> 0);
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 97, 0, 4)};
        }
    }

    private static class MagicAttack extends NpcMagicStrategy {

        public MagicAttack() {
            super(getDefinition("EMPTY"));
        }

        @Override
        public Animation getAttackAnimation(Npc attacker, Mob defender) {
            return new Animation(2656, UpdatePriority.HIGH);
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
            attacker.graphic(new Graphic(447, 0, 0x1F40000, UpdatePriority.HIGH));
            World.sendProjectile(attacker, defender, new Projectile(448, 70, 100, 140, 20));
            World.sendProjectile(attacker, defender, new Projectile(449, 75, 100, 140, 20));
            World.sendProjectile(attacker, defender, new Projectile(450, 80, 100, 140, 20));
            defender.graphic(new Graphic(157, 100, true, UpdatePriority.HIGH));
        }

        @Override
        public void hitsplat(Npc attacker, Mob defender, Hit hit) {
            if (defender.prayer.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                hit.modifyDamage(damage -> 0);
            }
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 97, 0, 4);
            hit.setAccurate(true);
            return new CombatHit[]{hit};
        }
    }

}
