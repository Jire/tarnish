package com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
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
import com.osroyale.game.world.region.RegionManager;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

/**
 * Handles Skotizo's stategy.
 *
 * @author Daniel
 */
public class Skotizo extends MultiStrategy {
    private static Magic MAGIC = new Magic();
    private static LightingRain LIGHTNING_RAIN = new LightingRain();
    private static TeleGrab TELE_GRAB = new TeleGrab();

    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(NpcMeleeStrategy.get(), MAGIC, TELE_GRAB, LIGHTNING_RAIN);
    private static final CombatStrategy<Npc>[] MAGIC_STRATEGIES = createStrategyArray(MAGIC, MAGIC, MAGIC,TELE_GRAB, LIGHTNING_RAIN);

    private static final String[] SHOUTS = {
            "Feel the wrath of Skotizo!",
            "The dark times have come for you all!"
    };

    /** Constructs a new <code>Skotizo</code>. */
    public Skotizo() {
        currentStrategy = MAGIC;
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(MAGIC_STRATEGIES);
        }
        return currentStrategy.canAttack(attacker, defender);
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);

        if (!defender.getCombat().isAttacking()) {
            defender.animate(new Animation(69, UpdatePriority.VERY_HIGH));
            defender.graphic(481);
            defender.speak("ARHHHH! TIME TO SWITCH IT UP!!");
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

    /** Skotizo's magic strategy. */
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
            Projectile projectile = new Projectile(1242, 50, 80, 85, 25);
            attacker.animate(new Animation(69, UpdatePriority.VERY_HIGH));
            RegionManager.forNearbyPlayer(attacker, 16, other -> {
                projectile.send(attacker, other);
                World.schedule(2, () -> other.damage(nextMagicHit(attacker, other, 38)));
            });

            if (Utility.random(0, 2) == 1)
                attacker.speak(Utility.randomElement(SHOUTS));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
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
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.animate(new Animation(69, UpdatePriority.VERY_HIGH));
            attacker.graphic(481);
            attacker.speak("ARHHHH! TIME TO SWITCH IT UP!!");
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
        }
    }

    private static class LightingRain extends NpcMagicStrategy {
        LightingRain() {
            super(CombatProjectile.getDefinition("Vet'ion"));
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.animate(new Animation(69, UpdatePriority.VERY_HIGH));
            attacker.speak("YOU WILL NOW FEEL THE TRUE WRATH OF SKOTIZO!");

            RegionManager.forNearbyPlayer(attacker, 16, other -> {
                Position position = other.getPosition();
                combatProjectile.getProjectile().ifPresent(projectile -> World.sendProjectile(attacker, position, projectile));

                World.schedule(2, () -> {
                    World.sendGraphic(new Graphic(775), position, attacker.instance);
                    if (other.getPosition().equals(position)) {
                        other.damage(new Hit(Utility.random(20, 50)));
                        other.speak("OUCH!");
                        other.message("Skotizo has just electrocuted your entire body! Don't stay in one spot!");
                    }
                });
            });
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 38);
            hit.setAccurate(false);
            return new CombatHit[]{hit};
        }
    }
}
