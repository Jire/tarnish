package com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths;

import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.MultiStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.CombatUtil.createStrategyArray;
import static com.osroyale.game.world.entity.combat.CombatUtil.randomStrategy;

/** @author Michael | Chex */
public class Spinolyp extends MultiStrategy {
    private static final Ranged RANGED = new Ranged();
    private static final Magic MAGIC = new Magic();

    private static final CombatStrategy<Npc>[] STRATEGIES = createStrategyArray(RANGED, MAGIC);

    public Spinolyp() {
        currentStrategy = randomStrategy(STRATEGIES);
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    private static final class Magic extends NpcMagicStrategy {

        public Magic() {
            super(CombatProjectile.getDefinition("Water Strike"));
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 15;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextMagicHit(attacker, defender, 10)};
        }
    }

    private static final class Ranged extends NpcRangedStrategy {

        public Ranged() {
            super(CombatProjectile.getDefinition("EMPTY"));
        }

        @Override
        public int getAttackDistance(Npc attacker, FightType fightType) {
            return 15;
        }

        @Override
        public CombatHit[] getHits(Npc attacker, Mob defender) {
            return new CombatHit[]{nextRangedHit(attacker, defender, 10)};
        }
    }

}
