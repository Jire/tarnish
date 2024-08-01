package com.osroyale.game.world.entity.combat.strategy.npc.boss.scorpia;

import com.google.common.base.Stopwatch;
import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.movement.waypoint.Waypoint;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

/** @author Daniel */
public class Scorpia extends MultiStrategy {
    private boolean hasGuardians;

    public Scorpia() {
        currentStrategy = new Melee();
    }

    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
        currentStrategy.block(attacker, defender, hit, combatType);
        defender.getCombat().attack(attacker);
    }

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
        if (hasGuardians || attacker.getCurrentHealth() >= 100) {
            return;
        }
        hasGuardians = true;
        for (int i = 0; i < 2; i++) {
            Position spawn = RandomUtils.random(Utility.getInnerBoundaries(attacker));
            Npc guardian = new Guardian(spawn, attacker);
            guardian.register();
            guardian.definition.setRespawnTime(-1);
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private class GuardianWaypoint extends Waypoint {
        final Npc scorpia;

        GuardianWaypoint(Guardian guardian, Npc scorpia) {
            super(guardian, scorpia);
            this.scorpia = scorpia;
        }

        @Override
        protected void onDestination() {
            CombatProjectile.getDefinition("Scorpia guardian").getProjectile().ifPresent(projectile -> projectile.send(mob, scorpia));
            mob.animate(new Animation(6261));
            scorpia.heal(2);
            ((Guardian) mob).lastHeal.reset();
        }
    }

    private class Guardian extends Npc {
        private final Stopwatch lastHeal = Stopwatch.createStarted();

        private Guardian(Position spawn, Npc scorpia) {
            super(6617, spawn);
            setWaypoint(new Waypoint(this, scorpia) {
                @Override
                protected void onDestination() {
                    CombatProjectile.getDefinition("Scorpia guardian").getProjectile().ifPresent(projectile -> projectile.send(mob, scorpia));
                    lastHeal.reset();
                    lastHeal.start();
                    mob.animate(new Animation(6261));
                    scorpia.heal(2);
                }
            });
        }

        @Override
        public void sequence() {
            super.sequence();
            long millis = lastHeal.elapsed().toMillis();
            if (millis > 15_000) {
                unregister();
            }
        }
    }

    private static final class Melee extends NpcMeleeStrategy {
        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMeleeHit(attacker, defender, 16)};
        }
    }
}
