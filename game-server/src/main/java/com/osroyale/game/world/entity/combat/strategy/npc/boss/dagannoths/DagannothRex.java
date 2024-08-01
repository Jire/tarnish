package com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

/** @author Michael | Chex */
public class DagannothRex extends MultiStrategy {
    private static final CrushMelee CRUSH = new CrushMelee();
    private static final SlashMelee SLASH = new SlashMelee();

    private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(CRUSH, SLASH);

    public DagannothRex() {
        currentStrategy = randomStrategy(STRATEGIES);
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static final class CrushMelee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(2853, UpdatePriority.HIGH);

        @Override
        public void finishOutgoing(Npc attacker, Mob defender) {
            attacker.getCombat().setFightType(FightType.SCYTHE_JAB);
        }

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

    private static final class SlashMelee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(2851, UpdatePriority.HIGH);

        @Override
        public void finishOutgoing(Npc attacker, Mob defender) {
            attacker.getCombat().setFightType(FightType.SCYTHE_REAP);
        }

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

}
