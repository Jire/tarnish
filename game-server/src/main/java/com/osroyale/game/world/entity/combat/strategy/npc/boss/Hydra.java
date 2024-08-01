package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

public class Hydra extends MultiStrategy {
    private static final Magic MAGIC = new Magic();
    private static final Ranged RANGED = new Ranged();
    private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(MAGIC, RANGED);
    private int attackCount = 0;

    public Hydra() {
        this.currentStrategy = randomStrategy(STRATEGIES);
    }

    private CombatStrategy getNextStrategy() {
        return RandomUtils.randomExclude(STRATEGIES, currentStrategy);
    }

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
        attackCount++;
        if (attackCount > 2) {
            if (Utility.hasOneOutOf(30)) {
                poisonAttack(attacker, defender);
            }
            currentStrategy = getNextStrategy();
            attackCount = 0;
        }
    }

    private static void poisonAttack(Npc attacker, Mob defender) {
        Position[] boundaries = Utility.getInnerBoundaries(defender.getPosition(), 5, 5);
        List<Position> positions = Arrays.asList(boundaries);
        Collections.shuffle(positions);

        attacker.animate(new Animation(8262, UpdatePriority.VERY_HIGH));
        World.sendProjectile(attacker, defender, new Projectile(1644, 37, 15, 42, 0));
        attacker.attributes.set("HYDRA_POISON", Boolean.TRUE);

        World.schedule(poisonTask(attacker, defender, defender.getPosition()));
        World.schedule(poisonTask(attacker, defender, positions.get(0)));
        World.schedule(poisonTask(attacker, defender, positions.get(1)));
    }


    private static Task poisonTask(Npc attacker, Mob defender, Position position) {
        return new Task(1) {
            int count = 0;

            @Override
            public void execute() {
                if (count == 0) {
                    World.sendGraphic(new Graphic(1654), position);
                } else if (count == 18) {
                    attacker.attributes.set("HYDRA_POISON", Boolean.FALSE);
                    cancel();
                    return;
                }
                if (count % 2 == 0 && defender.getPosition().equals(position)) {
                    var damage = 4;
                    if (defender.getPlayer().getPoisonImmunity().get() > 0) {
                        damage = Utility.random(1, 3, true);
                        defender.getPlayer().message("Your poison immunity reduces the damage taken from the acidic pool.");
                    }
                    defender.writeDamage(new Hit(damage));
                }
                count++;
            }
        };
    }

    private static class Magic extends NpcMagicStrategy {
        private static final Animation ANIMATION = new Animation(8263, UpdatePriority.HIGH);

        private Magic() {
            super(getDefinition("Hydra Magic"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            CombatHit combatHit = nextMagicHit(attacker, defender, 22);
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
        private static final Animation ANIMATION = new Animation(8261, UpdatePriority.HIGH);

        private Ranged() {
            super(getDefinition("Hydra Ranged"));
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 22)};
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
