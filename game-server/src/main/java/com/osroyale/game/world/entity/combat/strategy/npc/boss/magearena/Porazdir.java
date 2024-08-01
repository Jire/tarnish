package com.osroyale.game.world.entity.combat.strategy.npc.boss.magearena;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

/**
 * Handles Porazdir Combat Strategy
 *
 * @author TJ#5762
 */
public class Porazdir extends MultiStrategy {
    private static Magic MAGIC = new Magic();
    private static Melee MELEE = new Melee();

    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC);
    private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC);
    private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, MELEE, MELEE, MAGIC, MAGIC);

    /** Constructs a new <code>Porazdir</code>. */
    public Porazdir() {
        currentStrategy = MAGIC;
        currentStrategy = MELEE;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.canAttack(attacker, defender)) {
            currentStrategy = randomStrategy(MAGIC_STRATEGIES);
            currentStrategy = randomStrategy(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);

        if (!defender.getCombat().isAttacking()) {
            defender.animate(new Animation(7843, UpdatePriority.VERY_HIGH));
            defender.graphic(1196);
            defender.graphic(481);
            if (Utility.random(1, 25) == 2) {
                defender.prayer.deactivate(Prayer.PROTECT_FROM_MAGIC, Prayer.PROTECT_FROM_MELEE,
                        Prayer.PROTECT_FROM_RANGE);
                defender.getPlayer().send(new SendMessage("Your overhead prayers have been disabled!"));
            }
            CombatUtil.areaAction(attacker, 64, 18, mob -> {
                if (RandomUtils.success(.65))
                    return;

                World.schedule(2, () -> {
                    Position destination = Utility.randomElement(defender.boundaries);
                    World.sendGraphic(new Graphic(481), destination);
                    attacker.move(destination);

                });
            });
        }
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        currentStrategy.finishOutgoing(attacker, defender);
        if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        } else {
            currentStrategy = randomStrategy(MAGIC_STRATEGIES);
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static class Melee extends NpcRangedStrategy {

        public Melee() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {

            attacker.animate(new Animation(7840, UpdatePriority.VERY_HIGH));
            CombatHit hit = nextMeleeHit(attacker, defender, 21);
            defender.graphic(1176);
            CombatUtil.areaAction(attacker, 64, 18, mob -> {
                mob.damage(nextMagicHit(attacker, defender, 38));
            });

        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextRangedHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[] { hit };
        }

        @Override
        public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
            return roll + 50_000;
        }

    }

    /** Jisticiar magic strategy. */
    private static class Magic extends NpcMagicStrategy {
        public Magic() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            Projectile projectile = new Projectile(1378, 50, 80, 85, 25);
            attacker.animate(new Animation(7841, UpdatePriority.VERY_HIGH));
            CombatUtil.areaAction(attacker, 64, 18, mob -> {
                projectile.send(attacker, defender);
                defender.graphic(157);
                mob.damage(nextMagicHit(attacker, defender, 35));

            });

            if (Utility.random(0, 10) == 1) {
                attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
                attacker.graphic(new Graphic(1296, UpdatePriority.VERY_HIGH));
                attacker.heal(130);
                attacker.speak("Time To HEAL!");
                defender.getPlayer().send(new SendMessage("Porazdir heals himself!"));

            }

        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[] { hit };
        }

        @Override
        public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
            return roll + 50_000;
        }
    }

}
