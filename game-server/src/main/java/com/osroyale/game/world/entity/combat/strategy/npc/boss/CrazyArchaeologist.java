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
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.Utility;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * @author Daniel
 */
public class CrazyArchaeologist extends MultiStrategy {
    private static RainAttack RAIN = new RainAttack();
    private static RangeAttack RANGE = new RangeAttack();
    private static final CombatStrategy<Npc>[] FULL_STRATEGIES = createStrategyArray(RAIN, RANGE, NpcMeleeStrategy.get(), RANGE, NpcMeleeStrategy.get());
    private static final CombatStrategy<Npc>[] NON_MELEE = createStrategyArray(RAIN, RANGE, RANGE, RANGE, RANGE);

    private static final String[] MESSAGES = {
            "I'm Bellock - respect me!",
            "Get off my site!",
            "No-one messes with Bellock's dig!",
            "These ruins are mine!",
            "Taste my knowledge!",
            "You belong in a museum!",
    };

    public CrazyArchaeologist() {
        currentStrategy = randomStrategy(NON_MELEE);
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
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
        if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            currentStrategy = randomStrategy(FULL_STRATEGIES);
        } else {
            currentStrategy = randomStrategy(NON_MELEE);
        }
        if (currentStrategy != RAIN) {
            attacker.speak(Utility.randomElement(MESSAGES));
        }
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static class RainAttack extends NpcMagicStrategy {

        RainAttack() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public void start(Npc attacker, Mob defender, Hit[] hits) {
            attacker.animate(new Animation(1162, UpdatePriority.VERY_HIGH));
            attacker.speak("Rain of knowledge!");
            for (int i = 0; i < 3; i++) {
                int offsetX = defender.getX() - attacker.getX();
                int offsetY = defender.getY() - attacker.getY();
                if (i == 0 || i == 2) {
                    offsetX += i == 0 ? -1 : 1;
                    offsetY++;
                }
                World.sendProjectile(new Projectile(1260, 46, 80, 43, 31), attacker.getPosition(), attacker.instance, -1, (byte) offsetX, (byte) offsetY);
                Position end = new Position(attacker.getX() + offsetX, attacker.getY() + offsetY, 0);
                World.schedule(3, () -> {
                    if (defender.getPosition().equals(end)) {
                        defender.damage(nextMagicHit(attacker, defender, 23, combatProjectile));
                    }
                    World.sendGraphic(new Graphic(131, UpdatePriority.HIGH), end, attacker.instance);
                });
            }
        }

        @Override
        public void hit(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public void attack(Npc attacker, Mob defender, Hit hit) {
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit hit = nextMagicHit(attacker, defender, 23, combatProjectile);
            hit.setAccurate(false);
            return new CombatHit[] {hit};
        }

        @Override
        public int modifyAccuracy(Npc attacker, Mob defender, int roll) {
            return roll + 50_000;
        }

    }

    private static class RangeAttack extends NpcRangedStrategy {
        public RangeAttack() {
            super(getDefinition("Archaeologist Range"));
        }
    }
}
