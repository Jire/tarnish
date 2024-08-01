package com.osroyale.game.world.entity.combat.strategy.npc.boss.kril;

import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMeleeStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

public class Tstanon extends NpcMeleeStrategy {

    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return new CombatHit[] { nextMeleeHit(attacker, defender, 15) };
    }

}
