package com.osroyale.game.world.entity.combat.attack.listener.npc.dragon;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.attack.listener.NpcCombatListenerSignature;
import com.osroyale.game.world.entity.combat.attack.listener.SimplifiedListener;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.impl.DragonfireStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;
import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Daniel */
@NpcCombatListenerSignature(npcs = {467})
public class SkeletalWyvern extends SimplifiedListener<Npc> {
    private static DragonfireStrategy DRAGONFIRE;
    private static CombatStrategy<Npc>[] STRATEGIES;

    static {
        try {
            DRAGONFIRE = new DragonfireStrategy(getDefinition("Skeletal wyvern breathe"));
            STRATEGIES = createStrategyArray(new Melee(),  DRAGONFIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canAttack(Npc attacker, Mob defender) {
        if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            attacker.setStrategy(DRAGONFIRE);
        }
        return attacker.getStrategy().canAttack(attacker, defender);
    }

    @Override
    public void finishOutgoing(Npc attacker, Mob defender) {
        if (!NpcMeleeStrategy.get().withinDistance(attacker, defender)) {
            attacker.setStrategy(DRAGONFIRE);
        } else {
            attacker.setStrategy(randomStrategy(STRATEGIES));
        }
    }

    private static final class Melee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(422, UpdatePriority.HIGH);

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
