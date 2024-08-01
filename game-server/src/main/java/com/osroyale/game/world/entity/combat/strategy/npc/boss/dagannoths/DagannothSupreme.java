package com.osroyale.game.world.entity.combat.strategy.npc.boss.dagannoths;

import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcRangedStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

import static com.osroyale.game.world.entity.combat.projectile.CombatProjectile.getDefinition;

/** @author Michael | Chex */
public class DagannothSupreme extends NpcRangedStrategy {

    public DagannothSupreme() {
        super(getDefinition("Dagannoth SUPREME"));
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }

    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return new CombatHit[] { nextRangedHit(attacker, defender, 30) };
    }

}
