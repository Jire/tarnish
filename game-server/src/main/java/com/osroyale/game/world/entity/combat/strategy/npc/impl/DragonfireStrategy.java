package com.osroyale.game.world.entity.combat.strategy.npc.impl;

import com.osroyale.game.Animation;
import com.osroyale.game.UpdatePriority;
import com.osroyale.game.world.entity.combat.CombatUtil;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.CombatHit;
import com.osroyale.game.world.entity.combat.projectile.CombatProjectile;
import com.osroyale.game.world.entity.combat.strategy.npc.NpcMagicStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;

public class DragonfireStrategy extends NpcMagicStrategy {
	
	public DragonfireStrategy(CombatProjectile projectileDefinition) {
		super(projectileDefinition);
	}

	@Override
	public Animation getAttackAnimation(Npc attacker, Mob defender) {
		return new Animation(81, UpdatePriority.VERY_HIGH);
	}
	
	@Override
	public int getAttackDistance(Npc attacker, FightType fightType) {
		return 1;
	}
	
	@Override
	public CombatHit[] getHits(Npc attacker, Mob defender) {
		return new CombatHit[] { CombatUtil.generateDragonfire(attacker, defender, 60, true) };
	}

}