package com.osroyale.game.world.entity.combat.strategy.npc.boss.magearena;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.impl.ForceMovementTask;
import com.osroyale.game.world.World;
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
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.ForceMovement;
import com.osroyale.game.world.pathfinding.path.SimplePathChecker;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

/**
 * Handles Derwen Combat Strategy
 *
 * @author TJ#5762
 */
public class Derwen extends MultiStrategy {
    private static Magic MAGIC = new Magic();
    private static Melee MELEE = new Melee();
    private static LightingRain LIGHTNING_RAIN = new LightingRain();
    private static TeleGrab TELE_GRAB = new TeleGrab();

    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC,
            TELE_GRAB, LIGHTNING_RAIN);
    private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC, TELE_GRAB,
            LIGHTNING_RAIN);
    private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(MAGIC, MELEE, MELEE, MAGIC, MAGIC,
            TELE_GRAB, LIGHTNING_RAIN);

    /** Constructs a new <code>Derwen</code>. */
    public Derwen() {
        currentStrategy = MAGIC;
        currentStrategy = MELEE;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(MAGIC_STRATEGIES);
            currentStrategy = randomStrategy(NON_MELEE);
        }
        return currentStrategy.canAttack(attacker, defender);
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

            attacker.animate(new Animation(7848, UpdatePriority.VERY_HIGH));
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
            attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
            Projectile projectile = new Projectile(1379, 50, 80, 85, 25);
            CombatUtil.areaAction(attacker, 64, 18, mob -> {
                projectile.send(attacker, defender);
                defender.graphic(157);
                mob.damage(nextMagicHit(attacker, defender, 35));

            });

            if (Utility.random(0, 25) == 1) {
                attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
                attacker.graphic(new Graphic(1296, UpdatePriority.VERY_HIGH));
                attacker.heal(130);
                attacker.speak("Time To HEAL!");
                defender.getPlayer().send(new SendMessage("Derwen heals himself!"));

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

    private static class TeleGrab extends NpcMagicStrategy {
        TeleGrab() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[] { hit };
        }
    }

    private static class LightingRain extends NpcMagicStrategy {
        LightingRain() {
            super(CombatProjectile.getDefinition("Vorkath Frozen Special"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.animate(new Animation(7849, UpdatePriority.VERY_HIGH));
            World.sendProjectile(attacker, defender, new Projectile(1382, 46, 80, 43, 31));
            World.schedule(1, () -> {
                if (defender.isPlayer()) {
                    Position current = defender.getPosition();
                    Position best = Utility.findBestInside(defender, attacker);
                    int dx = current.getX() - best.getX();
                    int dy = current.getY() - best.getY();

                    int y = dy / (dx == 0 ? dy : dx);
                    Position destination = current.transform(dx, y);
                    if (SimplePathChecker.checkLine(defender, destination))
                    defender.damage(new Hit(Utility.random(1, 3)));
                    defender.interact(attacker);
                }
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[] { hit };
        }
    }
}
