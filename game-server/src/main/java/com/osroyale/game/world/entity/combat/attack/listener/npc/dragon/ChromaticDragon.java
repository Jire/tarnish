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

/** @author Michael | Chex */
@NpcCombatListenerSignature(npcs = {
    /* Lava */  6593,
    /* Green */ 260, 261, 262, 263, 264, 7868, 7869, 7870,
	/* Red */   247, 248, 249, 250, 251,
	/* Blue */  265, 4385, 5878, 5879, 5880, 5881, 5882, 267,
    /* Black */ 252, 253, 254, 255, 256, 257, 258, 259, 2642, 6500, 6501, 6502, 6636, 6652
})
public class ChromaticDragon extends SimplifiedListener<Npc> {
    private static DragonfireStrategy DRAGONFIRE;
    private static CombatStrategy<Npc>[] STRATEGIES;

    static {
        try {
            DRAGONFIRE = new DragonfireStrategy(getDefinition("Chromatic dragonfire"));
            STRATEGIES = createStrategyArray(new CrushMelee(), new StabMelee(), DRAGONFIRE);
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

    private static final class CrushMelee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(80, UpdatePriority.HIGH);

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

    private static final class StabMelee extends NpcMeleeStrategy {
        private static final Animation ANIMATION = new Animation(91, UpdatePriority.HIGH);

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
