package com.osroyale.game.world.entity.combat.strategy.npc.boss.armadyl;

import com.osroyale.game.world.entity.combat.CombatType;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

public class WingmanSkree extends NpcMagicStrategy {

    public WingmanSkree() {
        super(CombatProjectile.getDefinition("Wingman Skree"));
    }

    @Override
    public boolean canOtherAttack(Mob attacker, Npc defender) {
        if (attacker.isPlayer() && attacker.getStrategy().getCombatType().equals(CombatType.MELEE)) {
            attacker.getPlayer().message("You can't attack this npc with melee!");
            return false;
        }
        return super.canOtherAttack(attacker, defender);
    }

    @Override
    public int getAttackDelay(Npc attacker, Mob defender, FightType fightType) {
        return attacker.definition.getAttackDelay();
    }


    @Override
    public CombatHit[] getHits(Npc attacker, Mob defender) {
        return new CombatHit[] { nextMagicHit(attacker, defender, 16) };
    }

}
