package com.osroyale.game.world.entity.combat.strategy.npc.boss;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/**
 * Created by Daniel on 2017-11-29.
 */
public class Wyrm extends MultiStrategy {
    private static final Melee MELEE = new Melee();
    private static final Magic MAGIC = new Magic();

    private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(MAGIC, MELEE);
    private int attackCount = 0;

    public Wyrm() {
        this.currentStrategy = randomStrategy(STRATEGIES);
    }

    @Override
    public boolean withinDistance(Npc attacker, Mob defender) {
        if (!currentStrategy.withinDistance(attacker, defender)) {
            currentStrategy = MAGIC;
        }
        return currentStrategy.withinDistance(attacker, defender);
    }
    @Override
    public void block(Mob attacker, Npc defender, Hit hit, CombatType combatType) {
            hit.getDamage();
        if (defender.definition.getId() == 8610) {
            World.schedule(new Task(1) {
                               @Override
                               public void execute() {
                                   defender.transform(8611);
                               }
                           });
        }
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!currentStrategy.canAttack(attacker, defender)) {
            currentStrategy = MAGIC;
        } else currentStrategy = MELEE;
        return currentStrategy.canAttack(attacker, defender);
    }
    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        currentStrategy.finishOutgoing(attacker, defender);
        if (NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            currentStrategy = MELEE;
        } else {
            currentStrategy = MAGIC;
        }
    }

    @Override
    public void hit(Npc attacker, Mob defender, Hit hit) {
        super.hit(attacker, defender, hit);
    }

    @Override
    public int modifyDefensive(Mob attacker, Npc defender, int roll) {
        return (int) (roll * 2.3);
    }

    private static final class Melee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(8271, UpdatePriority.HIGH);

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 1;
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
        private static final Animation ANIMATION = new Animation(8270, UpdatePriority.HIGH);

        private Magic() {
            super(getDefinition("Wyrm Magic"));
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
            return 5;
        }
    }

}
